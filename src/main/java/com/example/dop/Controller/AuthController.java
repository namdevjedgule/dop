package com.example.dop.Controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.dop.DTO.SignupRequest;
import com.example.dop.Model.User;
import com.example.dop.Repository.UserRepository;
import com.example.dop.Service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserService userService;

	@GetMapping("/")
	public String showLoginPage() {
		return "login";
	}

	@PostMapping("/checkdata")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> checkLogin(@RequestBody Map<String, String> loginData,
			HttpSession session) {
		String email = loginData.get("email");
		String password = loginData.get("password");

		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		Map<String, Object> response = new HashMap<>();

		Integer loginAttempts = (Integer) session.getAttribute("loginAttempts");
		if (loginAttempts == null) {
			loginAttempts = 0;
		}

		if (loginAttempts >= 5) {
			response.put("success", false);
			response.put("message", "Too many failed login attempts. Try again later.");
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
		}

		Optional<User> userOptional = userRepository.findByEmail(email);

		if (userOptional.isPresent()) {
			User user = userOptional.get();

			if (passwordEncoder.matches(password, user.getPassword())) {

				user.setLastLogin(LocalDateTime.now());
				userRepository.save(user);

				session.setAttribute("userId", user.getId());
				session.setAttribute("user", user);
				session.setAttribute("loginAttempts", 0);

				String role = user.getRole().getRoleName();
				session.setAttribute("userRole", role);

				response.put("success", true);
				response.put("message", role + " login successful!");
				response.put("role", role);
				return ResponseEntity.ok(response);
			}
		}

		session.setAttribute("loginAttempts", loginAttempts + 1);
		response.put("success", false);
		response.put("message", "Invalid email or password.");
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	}

	@PostMapping(value = "/signup", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<User> signup(@ModelAttribute SignupRequest request) {
		User registeredUser = userService.registerUser(request);
		return ResponseEntity.ok(registeredUser);
	}

	@GetMapping("/logout")
	public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
		session.invalidate();
		redirectAttributes.addFlashAttribute("success", "You have been logged out.");
		return "redirect:/";
	}

}
