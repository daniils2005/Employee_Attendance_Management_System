package lv.venta.model;

import java.util.Collection;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Table(name = "employee_table")
@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Employee extends Person{
	
	
	@Setter(value = AccessLevel.NONE)
	@Column(name= "eid")
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long eid;
	
	@Min(0)
	@Max(1000)
	@Column(name = "hourly_rate")
	private double hourlyRate;
	
	@Column(name = "department")
	
	
	//one to many
	//on department many employee
	private Department department;
	
	
	
	
	public Employee(String newName, String newSurname, String newPersonCode, String newNumber, String newEmail, double newHourlyRate, Department newDeoartment) {
		super(newName, newSurname, newPersonCode, newNumber, newEmail);
		setHourlyRate(newHourlyRate);
		setDepartment(newDeoartment);
	}
	
}
