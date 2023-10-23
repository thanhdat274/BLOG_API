package vn.com.blogapi.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Data;
import vn.com.blogapi.util.ObjectUtil;

import java.util.Date;

@Data
@Entity
@Table(name = "users")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Users {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String username;
	private String password;
	private String email;
	private Integer role = 0;
	private boolean isActive = false;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at", nullable = false, updatable = false)
	private Date createdAt;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_at", nullable = false)
	private Date updatedAt;

	@Override
	public String toString() {
		return ObjectUtil.convertToString(this);
	}


}
