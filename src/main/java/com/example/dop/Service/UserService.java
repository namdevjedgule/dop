package com.example.dop.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.dop.Model.User;
import com.example.dop.Repository.UserRepository;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public User saveUser(User user) {
		if (userRepository.existsByUserEmailId(user.getUserEmailId())) {
			throw new RuntimeException("Email already exists");
		}

		user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));

		user.setMemberSince(LocalDateTime.now());
		user.setLastLogin(LocalDateTime.now());
		user.setStatus("Active");

		return userRepository.save(user);
	}

	public boolean checkPassword(String rawPassword, String encodedPassword) {
		return passwordEncoder.matches(rawPassword, encodedPassword);
	}

	public User loginUser(String email, String password) {
		User user = userRepository.loginUser(email);

		if (user == null) {
			throw new RuntimeException("Invalid email or password");
		}

		if (!passwordEncoder.matches(password, user.getUserPassword())) {
			throw new RuntimeException("Invalid email or password");
		}

		return user;
	}

	public long countUsers() {
		return userRepository.count();
	}

	public Optional<User> getUserByEmail(String email) {
		return userRepository.findByUserEmailId(email);
	}

	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

}
