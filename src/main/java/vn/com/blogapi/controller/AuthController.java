package vn.com.blogapi.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;
import vn.com.blogapi.model.AuthResponse;
import vn.com.blogapi.model.Users;
import vn.com.blogapi.service.AuthService;

@Slf4j
@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;

	@ExceptionHandler  // đây là hàm xử lý khi trong postman ko có dữ liệu vẫn bấm gửi
	public ResponseEntity<AuthResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
		log.info("Request data not found: ", ex);
		AuthResponse responseData = AuthResponse.builder().code("99")
				.message("Data not found!")
				.build();
		return ResponseEntity.badRequest().body(responseData);
	}

	@PostMapping("/signup")
	public ResponseEntity<AuthResponse> signup(@RequestBody Users users) {
		AuthResponse responseData = authService.signUp(users);
		return new ResponseEntity<>(responseData, HttpStatus.OK);
	}

	@PostMapping("/login")
	public ResponseEntity<AuthResponse> login(@RequestBody Users users) {
		AuthResponse responseData = authService.login(users);
		return new ResponseEntity<>(responseData, HttpStatus.OK);
	}

	@GetMapping("/confirmation/{email}/{token}")
	public ResponseEntity<AuthResponse> confirmation(@PathVariable("email") String email,
	                                                 @PathVariable("token") String token) {
		AuthResponse responseData = authService.confirmEmail(email, token);
		return new ResponseEntity<>(responseData, HttpStatus.OK);
	}
}
