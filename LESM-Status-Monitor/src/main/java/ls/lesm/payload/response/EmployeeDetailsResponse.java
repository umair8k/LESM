package ls.lesm.payload.response;

import java.time.LocalDate;

public interface EmployeeDetailsResponse {

	Integer getEmpId();
	String getEmployeeId();
	String getFirstName();
	String getLastName();
	LocalDate getDob();
	String getLocation();
	String getEmail();
	String getGender();
	String getDesignation();
	String getEmployeeType();
	String getSubDepartName();
	LocalDate getJoiningDate();
	String getVertical();
}
