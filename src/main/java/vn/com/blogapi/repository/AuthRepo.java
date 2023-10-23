package vn.com.blogapi.repository;

import vn.com.blogapi.model.AuthResponse;
import vn.com.blogapi.model.Users;

public interface AuthRepo {
	AuthResponse SignUp(Users users);

	Users Login(Users users);

	AuthResponse ConfirmEmail(boolean isActive, String email);
}
