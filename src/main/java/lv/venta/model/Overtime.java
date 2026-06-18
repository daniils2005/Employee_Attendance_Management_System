package lv.venta.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lv.venta.model.enums.RequestStatus;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "overtime_table")
public class Overtime {
	
	@Setter(value = AccessLevel.NONE)
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "oid")
	private long oid;
	
	@NotNull
	@Column(name = "date")
	private LocalDate date;
	
	@Min(0)
	@Column(name = "overtime_hours")
	private float overtimeHours;
	
	@Min(1)
	@Column(name = "overtime_rate")
	private Float overtimeRate;
	
	@Size(max = 100)
	private String description;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private RequestStatus status;
	
	@ManyToOne
	@JoinColumn(name = "eid")
	private Employee employee;
	
	public Overtime(float newOvertimeHours, String newDescription, Employee newEmployee) {
		this.date = LocalDate.now();
		setOvertimeHours(newOvertimeHours);
		setDescription(newDescription);
		setEmployee(newEmployee);
		this.overtimeRate = (float) 1;
		this.status = RequestStatus.IZSKATISANA;
	}
}
