package com.levi.springai.structuredoutput;

import com.levi.springai.structuredoutput.entity.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestStructuredOutPut {

    private ChatClient chatClient;

    @BeforeEach
    public void init(@Autowired ChatClient.Builder builder) {
        chatClient = builder.defaultAdvisors(SimpleLoggerAdvisor
                .builder().build()).build();
    }

    @Test
    public void test() {
        var parserAddress = chatClient.prompt()
                .system("你是一个地址解析的助手")
                .user("levi，18234556788,北京丰台区，万年花城小区14号楼1002室")
                .call()
                .entity(Address.class);
        System.out.println(parserAddress.toString());
    }
}
