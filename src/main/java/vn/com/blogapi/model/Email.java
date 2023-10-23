package vn.com.blogapi.model;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import vn.com.blogapi.util.ObjectUtil;

@Data
@Configuration
public class Email {
	@org.springframework.beans.factory.annotation.Value("${spring.mail.host}")
	private String host;

	@org.springframework.beans.factory.annotation.Value("${spring.mail.port}")
	private int port;

	@org.springframework.beans.factory.annotation.Value("${spring.mail.username}")
	private String username;

	@org.springframework.beans.factory.annotation.Value("${spring.mail.password}")
	private String password;

	@org.springframework.beans.factory.annotation.Value("${spring.mail.properties.mail.smtp.auth}")
	private boolean smtpAuth;

	@Value("${spring.mail.properties.mail.smtp.starttls.enable}")
	private boolean starttlsEnable;

	@Override
	public String toString() {
		return ObjectUtil.convertToString(this);
	}
}
