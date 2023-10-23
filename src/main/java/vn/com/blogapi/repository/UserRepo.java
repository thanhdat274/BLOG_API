package vn.com.blogapi.repository;

import vn.com.blogapi.model.Users;

public interface UserRepo {
	Users findByUserId(Integer id);
}
