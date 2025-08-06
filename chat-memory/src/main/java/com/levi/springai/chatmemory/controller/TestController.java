package com.levi.springai.chatmemory.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private final static String MY_CONVERSATION_ID = "levi2";

    @Autowired
    private ChatClient.Builder builder;
    @Autowired
    @Qualifier("jdbcChatMemory")
    private ChatMemory jdbcChatMemory;

    @GetMapping("/test")
    public String test() {
        var chatClient = builder.defaultAdvisors(
                PromptChatMemoryAdvisor.builder(jdbcChatMemory).build()
        ).build();

        chatClient
                .prompt("你好,我是levi，记住我的名字。")
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID,MY_CONVERSATION_ID))
                .call()
                .chatResponse();

        var chatResponse = chatClient
                .prompt("你好,请问我是谁")
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID,MY_CONVERSATION_ID))
                .call()
                .chatResponse();

        return chatResponse.getResult().getOutput().getText();
    }
}
