package com.levi.advisor.config;

import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.AdvisorChain;
import org.springframework.ai.chat.client.advisor.api.BaseAdvisor;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;

import java.util.Map;

public class ReReadAdvisor implements BaseAdvisor {

    private static final String RE_READ = """
            请你重新阅读一下问题，问题是：{re_read_input}
            """;

    @Override
    public ChatClientRequest before(ChatClientRequest chatClientRequest, AdvisorChain advisorChain) {
        String contents = chatClientRequest.prompt().getContents();
        String reReadInput = PromptTemplate.builder().template(RE_READ).build()
                .render(Map.of("re_read_input", contents));
        // mutate表示复制出来一个chatClientRequest，属性不变，我们设置新的即可
        return chatClientRequest.mutate().prompt(new Prompt(reReadInput)).build();
    }

    @Override
    public ChatClientResponse after(ChatClientResponse chatClientResponse, AdvisorChain advisorChain) {
        return null;
    }

    // 越小优先级越高
    @Override
    public int getOrder() {
        return 0;
    }
}
