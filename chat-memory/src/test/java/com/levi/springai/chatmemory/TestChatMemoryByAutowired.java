package com.levi.springai.chatmemory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestChatMemoryByAutowired {

    private ChatMemory chatMemory;
    private ChatClient chatClient;
    private final static String MY_CONVERSATION_ID = "levi1";

    @BeforeEach
    public void init(@Autowired ChatClient.Builder builder) {
        chatMemory = MessageWindowChatMemory.builder()
                .maxMessages(6)
                .build();
        chatClient = builder.defaultAdvisors(PromptChatMemoryAdvisor.builder(chatMemory).build()).build();
    }

    /**
     * 实现多个用户的隔离的多轮对话，可以使用advisor来插入本次的conversationId,这里使用的是默认的InMemoryChatMemoryRepository，所以是线程安全的
     * 但是如果是集群部署，就需要使用分布式的ChatMemoryRepository了,比如使用RedisChatMemoryRepository，就可以实现多个用户的隔离的多轮对话
     * 因为每个用户的对话都有一个唯一的conversationId，所以可以根据这个id来隔离不同用户的对话
     */
    @Test
    public void testChatMemoryAdvisor() {
        chatClient.prompt("你好,我是levi，记住我的名字。")
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID,MY_CONVERSATION_ID))
                .call()
                .chatResponse();

        var chatResponse = chatClient.prompt("你好,请问我是谁")
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID,MY_CONVERSATION_ID))
                .call()
                .chatResponse();
        System.out.println(chatResponse.getResult().getOutput());
    }

    /**
     * 也可以在这里配置测试自动装配bean，然后只在这个单元测试里面使用
     * 就可以直接注入了，当然我这里使用的@BeforeEach全局构建的，都可以，一个本质
     * 正式开发配置在config类即可
     */
//    @TestConfiguration
//    static class Config {
//        @Bean
//        public ChatMemory chatMemory() {
//            return MessageWindowChatMemory
//                    .builder()
//                    .maxMessages(6)
//                    .build();
//        }
//    }
}
