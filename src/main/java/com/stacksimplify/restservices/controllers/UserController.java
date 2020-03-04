package com.stacksimplify.restservices.controllers;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import com.stacksimplify.restservices.entities.User;
import com.stacksimplify.restservices.exceptions.UserExistsException;
import com.stacksimplify.restservices.exceptions.UserNameNotFoundException;
import com.stacksimplify.restservices.exceptions.UserNotFoundException;
import com.stacksimplify.restservices.services.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

// Controller
@Api(tags = "User Management RESTful Services", value = "UserController", description = "Controller for User Management Service")
@RestController
@Validated
@RequestMapping(value = "/users") // mapping para todos los mètodos del controlador
public class UserController {

	// Autowire the UserService
	@Autowired
	private UserService userService;

//	@GetMapping("/users") // mapping solo de este controlador
	// @GetMapping // sin valor de url porque ya tiene el @RequestMapping
	// getAllUsers Method
	@ApiOperation(value = "Retrieve list of users")
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public List<User> getAllUsers() {
		return userService.getAllUsers();
	}

	// Create user
	// @RequestBody Annotation
	// @PostMapping Annotation

	/*
	 * @PostMapping("/users") public User createUser(@RequestBody User user) { try {
	 * return userService.createUser(user); } catch (UserExistsException ex) { throw
	 * new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage()); } }
	 */

	// createUser with Location Header
//	@PostMapping("/users") // mapping solo de este controlador
	@PostMapping // sin valor de url porque ya tiene el @RequestMapping
	@ApiOperation(value = "Creates a new user")
	public ResponseEntity<Void> createUser(
			@ApiParam("User information for a new user to be created.") @Valid @RequestBody User user,
			UriComponentsBuilder builder) {
		try {
			userService.createUser(user);
			HttpHeaders headers = new HttpHeaders();
			headers.setLocation(builder.path("/users/{id}").buildAndExpand(user.getUserid()).toUri());
			return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
		} catch (UserExistsException ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
		}
	}

	// Get User by Id
//	@GetMapping("/users/{id}") // mapping solo de este controlador
	@GetMapping("/{id}")
	public User getUserById(@PathVariable("id") @Min(1) Long id) {
		try {
			Optional<User> userOptional = userService.getUserById(id);
			return userOptional.get();
		} catch (UserNotFoundException ex) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
		}
	}

	// Update user by Id
	// @PutMapping("/users/{id}") // mapping solo de este controlador
	@PutMapping("/{id}")
	public User updateUserById(@PathVariable("id") Long id, @RequestBody User user) {
		try {
			return userService.updateUserById(id, user);
		} catch (UserNotFoundException ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
		}
	}

	// Delete user by Id
//	@DeleteMapping("users/{id}") // mapping solo de este controlador
	@DeleteMapping("/{id}")
	public void deleteUserById(@PathVariable("id") Long id) {
		userService.deleteUserById(id);
	}

	// Get User by Username
//	@GetMapping("users/byusername/{username}") // mapping solo de este controlador
	@GetMapping("/byusername/{username}")
	public User getUserByUsername(@PathVariable("username") String username) throws UserNameNotFoundException {
		User user = userService.getUserByUsername(username);

		if (user == null) {
			throw new UserNameNotFoundException("Username: '" + username + "' not found int UserRepository");
		}

		return user;
	}
}
