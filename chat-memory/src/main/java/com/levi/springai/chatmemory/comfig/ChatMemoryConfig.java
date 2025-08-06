package com.levi.springai.chatmemory.comfig;

import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatMemoryConfig {

    @Bean(name = "jdbcChatMemory")
    public ChatMemory jdbcChatMemory(
            JdbcChatMemoryRepository jdbcChatMemoryRepository
    ) {
        return MessageWindowChatMemory
                .builder()
                .chatMemoryRepository(jdbcChatMemoryRepository)
                .maxMessages(10)
                .build();
    }
}
