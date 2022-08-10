package ls.lesm.model;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class Salary {
	
	@Id
	@GeneratedValue(generator = "sal_gen",strategy = GenerationType.AUTO)
	private Integer salId;
	//private Boolean isLatest;// 0 old salary; 1 new salary
	private Double salary;
	
	@JsonIgnore
	private LocalDate createdAt;
	
	@JsonIgnore
	@Column(length=30)
	private String createdBy;//principal
	
	@JsonIgnore
	@OneToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@JoinColumn(name="emp_id_fk")
    private MasterEmployeeDetails masterEmployeeDetails;
	

}
