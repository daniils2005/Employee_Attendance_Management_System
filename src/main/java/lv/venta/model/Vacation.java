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
import jakarta.validation.constraints.NotNull;
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
@Table(name = "vacation_table")
public class Vacation {

	@Setter(value = AccessLevel.NONE)
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "vid")
	private long vid;
	
	@NotNull
	@Column(name = "start_date")
	private LocalDate startDate;
	
	@NotNull
	@Column(name = "end_date")
	private LocalDate endDate;
	
	@Column(name = "isActive")
	private boolean isActive;
	
	@ManyToOne
	@JoinColumn(name = "eid")
	private Employee employee;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private RequestStatus status;
	
	public void setActive() {
		if(status == RequestStatus.APSTIPRINATS && !startDate.isAfter(LocalDate.now()) && !endDate.isBefore(LocalDate.now())) {
			isActive = true;
		} else {
			isActive = false;
		}
	}
	
	public Vacation(LocalDate newStartDate, LocalDate newEndDate, Employee newEmployee) {
		setStartDate(newStartDate);
		setEndDate(newEndDate);
		setEmployee(newEmployee);
		this.status = RequestStatus.IZSKATISANA;
		setActive();
	}
}
