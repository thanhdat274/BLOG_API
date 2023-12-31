package vn.com.blogapi.repository.Impl;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import vn.com.blogapi.model.SqlConstant;
import vn.com.blogapi.model.Users;
import vn.com.blogapi.repository.UserRepo;
import vn.com.blogapi.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserRepoImpl implements UserRepo {
	private final HikariDataSource hikariDataSource;

	public Users findByUserId(Integer id) {
		MDC.put("tracking", NanoIdUtils.randomNanoId());
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = hikariDataSource.getConnection();
			preparedStatement = connection.prepareStatement(SqlConstant.GET_USER_BY_ID_SQL);
			preparedStatement.setInt(1, id);
			resultSet = preparedStatement.executeQuery();
			Users user = new Users();
			if (resultSet.next()) {
				user.setId(resultSet.getInt("id"));
				user.setUsername(resultSet.getString("username"));
				user.setEmail(resultSet.getString("email"));
				user.setRole(resultSet.getInt("role"));
				user.setActive(resultSet.getBoolean("isActive"));
				user.setCreatedAt(resultSet.getTimestamp("created_at"));
				user.setUpdatedAt(resultSet.getTimestamp("updated_at"));
				return user;
			} else {
				// resultSet rỗng, không tìm thấy thư mục
				log.info("Không tìm thấy người dùng này với id: " + id);
				return null;
			}
		} catch (Exception e) {
			log.error("Error while connecting to the database: ", e);
			return null;
		} finally {
			// Đảm bảo kết nối và tài nguyên được đóng đúng cách
			DBUtil.cleanUp(connection, null, preparedStatement, resultSet);
		}
	}
}
