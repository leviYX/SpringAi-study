package com.levi.quickstart;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.deepseek.DeepSeekAssistantMessage;
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
        var prompt = new Prompt(List.of(userMessage));
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

    /**
     * Option的所有选项都可以在yml的模型配置下配置，全局生效。
     * 我们这里用代码灵活设置，代码设置的会覆盖yml的配置。
     */
    @Test
    public void testOption(@Autowired DeepSeekChatModel deepSeekChatModel) {
        var userMessage = new UserMessage("帮我写一首描述大海的七律诗");

        var chatOptions = DeepSeekChatOptions.builder()
                .temperature(2d) // 0-2,越大温度越高，越热情，创造力越高，发挥的越随意，每次的都不会太一样
                .maxTokens(5000)// 限制llm生成的最大token数，会把超出的截断，你可以用来测试，测试的时候避免大量token影响，看你自己的要求了
                .stop(List.of("易烊千玺","刘德华"))// 会把限定词的后面的直接截断
                .build();
        var prompt = new Prompt(List.of(userMessage),chatOptions);
        var res = deepSeekChatModel.call(prompt).getResult().getOutput().getText();
        System.out.println("res = " + res);
    }

    /**
     * 获取思考链的内容，需要把deepseek的模型配置为deepseek-reasoner
     */
    @Test
    public void testReason(@Autowired DeepSeekChatModel deepSeekChatModel) {
        var userMessage = new UserMessage("帮我写一首描述大海的七律诗");

        var chatOptions = DeepSeekChatOptions.builder()
                .temperature(2d)
                .maxTokens(500)
                .stop(List.of("cold","hot"))
                .build();
        var prompt = new Prompt(List.of(userMessage),chatOptions);
        var chatResponse = deepSeekChatModel.call(prompt);
        var deepSeekAssistantMessage = (DeepSeekAssistantMessage) chatResponse.getResult().getOutput();

        System.out.println("输出的思考链为:" + deepSeekAssistantMessage.getReasoningContent());
        System.out.println("输出的内容为:" + deepSeekAssistantMessage.getText());

    }

    @Test
    public void testReasonStream(@Autowired DeepSeekChatModel deepSeekChatModel) {
        var userMessage = new UserMessage("帮我写一首描述大海的七律诗");

        var chatOptions = DeepSeekChatOptions.builder()
                .temperature(2d)
                .maxTokens(500)
                .stop(List.of("cold","hot"))
                .build();
        var prompt = new Prompt(List.of(userMessage),chatOptions);
        Flux<ChatResponse> chatResponseFlux = deepSeekChatModel.stream(prompt);
        chatResponseFlux.toIterable().forEach(chatResponse -> {
            var deepSeekAssistantMessage = (DeepSeekAssistantMessage) chatResponse.getResult().getOutput();
            System.out.println("输出的思考链为:" + deepSeekAssistantMessage.getReasoningContent());
            System.out.println("输出的内容为:" + deepSeekAssistantMessage.getText());
        });
    }
}
