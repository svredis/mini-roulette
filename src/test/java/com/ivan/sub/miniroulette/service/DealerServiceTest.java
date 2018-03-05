package com.ivan.sub.miniroulette.service;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.util.ReflectionTestUtils;

import com.ivan.sub.miniroulette.model.BalanceChangedEvent;
import com.ivan.sub.miniroulette.model.entity.Round;
import com.ivan.sub.miniroulette.model.entity.Session;
import com.ivan.sub.miniroulette.repository.RoundRepo;
import com.ivan.sub.miniroulette.repository.SessionRepo;

/**
 * Unit tests for {@link DealerServiceImpl}.
 */
@RunWith(MockitoJUnitRunner.class)
public class DealerServiceTest {

  private static final String WON_MESSAGE = "Won";
  private static final String LOST_MESSAGE = "Lost";
  private static final String BID_ACCEPTED_MESSAGE = "Bid accepted.";
  private static final String SESSION_ID_2 = "TWO";
  private static final String SESSION_ID_1 = "ONE";
  private static final byte ROUND_RESULT_WON = (byte) 1;
  private static final byte ROUND_RESULT_LOST = (byte) 0;
  private static final int EXPECTED_INCREMENT_WON = 2;
  private static final int EXPECTED_INCREMENT_LOST = 0;

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  private DealerService dealerService;

  @Mock
  private ApplicationEventPublisher eventPublisher;

  @Mock
  private SessionRepo sessionRepo;

  @Mock
  private RoundRepo roundRepo;


  @Captor
  private ArgumentCaptor<List<String>> idsListCaptor;

  @Captor
  private ArgumentCaptor<BalanceChangedEvent> balanceChangedEventCaptor;

  @Before
  public void setUp() {
    dealerService = new DealerServiceImpl(eventPublisher, sessionRepo, roundRepo);
    ReflectionTestUtils.setField(dealerService, "wonMessage", WON_MESSAGE);
    ReflectionTestUtils.setField(dealerService, "lostMessage", LOST_MESSAGE);
    ReflectionTestUtils.setField(dealerService, "bidWasAcceptedMessage", BID_ACCEPTED_MESSAGE);
    ReflectionTestUtils.setField(dealerService, "wonIncrement", EXPECTED_INCREMENT_WON);
    ReflectionTestUtils.setField(dealerService, "lostIncrement", EXPECTED_INCREMENT_LOST);
  }

  @Test
  // Convention for test name: MethodName_StateUnderTest_ExpectedBehavior
  public void spin_NoPlayers_RoundNotSaved() {
    doReturn(emptyList()).when(sessionRepo).findByIsInGameIsTrue();

    dealerService.spin();

    verify(sessionRepo, times(1)).findByIsInGameIsTrue();
    verifyNoMoreInteractions(eventPublisher, sessionRepo, roundRepo);
  }

  @Test
  // Convention for test name: MethodName_StateUnderTest_ExpectedBehavior
  public void spin_ResultIs1_AllPlayersWon() {
    List<String> registeredPlayersIds = Arrays.asList(SESSION_ID_1, SESSION_ID_2);
    Session s1 = new Session();
    s1.setId(SESSION_ID_1);
    Session s2 = new Session();
    s2.setId(SESSION_ID_2);
    List<Session> registeredPlayers = Arrays.asList(s1, s2);

    doReturn(registeredPlayersIds).when(sessionRepo).findByIsInGameIsTrue();
    doReturn(registeredPlayers).when(sessionRepo).findByIdIn(registeredPlayersIds);
    doReturn(new Round(ROUND_RESULT_WON)).when(roundRepo).save(any(Round.class));

    dealerService.spin();

    verify(sessionRepo, times(1)).findByIsInGameIsTrue();
    verify(roundRepo, times(1)).save(any(Round.class));

    verify(sessionRepo, times(1)).updateWithRoundResult(idsListCaptor.capture(), eq(EXPECTED_INCREMENT_WON));
    assertThat(idsListCaptor.getValue(), containsInAnyOrder(registeredPlayersIds.toArray()));

    verify(sessionRepo, times(1)).findByIdIn(idsListCaptor.capture());
    assertThat(idsListCaptor.getValue(), containsInAnyOrder(registeredPlayersIds.toArray()));

    verify(eventPublisher, times(1)).publishEvent(balanceChangedEventCaptor.capture());
    BalanceChangedEvent event = balanceChangedEventCaptor.getValue();

    assertThat(event.getMessage(), is(WON_MESSAGE));
    assertThat(event.getSessions().stream().map(Session::getId).collect(toList()), containsInAnyOrder(equalTo(SESSION_ID_1), equalTo(SESSION_ID_2)));

    verifyNoMoreInteractions(eventPublisher, sessionRepo, roundRepo);
  }

  @Test
  // Convention for test name: MethodName_StateUnderTest_ExpectedBehavior
  public void spin_ResultIs0_AllPlayersLost() {
    List<String> registeredPlayersIds = singletonList(SESSION_ID_1);
    Session s1 = new Session();
    s1.setId(SESSION_ID_1);
    List<Session> registeredPlayers = singletonList(s1);

    doReturn(registeredPlayersIds).when(sessionRepo).findByIsInGameIsTrue();
    doReturn(registeredPlayers).when(sessionRepo).findByIdIn(registeredPlayersIds);
    doReturn(new Round(ROUND_RESULT_LOST)).when(roundRepo).save(any(Round.class));

    dealerService.spin();

    verify(sessionRepo, times(1)).findByIsInGameIsTrue();
    verify(roundRepo, times(1)).save(any(Round.class));

    verify(sessionRepo, times(1)).updateWithRoundResult(idsListCaptor.capture(), eq(EXPECTED_INCREMENT_LOST));
    assertThat(idsListCaptor.getValue(), containsInAnyOrder(registeredPlayersIds.toArray()));

    verify(sessionRepo, times(1)).findByIdIn(idsListCaptor.capture());
    assertThat(idsListCaptor.getValue(), containsInAnyOrder(registeredPlayersIds.toArray()));

    verify(eventPublisher, times(1)).publishEvent(balanceChangedEventCaptor.capture());
    BalanceChangedEvent event = balanceChangedEventCaptor.getValue();

    assertThat(event.getMessage(), is(LOST_MESSAGE));
    assertThat(event.getSessions().stream().map(Session::getId).collect(toList()), contains(equalTo(SESSION_ID_1)));

    verifyNoMoreInteractions(eventPublisher, sessionRepo, roundRepo);
  }


  @Test
  public void takeBid_SessionNotFoundInDb_ExpectedIllegalStateException() {
    expectedException.expect(IllegalStateException.class);
    doReturn(null).when(sessionRepo).findOne(any(String.class));

    dealerService.takeBid(SESSION_ID_1);
  }

  @Test
  public void takeBid_UpdateResultIs1_EventPublished() {
    Session session = new Session();
    session.setId(SESSION_ID_1);
    doReturn(session).when(sessionRepo).findOne(any(String.class));
    doReturn(1).when(sessionRepo).updateToBeInGame(SESSION_ID_1);

    dealerService.takeBid(SESSION_ID_1);

    verify(eventPublisher, times(1)).publishEvent(balanceChangedEventCaptor.capture());
    BalanceChangedEvent event = balanceChangedEventCaptor.getValue();

    assertThat(event.getMessage(), is(BID_ACCEPTED_MESSAGE));
    assertThat(event.getSessions().stream().map(Session::getId).collect(toList()), contains(equalTo(SESSION_ID_1)));
  }

  @Test
  public void takeBid_UpdateResultIs0_EventNotPublished() {
    Session session = new Session();
    session.setId(SESSION_ID_1);
    doReturn(session).when(sessionRepo).findOne(any(String.class));
    doReturn(0).when(sessionRepo).updateToBeInGame(SESSION_ID_1);

    dealerService.takeBid(SESSION_ID_1);

    verify(eventPublisher, times(0)).publishEvent(any());
  }

}