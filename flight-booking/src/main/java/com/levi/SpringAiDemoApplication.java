package com.levi;

import com.alibaba.fastjson2.JSON;
import com.github.javafaker.Faker;
import com.levi.constant.PromptConstant;
import com.levi.entity.Booking;
import com.levi.entity.User;
import com.levi.enums.BookingStatus;
import com.levi.factory.InstanceFactory;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@SpringBootApplication
public class SpringAiDemoApplication {

    @Autowired
    private StringRedisTemplate redisTemplate;
    private Random random = new Random();

    public static void main(String[] args) {
        SpringApplication.run(SpringAiDemoApplication.class, args);
    }


    // @PostConstruct
    public void init() {
        Faker faker = InstanceFactory.faker();
        var users = List.of(
          new User("Levi",20,"male"),
          new User("Jessica",18,"female")
        );

        for (User user : users) {
            var userJson = JSON.toJSONString(user);
            redisTemplate.opsForList().leftPush("book:user:",userJson);
        }

        var bookings = List.of(
                new Booking(PromptConstant.ORDER_ID_PREFIX + faker.code().isbn10(), LocalDate.now(), "大同", "北京", BookingStatus.CONFIRMED),
                new Booking(PromptConstant.ORDER_ID_PREFIX + faker.code().isbn10(), LocalDate.now(), "北京", "大同", BookingStatus.CONFIRMED),
                new Booking(PromptConstant.ORDER_ID_PREFIX + faker.code().isbn10(), LocalDate.now(), "青岛", "海南", BookingStatus.CONFIRMED),
                new Booking(PromptConstant.ORDER_ID_PREFIX + faker.code().isbn10(), LocalDate.now(), "东京", "纽约", BookingStatus.CONFIRMED),
                new Booking(PromptConstant.ORDER_ID_PREFIX + faker.code().isbn10(), LocalDate.now(), "北京", "上海", BookingStatus.CONFIRMED)
        );

        for (User user : users) {
            for (Booking booking : bookings) {
                redisTemplate.opsForZSet().add("book:user:booking:" + user.name(),JSON.toJSONString(booking), random.nextLong());
            }
        }
    }

}
