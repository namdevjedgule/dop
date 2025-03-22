package com.example.dop.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.dop.Model.User;
import com.example.dop.Repository.UserRepository;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;

	private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	public User saveUser(User user) {
		user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
		return userRepository.save(user);
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
}
