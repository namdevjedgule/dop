package com.example.dop.Controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.dop.DTO.LoginRequest;
import com.example.dop.Model.User;
import com.example.dop.Repository.UserRepository;
import com.example.dop.Service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {

	@Autowired
	private UserService userService;
	@Autowired
	private UserRepository userRepository;

	@GetMapping("/")
	public String showLoginPage() {
		return "login";
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
		try {
			User user = userService.loginUser(loginRequest.getEmail(), loginRequest.getPassword());
			return ResponseEntity.ok(user);
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
		}
	}

	@PostMapping("/checkdata")
	public String checkLogin(@RequestParam("email") String email, @RequestParam("password") String password,
			HttpSession session, RedirectAttributes redirectAttributes) {

		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

		Integer loginAttempts = (Integer) session.getAttribute("loginAttempts");
		if (loginAttempts == null)
			loginAttempts = 0;

		if (loginAttempts >= 5) {
			redirectAttributes.addFlashAttribute("error", "Too many failed login attempts. Try again later.");
			return "redirect:/";
		}

		Optional<User> userOptional = userRepository.findByUserEmailId(email);

		if (userOptional.isPresent()) {
			User user = userOptional.get();

			if (passwordEncoder.matches(password, user.getUserPassword())) {
				session.setAttribute("loggedInUser", user);
				session.setAttribute("loginAttempts", 0);
				redirectAttributes.addFlashAttribute("success", "Login successful!");
				return "redirect:/dashboard";
			}
		}

		session.setAttribute("loginAttempts", loginAttempts + 1);
		redirectAttributes.addFlashAttribute("error", "Invalid email or password.");
		return "redirect:/";
	}

	@GetMapping("/logout")
	public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
		session.invalidate();
		redirectAttributes.addFlashAttribute("success", "You have been logged out.");
		return "redirect:/";
	}

}
