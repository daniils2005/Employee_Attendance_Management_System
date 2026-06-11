package lv.venta.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
@Table(name = "attendance_table")
public class Attendance {
	
	@Setter(value = AccessLevel.NONE)
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "aid")
	private long aid;

	@NotNull
	@Column(name = "work_date")
	private LocalDate workDate;
	
	@Min(0)
	@Max(24)
	@Column(name = "hours_worked")
	private float hoursWorked;
	
	@ManyToOne
	@JoinColumn(name = "eid")
	private Employee employee;
	
	//TODO ielikt datumu kura diena strada
	
	public Attendance(float newHoursWorked, Employee newEmployee) {
		this.workDate = LocalDate.now();
		setHoursWorked(newHoursWorked);
		setEmployee(newEmployee);
	}
	
}
