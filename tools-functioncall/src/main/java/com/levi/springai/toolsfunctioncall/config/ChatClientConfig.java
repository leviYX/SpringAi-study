package com.levi.springai.toolsfunctioncall.config;

import com.levi.springai.toolsfunctioncall.service.OrderService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig {

    /**
     * 订单智能助手
     *
     * @param chatClientBuilder
     * @return
     */
    @Bean
    public ChatClient orderBotAssistantClient(ChatClient.Builder chatClientBuilder) {
        return chatClientBuilder
                .defaultSystem("你是一个订单系统的智能助手，你可以创建订单，取消订单，查询订单等")
                .defaultUser("用户")
                .defaultTools(new OrderService())
                .build();
    }
}
