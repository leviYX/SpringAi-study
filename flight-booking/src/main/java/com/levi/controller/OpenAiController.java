package com.levi.controller;

import com.levi.constant.PromptConstant;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

@RestController("/ai/v1")
@CrossOrigin
public class OpenAiController  {

    private final ChatClient chatClient;

    public OpenAiController(ChatClient.Builder chatClientBuilder, ChatMemory chatMemory) {
        this.chatClient = chatClientBuilder
                .defaultSystem(PromptConstant.BOOKING_BOT_SYSTEM_PROMPT)
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(),
                        PromptChatMemoryAdvisor.builder(chatMemory).build()
                )
                .build();
    }

    @CrossOrigin
    @GetMapping(value = "/chatWithBotByStream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chatWithBotByStream(@RequestParam(value = "message", defaultValue = "讲个笑话") String message) {
        Flux<String> content = this.chatClient.prompt()
                .system(promptSystemSpec -> promptSystemSpec.param("current_date", LocalDate.now().toString()))
                .user(message)
                .stream()
                .content();
        return content.concatWith(Flux.just("[complete]"));
    }

}
