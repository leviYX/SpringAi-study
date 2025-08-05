package com.levi.springai.utils;

public class PromptUtil {

    /**
     * 关闭ollama的思考模式：我们用的是qwen模型，他是一个思考模型，所以每次问答都会带上冗长的思考链。有时候我们还不需要这个能力。需要关掉它。
     * 1、软关闭：ollama在0.9之前没有关闭思考模式这个设置。但是可以在prompt之后加一个/no_think就可以让模型不输出思考链路。
     *           但是这样做的话，虽然没有了输出，但是<think></think>的标签还在，不过可以让前端或者后端去掉。
     *
     * 2、硬关闭: 0.9之后可以在客户端执行set nothink来关闭，但是这个指令是session有效的，你关了这个客户端的，其余的还是一样。
     *           所以如果要全局关闭，需要在每个客户端都执行set nothink。并且springai目前最新的是1.0.0.而这个关闭能力是在它之后
     *           出的。所以springai还没有集成这个选项，可能下一个版本的OllamaOptions就可以设置了。
     */
    public static String buildNoThinkPrompt(String prompt) {
        return prompt + "/no_think";
    }
}
