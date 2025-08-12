package com.levi.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/test")
public class TestController {
    private final ChatClient chatClient;

    public TestController(ChatClient.Builder chatClientBuilder, ToolCallbackProvider toolCallbackProvider) {
        this.chatClient = chatClientBuilder
                .defaultToolCallbacks(toolCallbackProvider)
                .defaultAdvisors(
                        new SimpleLoggerAdvisor()
                )
                .build();
    }

    @GetMapping(value = "/baidu-map", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chatWithBotByStream(@RequestParam(value = "message", defaultValue = "讲个笑话") String message) {
        return  this.chatClient.prompt()
                .user(message)
                .stream()
                .content();
    }
}
