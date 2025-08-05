package com.levi.advisor;

import com.levi.advisor.config.ReReadAdvisor;
import com.levi.springai.utils.PromptUtil;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class TestAdvisor {

    @Test
    public void testAdvisor(@Autowired ChatClient.Builder builder) {
        var chatClient = builder.defaultSystem("你是一个新闻小编，请回答客户的问题。")
                // 设置全局的advisor，或者可以在每一次调用设置也可以
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(),
                        new SafeGuardAdvisor(List.of("金正恩","将军","金三胖"),"对不起，我无权讨论太阳",0)
                )
                .build();

        var content = chatClient.prompt().user(PromptUtil.buildNoThinkPrompt("你好，给我写一篇关于金正恩的文章")).call().content();
        System.out.println(content);
    }

    @Test
    public void testReReadAdvisor(@Autowired ChatClient.Builder builder) {
        var chatClient = builder.defaultSystem("你是一个新闻小编，请回答客户的问题。")
                .defaultAdvisors(
                        new ReReadAdvisor(),
                        new SimpleLoggerAdvisor(1)
                )
                .build();

        var content = chatClient.prompt().user(PromptUtil.buildNoThinkPrompt("你好，给我写一篇关于金正恩的文章")).call().content();
        System.out.println(content);
    }
}
