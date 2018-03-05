package com.ivan.sub.miniroulette.service;

import static java.lang.Boolean.FALSE;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.util.ReflectionTestUtils;

import com.ivan.sub.miniroulette.model.BalanceChangedEvent;
import com.ivan.sub.miniroulette.model.entity.Session;
import com.ivan.sub.miniroulette.repository.SessionRepo;

/**
 * Unit tests for {@link SessionServiceImpl}.
 */
@RunWith(MockitoJUnitRunner.class)
public class SessionServiceTest {

  private static final String SESSION_ID_1 = "ONE";
  private static final Integer START_BALANCE = 1000;
  private static final String BALANCE_INITIALIZED_MESSAGE = "Balance initialized.";

  private SessionService sessionService;

  @Mock
  private ApplicationEventPublisher eventPublisher;

  @Mock
  private SessionRepo sessionRepo;

  @Captor
  private ArgumentCaptor<Session> sessionCaptor;

  @Captor
  private ArgumentCaptor<BalanceChangedEvent> balanceChangedEventCaptor;

  @Before
  public void setUp() throws Exception {
    sessionService = new SessionServiceImpl(eventPublisher, sessionRepo);
    ReflectionTestUtils.setField(sessionService, "balanceInitializedMessage", BALANCE_INITIALIZED_MESSAGE);
    ReflectionTestUtils.setField(sessionService, "startBalance", START_BALANCE);
  }

  @Test
  // Convention for test name: MethodName_StateUnderTest_ExpectedBehavior
  public void checkSessionWasInitialized_SessionNotInitialized_NewSessionCreated() {
    doReturn(null).when(sessionRepo).findOne(SESSION_ID_1);
    Session session = new Session();
    session.setId(SESSION_ID_1);
    doReturn(session).when(sessionRepo).save(any(Session.class));

    sessionService.checkSessionWasInitialized(SESSION_ID_1);

    verify(sessionRepo, times(1)).findOne(eq(SESSION_ID_1));

    verify(sessionRepo, times(1)).save(sessionCaptor.capture());
    Session sessionPassedToSave = sessionCaptor.getValue();
    assertThat(sessionPassedToSave.getId(), is(SESSION_ID_1));
    assertThat(sessionPassedToSave.getBalance(), is(START_BALANCE));
    assertThat(sessionPassedToSave.getIsInGame(), is(FALSE));

    verify(eventPublisher, times(1)).publishEvent(balanceChangedEventCaptor.capture());
    BalanceChangedEvent event = balanceChangedEventCaptor.getValue();

    assertThat(event.getMessage(), is(BALANCE_INITIALIZED_MESSAGE));
    assertThat(event.getSessions().stream().map(Session::getId).collect(toList()), contains(equalTo(SESSION_ID_1)));

    verifyNoMoreInteractions(eventPublisher, sessionRepo);
  }

}
