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

import com.example.dop.DTO.SignupRequest;
import com.example.dop.Model.Company;
import com.example.dop.Model.RoleMaster;
import com.example.dop.Model.User;
import com.example.dop.Repository.AboutUsRepository;
import com.example.dop.Repository.CompanyRepo;
import com.example.dop.Repository.CountryRepository;
import com.example.dop.Repository.DesignationRepository;
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

	@Autowired
	private DesignationRepository designationRepo;

	@Autowired
	private CountryRepository countryRepo;

	@Autowired
	private CompanyRepo companyRepo;

	@Autowired
	private AboutUsRepository aboutUsRepo;

	private static final String UPLOAD_DIR = "src/main/resources/static/uploads/user/";

//	public User registerUser(SignupRequest request) {
//		if (userRepository.existsByEmail(request.getEmail())) {
//			throw new RuntimeException("Email already registered");
//		}
//
//		String companyName = request.getCompanyName().trim();
//		Optional<Company> existingCompany = companyRepo.findByCompanyNameIgnoreCase(companyName);
//
//		if (existingCompany.isPresent()) {
//			throw new RuntimeException("Company already exists");
//		}
//
//		Company newCompany = new Company();
//		newCompany.setCompanyName(companyName);
//		newCompany = companyRepo.save(newCompany);
//
//		User user = new User();
//		user.setFirstName(request.getFirstName());
//		user.setLastName(request.getLastName());
//		user.setEmail(request.getEmail());
//		user.setPassword(passwordEncoder.encode(request.getPassword()));
//		user.setPhoneNumber(request.getPhoneNumber());
//		user.setCompany(newCompany);
//		user.setActive(true);
//		user.setCreatedDate(LocalDateTime.now());
//		user.setMemberSince(LocalDateTime.now());
//		user.setStatus("Active");
//
//		user.setRole(roleMasterRepository.findById(2L)
//				.orElseThrow(() -> new RuntimeException("User role (ID 2) not found")));
//
//		user.setDesignation(designationRepo.findById(request.getDesignationId()).orElse(null));
//		user.setCountry(countryRepo.findById(request.getCountryId()).orElse(null));
//		user.setAboutUs(aboutUsRepo.findById(request.getAboutUsId()).orElse(null));
//
//		return userRepository.save(user);
//	}

	public User registerUser(SignupRequest request) {
		if (userRepository.existsByEmail(request.getEmail())) {
			throw new RuntimeException("Email already registered");
		}

		String companyName = request.getCompanyName().trim();
		Optional<Company> existingCompany = companyRepo.findByCompanyNameIgnoreCase(companyName);

		if (existingCompany.isPresent()) {
			throw new RuntimeException("Company already exists");
		}

		Company newCompany = new Company();
		newCompany.setCompanyName(companyName);
		newCompany.setCreatedBy(request.getEmail());
		newCompany.setCreatedOn(LocalDateTime.now());
		newCompany.setStatus("Active");
		newCompany = companyRepo.save(newCompany);

		User user = new User();
		user.setFirstName(request.getFirstName());
		user.setLastName(request.getLastName());
		user.setEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setPhoneNumber(request.getPhoneNumber());
		user.setCompany(newCompany);
		user.setActive(true);
		user.setCreatedDate(LocalDateTime.now());
		user.setMemberSince(LocalDateTime.now());
		user.setStatus("Active");

		user.setRole(roleMasterRepository.findById(2L)
				.orElseThrow(() -> new RuntimeException("User role (ID 2) not found")));

		user.setDesignation(designationRepo.findById(request.getDesignationId()).orElse(null));
		user.setCountry(countryRepo.findById(request.getCountryId()).orElse(null));
		user.setAboutUs(aboutUsRepo.findById(request.getAboutUsId()).orElse(null));

		return userRepository.save(user);
	}

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

		user.setActive(true);
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
		RoleMaster superAdminRole = roleMasterRepository.findByRoleName("SuperAdmin")
				.orElseThrow(() -> new RuntimeException("Role 'SuperAdmin' not found"));

		long adminCount = userRepository.countByRole(adminRole);
		long superAdminCount = userRepository.countByRole(superAdminRole);

		return adminCount + superAdminCount;
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

	public List<User> getAdminsAndSuperAdmins() {
		return userRepository.findAllAdminsAndSuperAdmins();
	}

	public List<User> getAllRegularUsers() {
		RoleMaster userRole = roleMasterRepository.findByRoleName("User")
				.orElseThrow(() -> new RuntimeException("Role 'User' not found"));
		return userRepository.findByRole(userRole);
	}

	public void deleteUser(Long id) {
		Optional<User> userOptional = userRepository.findById(id);
		if (userOptional.isPresent()) {
			User user = userOptional.get();

			if (user.getRole().getRoleId() == 3L) {
				throw new RuntimeException("Super Admin cannot be deleted.");
			}

			userRepository.deleteById(id);
		} else {
			throw new RuntimeException("User not found with ID: " + id);
		}
	}

	public long countByRole(String roleName) {
		Optional<RoleMaster> roleOptional = roleMasterRepository.findByRoleName(roleName);
		return roleOptional.map(role -> userRepository.countByRole(role)).orElse(0L);
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

			if (user.getRole().getRoleId() == 3L) {
				throw new RuntimeException("Super Admin cannot be deactivated.");
			}

			if ("Active".equalsIgnoreCase(user.getStatus())) {
				user.setStatus("Inactive");
				user.setActive(false);
			} else {
				user.setStatus("Active");
				user.setActive(true);
			}

			user.setUpdatedDate(LocalDateTime.now());
			return userRepository.save(user);
		}
		return null;
	}

	public User updateUserWithFile(Long id, User updatedUser, MultipartFile imageFile, String updatedBy) {
		User existingUser = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

		if (existingUser.getRole().getRoleId() == 3L) {
			throw new RuntimeException("Super Admin cannot be edited.");
		}

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

	public boolean isEmailTaken(String email, Long userId) {
		Optional<User> existingUser = userRepository.findByEmailIgnoreCase(email);

		if (existingUser.isPresent()) {
			return userId == null || !existingUser.get().getId().equals(userId);
		}

		return false;
	}

	public List<User> getUsersCreatedByAdmin(String createdBy) {
		return userRepository.findByCreatedBy(createdBy);
	}

	public User updateProfile(User existingUser, String firstName, String lastName, String email, String status,
			MultipartFile imageFile) {
		existingUser.setFirstName(firstName);
		existingUser.setLastName(lastName);
		existingUser.setEmail(email);
		existingUser.setStatus(status);

		if (imageFile != null && !imageFile.isEmpty()) {
			String imageUrl = saveImage(imageFile);
			existingUser.setProfilePhoto(imageUrl);
		}

		return userRepository.save(existingUser);
	}

}
