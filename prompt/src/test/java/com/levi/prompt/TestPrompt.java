package com.levi.prompt;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
public class TestPrompt {

    @Test
    public void testPrompt(@Autowired ChatClient.Builder chatClientBuilder) {
        var defaultSystemPrompt = """
                ## 角色说明
                你是一个专业的翻译员AI。
           
                ## 输入说明
                你需要根据角色说明，将用户输入的内容进行翻译。
                
                ## 输出说明
                你需要根据角色说明，将用户输入的内容进行翻译，输出结果需要包含原文和翻译结果。
                输出结果需要使用JSON格式，包含原文和翻译结果两个字段。
                
                ## 示例
                输入：你好
                输出：{"original": "你好", "translation": "Hello"}
                
                ## 注意事项
                1. 请严格按照角色说明进行翻译，不能超出角色说明的范围。
                2. 请严格按照输出说明进行输出，不能超出输出说明的范围。
            """;

        var chatClient = chatClientBuilder.defaultSystem(defaultSystemPrompt).build();
        var content = chatClient.prompt()
                // .system() 可以单独为这次对话设置，上面那个defaultSystem是全局生效的
                .user(buildNoThinkPrompt("翻译一下 give me a break"))
                .call()
                .content();
        System.out.println(content);
    }

    @Test
    public void testPromptSetParam(@Autowired ChatClient.Builder chatClientBuilder) {
        var defaultSystemPrompt = """
                ## 角色说明
                你是一个专业的翻译员AI。
           
                ## 输入说明
                你需要根据角色说明，将用户输入的内容进行翻译。
                
                ## 输出说明
                你需要根据角色说明，将用户输入的内容进行翻译，输出结果需要包含原文和翻译结果。
                输出结果需要使用JSON格式，包含原文和翻译结果两个字段。
                
                ## 注意事项
                1. 请严格按照角色说明进行翻译，不能超出角色说明的范围。
                2. 请严格按照输出说明进行输出，不能超出输出说明的范围。
                
                你所服务的角色信息如下
                姓名:{name},年龄:{age}, 性别:{sex}
            """;

        var chatClient = chatClientBuilder.defaultSystem(defaultSystemPrompt).build();
        var content = chatClient.prompt()
                .system(param -> param.param("name", "levi").param("age", "20").param("sex", "male"))
                .user(buildNoThinkPrompt("你好，你知道我是谁吗"))
                .call()
                .content();
        System.out.println(content);
    }

    private String buildNoThinkPrompt(String prompt) {
        return prompt + "/no_think";
    }
}
