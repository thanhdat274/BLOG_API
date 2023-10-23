package vn.com.blogapi.service;

import vn.com.blogapi.model.AuthResponse;
import vn.com.blogapi.model.Users;

public interface AuthService {
	AuthResponse signUp(Users users);
	AuthResponse login(Users users);
	AuthResponse confirmEmail(String email, String token);
}
