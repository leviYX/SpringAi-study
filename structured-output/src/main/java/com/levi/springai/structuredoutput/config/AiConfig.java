package com.levi.springai.structuredoutput.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    /**
     * 出行规划
     *
     * @param builder
     * @param chatMemory
     * @return
     */
    @Bean
    public ChatClient strategyChatClient(ChatClient.Builder builder,
                                         ChatMemory chatMemory) {
        var chatOptions = ChatOptions.builder().temperature(1.0).build();
        return builder
                .defaultOptions(chatOptions)
                .defaultSystem("""
                            你是一名出行规划小助手，请根据用户的信息来计划出行。
                            # 出行小助手计划规则
                            ## 1.要求
                            ### 1.1 根据用户内容识别出行内容信息，给出计划
                            ## 2. 出行计划内容
                            ### 2.1 walkType为CANCEL的时候 要求用户提供姓名和订单号，如果用户没有明确给出，则从对话中提取相关信息；
                            ### 2.2 walkType为QUERY的时候 要求用户提供订单号，如果用户没有明确给出，则从对话中提取相关信息；
                            ### 2.3 walkType为OTHER的时候 用户信息为001号用户levi的出行计划为:这么近这么美，周末去河北
                            """)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }

    /**
     * 智能客服，用来聊天
     * @param builder
     * @param chatMemory
     * @return
     */
    @Bean
    public ChatClient botChatClient(ChatClient.Builder builder,
                                    ChatMemory chatMemory) {
        var chatOptions = ChatOptions.builder().temperature(1.7).build();
        return  builder
                .defaultOptions(chatOptions)
                .defaultSystem("""
                           你是橘子旅行社的代理智能客服， 请以友好的语气服务用户。每次返回用户都带上小橘子为您服务的话语。
                            """)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }

    /**
     * 计划规划客户端
     * @param builder
     * @param chatMemory
     * @return
     */
    @Bean
    public ChatClient planningChatClient(ChatClient.Builder builder,
                                         ChatMemory chatMemory) {
        var chatOptions = ChatOptions.builder().temperature(0.7).build();
        return builder
                .defaultOptions(chatOptions)
                .defaultSystem("""
                            你是一名任务分类器。
                            **只能返回纯 JSON，不要任何解释、不要 markdown 代码块，不要返回<think></think>思考链标签、不要多余空格**。
                            示例：
                            {"jobType":"OTHER","keyInfos":{}}
                            # 票务助手任务拆分规则
                            ## 1.要求
                            ### 1.1 根据用户内容识别任务
                            
                            ## 2. 任务
                            ### 2.1 JobType:退票(CANCEL) 要求用户提供姓名和预定号， 或者从对话中提取；
                            ### 2.2 JobType:查票(QUERY) 要求用户提供预定号， 或者从对话中提取；
                            ### 2.3 JobType:其他(OTHER)
                            """)
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(chatMemory).build()
                )
                .build();
    }

    /**
     * 智能客服，用来聊天
     * @param builder
     * @param chatMemory
     * @return
     */
//    @Bean
//    public ChatClient botChatClient(ChatClient.Builder builder,
//                                    ChatMemory chatMemory) {
//        var chatOptions = ChatOptions.builder().temperature(1.7).build();
//        return  builder
//                .defaultOptions(chatOptions)
//                .defaultSystem("""
//                           你是XS航空智能客服代理， 请以友好的语气服务用户。
//                            """)
//                .defaultAdvisors(
//                        MessageChatMemoryAdvisor.builder(chatMemory).build()
//                )
//                .build();
//    }

}