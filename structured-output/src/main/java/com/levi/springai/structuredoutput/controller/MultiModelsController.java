package com.levi.springai.structuredoutput.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.levi.springai.structuredoutput.entity.Job;
import com.levi.springai.utils.PromptUtil;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.Objects;

@RestController
public class MultiModelsController {

    @Autowired
    private ChatClient planningChatClient;
    @Autowired
    private ChatClient botChatClient;

    @GetMapping(value = "/stream", produces = "text/stream;charset=UTF8")
    public Flux<String> stream(@RequestParam String message) {
        // 创建一个用于接收多条消息的 Sink
        Sinks.Many<String> sink = Sinks.many().unicast().onBackpressureBuffer();
        // 推送消息
        sink.tryEmitNext("正在计划任务...<br/>");

        var aiThread = Thread.ofVirtual()
                .name("ai-thread")
                .unstarted(() -> {
                    // 发起llm调用
                    ChatClient.CallResponseSpec callResponseSpec = planningChatClient.prompt().user(PromptUtil.buildNoThinkPrompt(message)).call();
                    String trim = callResponseSpec.content().replaceAll("(?s)<think>.*?</think>", "").trim();
                    Job job = null;
                    try {
                        job = new ObjectMapper().readValue(trim, new TypeReference<Job>() {
                        });
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println(
                            "job = " + job.toString()
                    );

                    //Job job = planningChatClient.prompt().user(PromptUtil.buildNoThinkPrompt(message)).call().entity(Job.class);
                    // 判断结构化的任务类型
                    switch (Objects.requireNonNull(job).jobType()) {
                        case CANCEL -> {
                            System.out.println(job);
                            if (job.keyInfos().isEmpty()) {
                                sink.tryEmitNext("请输入姓名和订单号.");
                            } else {
                                sink.tryEmitNext("退票成功!");
                            }
                        }
                        case QUERY -> {
                            System.out.println(job);
                            sink.tryEmitNext("查询预定信息：xxxx");
                        }
                        case OTHER -> {
                            Flux<String> content = botChatClient.prompt().user(message).stream().content();
                            content.doOnNext(sink::tryEmitNext) // 推送每条AI流内容
                                    .doOnComplete(sink::tryEmitComplete)
                                    .subscribe();
                        }
                        default -> {
                            System.out.println(job);
                            sink.tryEmitNext("解析失败");
                        }
                    }
                });
        aiThread.start();
        return sink.asFlux();
    }
}