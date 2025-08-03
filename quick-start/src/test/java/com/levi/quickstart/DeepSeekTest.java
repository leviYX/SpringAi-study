package com.levi.quickstart;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.ai.deepseek.DeepSeekChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;

import java.util.List;

@SpringBootTest
public class DeepSeekTest {

    /**
     * org.springframework.ai.model.deepseek.autoconfigure.DeepSeekChatAutoConfiguration
     */
    @Test
    public void testDeepSeek(@Autowired DeepSeekChatModel deepSeekChatModel) {
        var userMessage = new UserMessage("帮我写一首描述大海的七律诗");

        var chatOptions = DeepSeekChatOptions.builder()
                .temperature(2d) // 0-2,越大温度越高，越热情，创造力越高，发挥的越随意，每次的都不会太一样
                .build();
        var prompt = new Prompt(List.of(userMessage),chatOptions);
        var res = deepSeekChatModel.call(prompt).getResult().getOutput().getText();
        System.out.println("res = " + res);
    }

    @Test
    public void testDeepSeekStream(@Autowired DeepSeekChatModel deepSeekChatModel) {
        var userMessage = new UserMessage("你好，请问你是谁?");
        var prompt = new Prompt(List.of(userMessage));
        Flux<ChatResponse> chatResponseFlux = deepSeekChatModel.stream(prompt);
        chatResponseFlux.toIterable().forEach(System.out::println);
    }
}
