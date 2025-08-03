package com.levi.chatclient.controller;

import com.levi.chatclient.entity.DynamicModelOptionReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RestController
public class ChatController {

    private Map<String, ChatModel> platforms = new ConcurrentHashMap<>();

    public ChatController(DeepSeekChatModel deepSeekChatModel,
                          OllamaChatModel ollamaChatModel) {
        platforms.put("deepseek", deepSeekChatModel);
        platforms.put("ollama", ollamaChatModel);
    }

    /**
     * http://localhost:8080/dynamicModelChat?message=%E4%BD%A0%E6%98%AF%E8%B0%81&platform=ollama&model=gemma3&temperature=1.0
     * @param message
     * @param dynamicModelOptionReq
     * @return
     */
    @GetMapping(value = "/dynamicModelChat",produces = "text/stream;charset=UTF-8")
    public Flux<String> dynamicModelChat(String message,DynamicModelOptionReq dynamicModelOptionReq) {
        String platform = dynamicModelOptionReq.platform();
        String model = dynamicModelOptionReq.model();
        Double temperature = dynamicModelOptionReq.temperature();

        ChatModel chatModel = Objects.requireNonNull(platforms.get(platform),"模型平台异常，请检查参数");
        var chatClient = ChatClient.builder(chatModel).defaultOptions(ChatOptions.builder().model(model).temperature(temperature).build()).build();
        return chatClient.prompt().user(message).stream().content();
    }
}
