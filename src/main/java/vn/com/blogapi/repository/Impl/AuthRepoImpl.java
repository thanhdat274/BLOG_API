package vn.com.blogapi.repository.Impl;

import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vn.com.blogapi.model.AuthResponse;
import vn.com.blogapi.model.SqlConstant;
import vn.com.blogapi.model.Users;
import vn.com.blogapi.repository.AuthRepo;
import vn.com.blogapi.util.DBUtil;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthRepoImpl implements AuthRepo {
	private final HikariDataSource hikariDataSource;

	public AuthResponse SignUp(Users users) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		CallableStatement callableStatement = null;
		ResultSet resultSet = null;
		try {
			connection = hikariDataSource.getConnection();
			preparedStatement = connection.prepareCall(SqlConstant.GET_EMAIL_SQL);
			preparedStatement.setString(1, users.getEmail());
			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				int count = resultSet.getInt("count");
				log.info("User count: " + count);
				if (count > 0) {
					log.error("Email already exists: {}", users.getEmail());
					return AuthResponse.builder()
							.code("01")
							.message("Email already exists")
							.build();
				}
			}

			callableStatement = connection.prepareCall(SqlConstant.INSERT_USER_SQL);
			callableStatement.setString("P_EMAIL", users.getEmail());
			callableStatement.setString("P_PASSWORD", users.getPassword());
			callableStatement.setString("P_USERNAME", users.getUsername());
			callableStatement.executeUpdate();

			PreparedStatement getUserStatement = connection.prepareStatement(SqlConstant.GET_USER_BY_EMAIL_SQL);
			getUserStatement.setString(1, users.getEmail());
			ResultSet userResultSet = getUserStatement.executeQuery();
			Users data = new Users();
			if (userResultSet.next()) {
				data.setId(userResultSet.getInt("id"));
				data.setUsername(userResultSet.getString("username"));
				data.setEmail(userResultSet.getString("email"));
				data.setActive(userResultSet.getBoolean("isActive"));
			}
			log.info("New user data: {}", data);
			return AuthResponse.builder()
					.code("00")
					.message("Sign-up successful")
					.data(new HashMap<>() {{
						put("user", data);
					}})
					.build();
		} catch (Exception e) {
			log.error("Error while connecting to the database: ", e);
			return AuthResponse.builder()
					.code("02")
					.message("Sign-up failed")
					.build();
		} finally {
			DBUtil.cleanUp(connection, callableStatement, preparedStatement, resultSet);
		}
	}

	public AuthResponse ConfirmEmail(boolean isActive, String email) {
		Connection connection = null;
		CallableStatement callableStatement = null;
		try {
			connection = hikariDataSource.getConnection();
			callableStatement = connection.prepareCall(SqlConstant.UPDATE_IS_ACTIVE_USER_SQL);
			callableStatement.setString("P_EMAIL", email);
			callableStatement.setBoolean("P_ISACTIVE", isActive);
			callableStatement.executeUpdate();

			log.info("Tài khoản của bạn đã được xác minh thành công!");
			return AuthResponse.builder()
					.code("00")
					.message("Tài khoản của bạn đã được xác minh thành công!")
					.build();
		} catch (Exception e) {
			log.error("Error while connecting to the database: ", e);
			return AuthResponse.builder()
					.code("02")
					.message("Tài khoản của bạn xác minh không thành công!")
					.build();
		} finally {
			DBUtil.cleanUp(connection, callableStatement, null, null);
		}
	}

	public Users Login(Users users) {
		return null;
	}


}
