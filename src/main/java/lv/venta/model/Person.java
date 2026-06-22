package lv.venta.model;


import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Person {
	
	@NotNull
	@NotEmpty
	@Pattern(regexp = "^[A-Z][a-zA-Z]{1,29}$")
	@Column(name = "name")
	private String name;
	
	@NotNull
	@NotEmpty
	@Pattern(regexp = "^[A-Z][a-zA-Z]{1,29}$")
	@Column(name = "surname")
	private String surname;
	
	@NotNull
	@NotEmpty
	@Pattern(regexp = "^[0-9]{6}-[0-9]{5}$")
	@Column(name = "person_code")
	private String personCode;
	
	@NotNull
	@NotEmpty
	@Pattern(regexp = "^(\\+371|371)?[1-9]\\d{7}$")
	@Column(name = "number")
	private String number;
	
	@NotNull
	@NotEmpty
	@Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
	@Column(name = "email")
	private String email;
	
	public Person(String newName, String newSurname, String newPersonCode, String newNumber, String newEmail) {
		setName(newName);
		setSurname(newSurname);
		setPersonCode(newPersonCode);
		setNumber(newNumber);
		setEmail(newEmail);
	}
	
}
