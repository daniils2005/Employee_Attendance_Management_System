package lv.venta.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Table(name = "user_table")
@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class User {

	@Setter(value = AccessLevel.NONE)
	@Column(name= "uid")
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long uid;
	
	@NotNull
	@NotEmpty
	@Column(name = "username")
	@Pattern(regexp = "^.{8,}$")
	private String username;
	
	@NotNull
	@NotEmpty
	@Column(name = "password")
	private String password;
	
	//private Role role;
	
	
	//TODO eid 
	
	
	public User(String newUsername, String newPassword, Role newRole) {
		setUsername(newUsername);
		setPassword(newPassword);
		
	}
}
