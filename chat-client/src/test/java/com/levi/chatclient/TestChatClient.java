package com.levi.chatclient;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;

@SpringBootTest
public class TestChatClient {

    @Test
    public void testChatClient(@Autowired ChatClient.Builder builder) {
        var chatClient = builder.build();
        ChatClient.CallResponseSpec callResponseSpec = chatClient.prompt().user("你好，请问你是谁?").call();
        System.out.println(callResponseSpec.content());
    }

    @Test
    public void testChatClientStream(@Autowired ChatClient.Builder builder) {
        var chatClient = builder.build();
        Flux<String> chatClientResponseFlux = chatClient.prompt().user("你好，请问你是谁?").stream().content();
        chatClientResponseFlux.toIterable().forEach(System.out::println);
    }
}
