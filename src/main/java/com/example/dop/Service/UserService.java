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

	public void deleteUser(Long id) {
		if (userRepository.existsById(id)) {
			userRepository.deleteById(id);
		} else {
			throw new RuntimeException("User not found");
		}
	}

	public void deactivateUser(Long id) {
		User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
		user.setStatus("Inactive");
		userRepository.save(user);
	}

	public User getUserById(Long id) {
		return userRepository.findById(id).orElse(null);
	}

//	public boolean existsByUserName(String userName) {
//		return userRepository.findByUserName(userName).isPresent();
//	}

	public User toggleUserStatus(Long id) {
		Optional<User> userOpt = userRepository.findById(id);
		if (userOpt.isPresent()) {
			User user = userOpt.get();
			user.setStatus(user.getStatus().equals("Active") ? "Inactive" : "Active");
			userRepository.save(user);
		}
		return null;
	}

	public User findById(Long userId) {
		Optional<User> user = userRepository.findById(userId);
		return user.orElse(null); // Return user if found, else null
	}

	public User updateUser(Long userId, User userDetails) {
		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found!"));

		user.setFirstName(userDetails.getFirstName());
		user.setLastName(userDetails.getLastName());
		user.setUserEmailId(userDetails.getUserEmailId());

		if (!userDetails.getUserPassword().isEmpty()) {
			user.setUserPassword(passwordEncoder.encode(userDetails.getUserPassword()));
		}

		return userRepository.save(user);
	}
}