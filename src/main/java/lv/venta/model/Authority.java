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
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name = "authority_table")
@Entity
public class Authority {
	
	@Setter(value = AccessLevel.NONE)
	@Column(name = "authority_id")
	@Id 
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long authority_id;
	
	@Column(name = "title")
	@NotNull
	@NotEmpty
	@Pattern(regexp = "^[A-Z_]{4,10}$")
	private String title;
	
	@OneToMany(mappedBy =  "authority")
	@ToString.Exclude
	private Collection<User> users = new ArrayList<>();
	
	public Authority(String title) {
		setTitle(title);
	}
	
}

