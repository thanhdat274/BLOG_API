package vn.com.blogapi.model;

public class SqlConstant {
	public static final String GET_EMAIL_SQL = "SELECT COUNT(*) AS count FROM users WHERE email = ?";
	public static final String INSERT_USER_SQL = "{CALL add_user(?, ?, ?)}";
	public static final String GET_USER_BY_EMAIL_SQL = "SELECT * FROM users WHERE email = ?";
	public static final String GET_USER_BY_ID_SQL = "SELECT * FROM users WHERE id = ?";
	public static final String UPDATE_IS_ACTIVE_USER_SQL = "{CALL UpdateIsActiveUser(?, ?)}";
}
