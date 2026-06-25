package lv.venta.model;

import java.util.ArrayList;
import java.util.Collection;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lv.venta.model.enums.Status;

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
	@Max(100000)
	@Column(name = "hourly_rate")
	private double hourlyRate;
	
	@Enumerated(EnumType.STRING)
	@NotNull
	@Column(name = "status")
	private Status status;
	

	@ManyToOne
	@JoinColumn(name = "poid")
	private Position position;
	
	@ManyToOne
	@JoinColumn(name = "did")
	private Department department;
	
	@OneToMany(mappedBy = "employee")
	@ToString.Exclude
	private Collection<Overtime> overtimes = new ArrayList<Overtime>();
	
	@OneToMany(mappedBy = "employee")
	@ToString.Exclude
	private Collection<Vacation> vacations = new ArrayList<Vacation>();
	
	@OneToMany(mappedBy = "employee")
	@ToString.Exclude
	private Collection<Attendance> attendances = new ArrayList<Attendance>();
	
	@OneToOne(mappedBy = "employee")
	@ToString.Exclude
	private User user;
	
	public Employee(String newName, String newSurname, String newPersonCode, String newNumber, String newEmail, double newHourlyRate, Department newDepartment, Status newStatus, Position newPosition) {
		super(newName, newSurname, newPersonCode, newNumber, newEmail);
		setHourlyRate(newHourlyRate);
		setDepartment(newDepartment);
		setStatus(newStatus);
		setPosition(newPosition);
	}
	
}
