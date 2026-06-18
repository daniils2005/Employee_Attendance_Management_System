package lv.venta.model;

import java.util.ArrayList;
import java.util.Collection;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "department_table")
public class Department {
	
	@Setter(value = AccessLevel.NONE)
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "did")
	private long did;
	
	@NotNull
	@NotEmpty
	@Column(name = "department_name")
	private String departmentName;
	
	@Size(max = 100)
	private String description;
	
	@OneToMany(mappedBy = "department")
	@ToString.Exclude
	private Collection<Employee> employees = new ArrayList<Employee>();
	
	public Department(String newDepartmentName, String newDescription) {
		setDepartmentName(newDepartmentName);
		setDescription(newDescription);
	}
}
