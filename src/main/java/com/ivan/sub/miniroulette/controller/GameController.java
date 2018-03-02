package com.ivan.sub.miniroulette.controller;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.ivan.sub.miniroulette.model.Session;
import com.ivan.sub.miniroulette.service.DealerService;
import com.ivan.sub.miniroulette.service.MessageService;
import com.ivan.sub.miniroulette.service.SessionService;

/**
 * Created on 2/27/18.
 */
@Controller
public class GameController {

  private static final Logger logger = LoggerFactory.getLogger(GameController.class);

  private MessageService messageService;

  private SessionService sessionService;

  private DealerService dealerService;

  @Autowired
  public GameController(MessageService messageService, SessionService sessionService, DealerService dealerService) {
    this.messageService = messageService;
    this.sessionService = sessionService;
    this.dealerService = dealerService;
  }

  @RequestMapping("/")
  public String home() {
    return "home";
  }

  @GetMapping("/api/bet")
  public void doBet(HttpServletResponse response, HttpSession session) {
    response.setHeader("Cache-Control", "no-store");
    logger.debug("Session with id = {} is trying to do a bet.", session.getId());

    dealerService.takeBid(session.getId());

    messageService.sendMessage(session.getId(), "Your bid is accepted. Your balance has been reduced by one unit.",
        sessionService.getSession(session.getId()).getBalance());
  }

  @GetMapping("/api/sse")
  public SseEmitter getSseEmitter(HttpServletResponse response, HttpSession httpSession) {
    response.setHeader("Cache-Control", "no-store");

    logger.debug("An emitter is requested for httpSession with id = {}", httpSession.getId());

    SseEmitter emitter = messageService.getEmitter(httpSession.getId());

    Session session = sessionService.getSession(httpSession.getId());

    messageService.sendMessage(session.getSessionId(), null, session.getBalance());

    return emitter;
  }

}
