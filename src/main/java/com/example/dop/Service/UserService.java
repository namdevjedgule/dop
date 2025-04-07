package com.example.dop.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.dop.Model.RoleMaster;
import com.example.dop.Model.User;
import com.example.dop.Repository.RoleMasterRepository;
import com.example.dop.Repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private RoleMasterRepository roleMasterRepository;

	private static final String UPLOAD_DIR = "src/main/resources/static/uploads/user/";

	public User saveUserWithFile(User user, MultipartFile file, String roleName, String createdBy) throws IOException {

		if (file != null && !file.isEmpty()) {
			String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
			String fullPath = UPLOAD_DIR + fileName;

			File uploadDir = new File(UPLOAD_DIR);
			if (!uploadDir.exists()) {
				uploadDir.mkdirs();
			}

			file.transferTo(Paths.get(fullPath));
			user.setProfilePhoto("/uploads/user/" + fileName);
		}

		RoleMaster role = roleMasterRepository.findByRoleName(roleName)
				.orElseThrow(() -> new RuntimeException("Role '" + roleName + "' not found"));
		user.setRole(role);

		if (user.getPassword() == null || user.getPassword().isEmpty()) {
			throw new RuntimeException("Password cannot be empty");
		}
		user.setPassword(passwordEncoder.encode(user.getPassword()));

		user.setActive(1);
		user.setCreatedBy(createdBy);
		user.setCreatedDate(LocalDateTime.now());
		user.setUpdatedBy(null);
		user.setUpdatedDate(null);
		user.setMemberSince(LocalDateTime.now());
		user.setLastLogin(null);
		user.setStatus("Active");

		return userRepository.save(user);
	}

	public boolean checkPassword(String rawPassword, String encodedPassword) {
		return passwordEncoder.matches(rawPassword, encodedPassword);
	}

	public long countAdmins() {
		RoleMaster adminRole = roleMasterRepository.findByRoleName("Admin")
				.orElseThrow(() -> new RuntimeException("Role 'Admin' not found"));
		return userRepository.countByRole(adminRole);
	}

	public long countUsers() {
		RoleMaster userRole = roleMasterRepository.findByRoleName("User")
				.orElseThrow(() -> new RuntimeException("Role 'User' not found"));
		return userRepository.countByRole(userRole);
	}

	public Optional<User> getUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public boolean checkIfEmailExists(String email) {
		return userRepository.existsByEmail(email);
	}

	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	public List<User> getAllAdmins() {
		RoleMaster adminRole = roleMasterRepository.findByRoleName("Admin")
				.orElseThrow(() -> new RuntimeException("Role 'Admin' not found"));
		return userRepository.findByRole(adminRole);
	}

	public List<User> getAllRegularUsers() {
		RoleMaster userRole = roleMasterRepository.findByRoleName("User")
				.orElseThrow(() -> new RuntimeException("Role 'User' not found"));
		return userRepository.findByRole(userRole);
	}

	public void deleteUser(Long id) {
		if (userRepository.existsById(id)) {
			userRepository.deleteById(id);
		} else {
			throw new RuntimeException("User not found");
		}
	}

	public void deactivateUser(Long id) {
		User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
		user.setStatus("Inactive");
		userRepository.save(user);
	}

	public User getUserById(Long id) {
		return userRepository.findById(id).orElse(null);
	}

	public List<User> getUsersByRoleId(Long roleId) {
		return userRepository.findByRoleRoleId(roleId);
	}

	public User toggleUserStatus(Long id) {
		Optional<User> userOpt = userRepository.findById(id);
		if (userOpt.isPresent()) {
			User user = userOpt.get();
			user.setStatus(user.getStatus().equals("Active") ? "Inactive" : "Active");
			return userRepository.save(user);
		}
		return null;
	}

	public User updateUserWithFile(Long id, User updatedUser, MultipartFile imageFile, String updatedBy) {
		User existingUser = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

		existingUser.setFirstName(updatedUser.getFirstName());
		existingUser.setLastName(updatedUser.getLastName());
		existingUser.setEmail(updatedUser.getEmail());

		if (updatedUser.getPassword() != null && !updatedUser.getPassword().isBlank()) {
			existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
		}

		if (imageFile != null && !imageFile.isEmpty()) {
			String imageName = saveImage(imageFile);
			existingUser.setProfilePhoto(imageName);
		}

		existingUser.setUpdatedBy(updatedBy);
		existingUser.setUpdatedDate(LocalDateTime.now());

		return userRepository.save(existingUser);
	}

	private String saveImage(MultipartFile imageFile) {
		try {
			File uploadDir = new File(UPLOAD_DIR);
			if (!uploadDir.exists()) {
				uploadDir.mkdirs();
			}

			String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
			String fullPath = UPLOAD_DIR + fileName;

			imageFile.transferTo(Paths.get(fullPath));

			return "/uploads/user/" + fileName;
		} catch (IOException e) {
			throw new RuntimeException("Failed to save image: " + e.getMessage());
		}
	}

}
