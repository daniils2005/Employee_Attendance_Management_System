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
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Table(name = "Position_table")
@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Position {
	
	@Setter(value = AccessLevel.NONE)
	@Column(name= "pid")
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long pid;
	
	@NotNull
	@NotEmpty
	@Column(name = "name")
	private String name;
	
	@Column(name = "description")
	private String description;
	
	@OneToMany(mappedBy = "position")
	@ToString.Exclude
	private Collection<Employee> employees = new ArrayList<Employee>();
	
	public Position(String newName, String newDescription) {
		setName(newName);
		setDescription(newDescription);
	}
}
