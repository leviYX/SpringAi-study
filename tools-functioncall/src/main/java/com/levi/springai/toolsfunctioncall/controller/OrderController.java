package com.levi.springai.toolsfunctioncall.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.definition.ToolDefinition;
import org.springframework.ai.tool.method.MethodToolCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    @Autowired
    @Qualifier("orderBotAssistantClient")
    private ChatClient chatClient;

    @GetMapping("/createOrder")
    private String createOrder(@RequestParam("message") String message) {
        ToolDefinition toolDefinition = ToolDefinition.builder().name("test").description("测试一下").inputSchema("json内容").build();
        MethodToolCallback methodToolCallback = MethodToolCallback.builder().toolDefinition(toolDefinition).build();
        chatClient.prompt().user(message).call().content();
        return chatClient.prompt().user(message).call().content();
    }
}
