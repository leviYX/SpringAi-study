package com.levi.springai.toolsfunctioncall.service;

import com.levi.springai.toolsfunctioncall.entity.Order;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OrderService {

    private final static Map<String, Order> orderMap = new ConcurrentHashMap<>();

    public boolean createOrder(Order order) {
        var orderId = Objects.requireNonNull(order.orderId(),"订单id不能为空");
        System.out.println("创建订单:" + order);
        return orderMap.putIfAbsent(orderId, order) == null;
    }

    public boolean cancelOrder(String orderId) {
        Objects.requireNonNull(orderId,"订单id不能为空");
        System.out.println("取消订单:" + orderId);
        Order removeOrder = orderMap.remove(orderId);
        if(Objects.nonNull(removeOrder)) {
            System.out.println("取消订单成功:" + removeOrder);
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }


    @Tool(description = "创建订单")
    private boolean createOrderTool(@ToolParam(description = "订单id") String orderId,
                                @ToolParam(description = "用户名") String username) {
        Order order = new Order(orderId, username);
        return createOrder(order);
    }

    @Tool(description = "取消订单")
    private boolean cancelOrderTool(@ToolParam(description = "订单id") String orderId) {
        return cancelOrder(orderId);
    }
}
