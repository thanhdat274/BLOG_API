package vn.com.blogapi.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import vn.com.blogapi.util.ObjectUtil;

import java.util.Map;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthResponse {
	private String code;
	private String message;
	private Map<String, Object> data;

	@Override
	public String toString() {
		return ObjectUtil.convertToString(this);
	}
}
