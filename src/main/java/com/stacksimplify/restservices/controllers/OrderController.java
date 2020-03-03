package com.stacksimplify.restservices.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stacksimplify.restservices.entities.Order;
import com.stacksimplify.restservices.entities.User;
import com.stacksimplify.restservices.exceptions.OrderNotFoundException;
import com.stacksimplify.restservices.exceptions.UserNotFoundException;
import com.stacksimplify.restservices.repositories.OrderRepository;
import com.stacksimplify.restservices.repositories.UserRepository;

@RestController
@RequestMapping(value = "/users")
public class OrderController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private OrderRepository orderRepository;

	@GetMapping("/{userid}/orders")
	public List<Order> getAllOrders(@PathVariable Long userid) throws UserNotFoundException {

		Optional<User> optionalUser = userRepository.findById(userid);

		if (!optionalUser.isPresent())
			throw new UserNotFoundException("User not found. Can't get all orders..");

		return optionalUser.get().getOrders();

	}

	// Create order method
	@PostMapping("{userid}/orders")
	public Order createOrder(@PathVariable Long userid, @RequestBody Order order) throws UserNotFoundException {

		Optional<User> optionalUser = userRepository.findById(userid);

		if (!optionalUser.isPresent())
			throw new UserNotFoundException("User not found. Can't get all orders..");

		User user = optionalUser.get();
		order.setUser(user);
		return orderRepository.save(order);
	}

	// Get order by id method
	@GetMapping("/{userid}/orders/{orderid}")
	public Order getOrderById(@PathVariable Long userid, @PathVariable Long orderid)
			throws UserNotFoundException, OrderNotFoundException {

		Optional<User> optionalUser = userRepository.findById(userid);

		if (!optionalUser.isPresent())
			throw new UserNotFoundException("User not found. Can't get order by id..");

		Optional<Order> optionalOrder = orderRepository.findById(orderid);

		if (!optionalOrder.isPresent())
			throw new OrderNotFoundException("Order not found. Can't get order by id..");

		Order order = optionalOrder.get();

		if (order.getUser().getUserid() != userid)
			throw new OrderNotFoundException("Order not found for that user..");

		return order;
	}
}
