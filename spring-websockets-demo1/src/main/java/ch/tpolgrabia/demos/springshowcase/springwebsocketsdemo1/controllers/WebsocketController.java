package ch.tpolgrabia.demos.springshowcase.springwebsocketsdemo1.controllers;

import ch.tpolgrabia.demos.springshowcase.springwebsocketsdemo1.models.Greeting;
import ch.tpolgrabia.demos.springshowcase.springwebsocketsdemo1.models.HelloMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebsocketController {
    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message) {
        return new Greeting(String.format("Hello %s!!!", message.getName()));
    }
}
