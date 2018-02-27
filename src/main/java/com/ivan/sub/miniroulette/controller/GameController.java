package com.ivan.sub.miniroulette.controller;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.ivan.sub.miniroulette.service.MessageService;

/**
 * Created on 2/27/18.
 */
@Controller
public class GameController {

  private MessageService messageService;

  @Autowired
  public GameController(MessageService messageService) {
    this.messageService = messageService;
  }

  @RequestMapping("/")
  public String home() {
    return "home";
  }

  @GetMapping("/api/bet")
  public void doBet(HttpServletResponse response, HttpSession session) {
    response.setHeader("Cache-Control", "no-store");

    System.out.println("BET ENDPOINT STARTED");


    System.out.println("session id : " + session.getId());

    messageService.sendMessage(session.getId(), "Msg from doBet method.");
//    checkSession(sessionContext);
//    checkBet();
//    checkBalance(sessionContext);
  }

  @GetMapping("/api/sse")
  public SseEmitter getSseEmitter(HttpServletResponse response, HttpSession session) {
    response.setHeader("Cache-Control", "no-store");

    System.out.println("Get sse STARTED");


    System.out.println("session id : " + session.getId());


    return messageService.getEmitter(session.getId());
  }
}
