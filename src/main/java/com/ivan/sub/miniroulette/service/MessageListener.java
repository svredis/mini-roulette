package com.ivan.sub.miniroulette.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import com.ivan.sub.miniroulette.model.BalanceChangedEvent;

/**
 * Container for event listeners which are used to sent messages to clients.
 */
@Component
public class MessageListener {

  private static final Logger logger = LoggerFactory.getLogger(MessageListener.class);

  private EmitterService emitterService;

  @Autowired
  public MessageListener(EmitterService emitterService) {
    this.emitterService = emitterService;
  }

  /**
   * Listener to handle {@link BalanceChangedEvent}.
   *
   * @param event is input event object.
   */
  @Async("messageEventExecutor")
  @TransactionalEventListener
  public void onBalanceChanged(BalanceChangedEvent event) {
    logger.debug("New event handling has been started. Try to send message [{}] for {} session(s).", event.getMessage(), event.getSessions().size());
    event.getSessions()
        .forEach(s -> emitterService.sendMessage(s, event.getMessage()));
  }

}
