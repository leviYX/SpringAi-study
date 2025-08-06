package com.levi.springai.chatmemory;

import com.levi.springai.utils.PromptUtil;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

// org.springframework.ai.chat.memory.ChatMemory  MessageWindowChatMemory
@SpringBootTest
public class TestChatMemory {

    private static final String conversationId = "1234567";

    @Test
    public void testNoChatMemory(@Autowired ChatClient.Builder builder) {
        var chatClient = builder.build();
        var message1 = new UserMessage.Builder().text(PromptUtil.buildNoThinkPrompt("你好，记住我是levi")).build();
        var chatResponse1 = chatClient
                .prompt(new Prompt(message1))
                .call().chatResponse();

        var message2 = new UserMessage.Builder().text(PromptUtil.buildNoThinkPrompt("你好，请问我是谁")).build();
        var chatResponse2 = chatClient
                .prompt(new Prompt(message2))
                .call().chatResponse();

        System.out.println(chatResponse2.getResult().getOutput());
    }

    @Test
    public void testChatMemory(@Autowired ChatClient.Builder builder) {
        var chatClient = builder.build();
        var chatMemory = MessageWindowChatMemory.builder().build();

        var message1 = new UserMessage.Builder().text(PromptUtil.buildNoThinkPrompt("你好，记住我是levi")).build();
        chatMemory.add(conversationId,message1);
        var chatResponse1 = chatClient
                .prompt(new Prompt(chatMemory.get(conversationId)))
                .call().chatResponse();
        chatMemory.add(conversationId,chatResponse1.getResult().getOutput());

        var message2 = new UserMessage.Builder().text(PromptUtil.buildNoThinkPrompt("你好，请问我是谁")).build();
        chatMemory.add(conversationId,message2);
        var chatResponse2 = chatClient
                .prompt(new Prompt(chatMemory.get(conversationId)))
                .call().chatResponse();

        System.out.println(chatResponse2.getResult().getOutput());
    }

    /**
     * 通过PromptChatMemoryAdvisor来维护chatMemory
     * @param builder
     * @param chatMemory 引入spring-ai-autoconfigure-model-chat-memory可以自动注入了
     *                   spring-ai-autoconfigure-model-chat-memory里面装配了一个ChatMemory的bean
     *                   自动注入之后就不用你MessageWindowChatMemory.builder().build();构建了
     *                   或者你自己MessageWindowChatMemory构建也可以
     */
    @Test
    public void testChatMemoryAdvisor(@Autowired ChatClient.Builder builder,
            @Autowired ChatMemory chatMemory) {
        var chatClient = builder.defaultAdvisors(
                PromptChatMemoryAdvisor.builder(chatMemory)
                        .conversationId(conversationId)
                        .build(), // 提供ChatMemoryAdvisor，通过哪一个chatMemory和conversationId来处理chatMemory
                new SimpleLoggerAdvisor()
        ).build();

        chatClient.prompt()
                .user(PromptUtil.buildNoThinkPrompt("你好，记住我是levi"))
                .call()
                .chatResponse();

        ChatResponse chatResponse = chatClient.prompt()
                .user(PromptUtil.buildNoThinkPrompt("你好，请问我是谁"))
                .call()
                .chatResponse();

        System.out.println(chatResponse.getResult().getOutput());
    }
}
