package com.levi.springai.structuredoutput.controller;

import com.levi.springai.structuredoutput.entity.Plan;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.Objects;

@RestController
public class OrderController {

    @Autowired
    private ChatClient strategyChatClient;
    @Autowired
    private ChatClient botChatClient;

    @GetMapping(value = "/order", produces = "text/stream;charset=UTF8")
    public Flux<String> stream(@RequestParam String message) {
        // 创建一个用于接收多条消息的 Sink
        Sinks.Many<String> sink = Sinks.many().unicast().onBackpressureBuffer();
        // 推送消息
        sink.tryEmitNext("橘子智能客服正在为您计划任务...<br/>");
        Thread.ofVirtual()
                .name("ai-thread")
                .unstarted(() -> {
                    // 发起llm调用，转为结构化输出
                    var plan = strategyChatClient.prompt().user(message).call().entity(Plan.class);
                    // 判断结构化的任务类型
                    switch (Objects.requireNonNull(plan).walkType()) {
                        case CANCEL -> {
                            if (plan.infos().isEmpty()) {
                                sink.tryEmitNext("请输入姓名和订单号.");
                            } else {
                                sink.tryEmitNext("您的订单号为：" + plan.infos().get("orderNo") + "，是否确认退票？");
                            }
                        }
                        case QUERY -> sink.tryEmitNext("查询出行信息：结果为xxx");
                        case OTHER -> {
                            Flux<String> content = botChatClient.prompt().user(message).stream().content();
                            content.doOnNext(sink::tryEmitNext).doOnComplete(sink::tryEmitComplete).subscribe();
                        }
                        default -> sink.tryEmitNext("解析失败");
                    }
                }).start();
        return sink.asFlux();
    }
}
