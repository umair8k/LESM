package ls.lesm.payload.response;

import lombok.Data;
import ls.lesm.model.Address;
import ls.lesm.model.MasterEmployeeDetails;
@Data
public class EmpDetailsResponse {
	MasterEmployeeDetails masterEmployeeDetails;
	Address address;

}
