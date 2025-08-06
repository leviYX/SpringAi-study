//package com.levi.springai.chatmemory.comfig;
//
//import org.springframework.ai.chat.client.ChatClient;
//import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
//import org.springframework.ai.chat.memory.ChatMemory;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class ChatClientConfig {
//
//    @Bean
//    public ChatClient chatClient(ChatClient.Builder builder, ChatMemory jdbcChatMemory) {
//        return builder.defaultAdvisors(
//                PromptChatMemoryAdvisor.builder(jdbcChatMemory).build()
//        ).build();
//    }
//}
