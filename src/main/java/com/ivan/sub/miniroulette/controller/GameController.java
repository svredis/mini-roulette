package com.ivan.sub.miniroulette.controller;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.ivan.sub.miniroulette.service.DealerService;
import com.ivan.sub.miniroulette.service.EmitterService;
import com.ivan.sub.miniroulette.service.SessionService;

/**
 * Entry point for playing roulette.
 */
@Controller
public class GameController {

  private EmitterService emitterService;

  private SessionService sessionService;

  private DealerService dealerService;

  @Autowired
  public GameController(EmitterService emitterService, SessionService sessionService, DealerService dealerService) {
    this.emitterService = emitterService;
    this.sessionService = sessionService;
    this.dealerService = dealerService;
  }

  @RequestMapping("/")
  public String home() {
    return "home";
  }

  /**
   * Take a bid for next round from given session.
   *
   * @param response is {@link HttpServletResponse} object to manage response configuration.
   * @param session is {@link HttpSession} object to manage session configuration.
   */
  @GetMapping("/api/bid")
  public void takeBid(HttpServletResponse response, HttpSession session) {
    response.setHeader("Cache-Control", "no-store");
    dealerService.takeBid(session.getId());
  }

  /**
   * Get SseEmitter for given session and create new session entity if session wasn't initialized.
   *
   * @param response is {@link HttpServletResponse} object to manage response configuration.
   * @param httpSession is {@link HttpSession} object to manage session configuration.
   *
   * @return SseEmitter that is associated with given http session.
   */
  @GetMapping("/api/sse")
  public SseEmitter getSseEmitter(HttpServletResponse response, HttpSession httpSession) {
    response.setHeader("Cache-Control", "no-store");
    SseEmitter emitter = emitterService.getEmitter(httpSession.getId());
    sessionService.checkSessionWasInitialized(httpSession.getId());
    return emitter;
  }

}
