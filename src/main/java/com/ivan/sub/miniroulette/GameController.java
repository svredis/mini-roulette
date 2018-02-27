package com.ivan.sub.miniroulette;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * Created on 2/27/18.
 */
@Controller
public class GameController {

  @RequestMapping("/")
  public String home() {
    return "home";
  }

  @GetMapping("/api/bet")
  public void doBet(HttpServletResponse response, HttpSession session) {
    response.setHeader("Cache-Control", "no-store");

    System.out.println("BET ENDPOINT STARTED");


    System.out.println("session id : " + session.getId());

//    checkSession(sessionContext);
//    checkBet();
//    checkBalance(sessionContext);
  }

//  @GetMapping("/api/sse")
//  public SseEmitter getSseEmitter(HttpServletResponse response, HttpSession session) {
//    response.setHeader("Cache-Control", "no-store");
//
//    System.out.println("Get sse STARTED");
//
//
//    System.out.println("session id : " + session.getId());
//
//
//    return messagesService.getEmitter(session.getId());
//  }
}
