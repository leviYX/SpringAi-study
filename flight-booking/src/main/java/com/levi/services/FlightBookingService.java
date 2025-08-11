package com.levi.services;
import com.alibaba.fastjson2.JSONObject;
import com.levi.entity.Booking;
import com.levi.entity.BookingDetail;
import com.levi.entity.User;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class FlightBookingService {

	@Autowired
	private StringRedisTemplate redisClient;

	@Tool(description = "取消航班")
	private boolean cancelBookingTool(
			@ToolParam(description = "航班号") String bookingNumber,
			@ToolParam(description = "用户名") String userName) {
		return cancelBooking(bookingNumber, userName);
	}

	// 获取所有已预订航班
	public List<BookingDetail> getBookings() {
		List<User> users = new ArrayList<>();
		List<BookingDetail> bookings = new ArrayList<>();

		List<String> usersStrs = redisClient.opsForList().range("book:user:", 0, -1);
		for (String userStr : usersStrs) {
			users.add(JSONObject.parseObject(userStr,User.class));
		}

		for (User user : users) {
			String key = "book:user:booking:" + user.name();
			Set<String> bookingsStr = redisClient.opsForZSet().range(key, 0, -1);
			for (String booking : bookingsStr) {
				bookings.add(new BookingDetail(JSONObject.parseObject(booking,Booking.class),user.name()));
			}
		}
		return bookings;
	}

	// 取消预定航班
	public boolean cancelBooking(String bookingNumber, String userName) {
		String key = "book:user:booking:" + userName;
		Set<String> bookingsStr = redisClient.opsForZSet().range(key, 0, -1);
		if (!bookingsStr.contains(bookingNumber)) return false;
		redisClient.opsForZSet().remove(key,bookingNumber);
		return true;

	}

}
