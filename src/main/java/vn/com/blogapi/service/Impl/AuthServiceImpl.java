package vn.com.blogapi.service.Impl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import vn.com.blogapi.model.AuthResponse;
import vn.com.blogapi.model.Constant;
import vn.com.blogapi.model.Users;
import vn.com.blogapi.repository.AuthRepo;
import vn.com.blogapi.repository.UserRepo;
import vn.com.blogapi.service.AuthService;
import vn.com.blogapi.util.CheckToken;

import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
	@Value("${app.jwt.secret}")
	private String SECRET_KEY;
	private final AuthRepo authRepo;
	private final CheckToken check_Token;
	private final UserRepo userRepo;
	private final JavaMailSender mailSender;
	private final String SUCCESS_CODE = "00";
	private final TemplateEngine templateEngine;

	public AuthResponse signUp(Users users) {
		try {
			AuthResponse authResponse = authRepo.SignUp(users);
			if (!SUCCESS_CODE.equals(authResponse.getCode())) {
				return authResponse;
			}
			Users dataUser = (Users) authResponse.getData().get("user");
			log.info("User " + dataUser);

			long expirationTimeMillis = System.currentTimeMillis() + 5 * 60 * 1000L;
			Date expirationDate = new Date(expirationTimeMillis);
			String token = Jwts.builder()
					.setSubject(String.valueOf(dataUser.getId()))
					.setExpiration(expirationDate)
					.signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()), SignatureAlgorithm.HS256)
					.compact();
			String resetLink =
					Constant.BaseUrlFE + "/auth/verify-email?email=" + dataUser.getEmail() + "&token=" + token;
			Context context = new Context();
			context.setVariable("name", dataUser.getUsername());
			context.setVariable("url", resetLink);
			String emailContent = templateEngine.process("verify-email", context);
			log.info("template engine   " + emailContent);

			MimeMessage message = mailSender.createMimeMessage();
			log.info("message " + message);

			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setTo(dataUser.getEmail());
			helper.setSubject("Xác minh tài khoản");

			helper.setText(emailContent, true);
			mailSender.send(message);
			log.info("Gửi mail thành công: " + message);
			return AuthResponse.builder().code("00").message("Gửi email thành công").build();
		} catch (MessagingException e) {
			log.error("Failed sending email", e);
			return AuthResponse.builder().code("02").message("Gửi mail không thành công").build();
		} catch (Exception e) {
			log.error("Error while processing DeleteFolder", e);
			return AuthResponse.builder()
					.code("02")
					.message("Lỗi trong quá trình xử lý")
					.build();
		}
	}

	public AuthResponse confirmEmail(String email, String token) {
		log.info("Begin confirm email: " + email);
		String checkToken = check_Token.checkToken(token);
		if (null == checkToken) {
			log.info("Liên kết xác minh của bạn đã hết hạn. Vui lòng nhấp vào gửi lại để xác minh email của bạn");
			return AuthResponse.builder()
					.code("01")
					.message("Liên kết xác minh của bạn đã hết hạn. Vui lòng nhấp vào gửi lại để xác minh email của " +
							"bạn")
					.build();
		} else {
			Users checkUsers = userRepo.findByUserId(Integer.valueOf(checkToken));
			if (null == checkUsers) {
				log.info("Chúng tôi không thể tìm thấy người dùng. Vui lòng đăng ký!");
				return AuthResponse.builder()
						.code("01")
						.message("Chúng tôi không thể tìm thấy người dùng. Vui lòng đăng ký!")
						.build();
			}
			if (checkUsers.isActive()) {
				log.info("Người dùng đã được xác minh. Vui lòng hãy đăng nhập");
				return AuthResponse.builder()
						.code("02")
						.message("Người dùng đã được xác minh. Vui lòng hãy đăng nhập")
						.build();
			}
			boolean isActive = true;
			return authRepo.ConfirmEmail(isActive, email);
		}
	}

	public AuthResponse login(Users users) {
		return null;
	}
}
