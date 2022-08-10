package ls.lesm.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Sub_Profit {
	
	@Id
    @GeneratedValue(generator = "sub_profit_gen",strategy = GenerationType.AUTO)
    private Integer sp_id;

    private Double subprofit;

    @ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    @JoinColumn(name="emp_id_fk")
    private MasterEmployeeDetails masterEmployeeDetails;

    public Sub_Profit(Double subprofit, MasterEmployeeDetails masterEmployeeDetails) {
        super();
        this.subprofit = subprofit;
        this.masterEmployeeDetails = masterEmployeeDetails;
    }

}
