package lv.venta.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lv.venta.model.enums.DepartmentName;

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
	
	@Enumerated(EnumType.STRING)
	@NotNull
	@Column(name = "department_name")
	private DepartmentName department;
	
	@NotNull
	@Pattern(regexp = "[\\s\\S]{0,100}")
	private String description;
}
