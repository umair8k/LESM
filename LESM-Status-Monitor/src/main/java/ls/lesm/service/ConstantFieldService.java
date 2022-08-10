package ls.lesm.service;

import java.security.Principal;
import java.util.List;

import ls.lesm.model.AddressType;
import ls.lesm.model.Clients;
import ls.lesm.model.Departments;
import ls.lesm.model.Designations;
import ls.lesm.model.EmployeeType;
import ls.lesm.model.OnsiteExpensesType;
import ls.lesm.model.SubDepartments;

public interface ConstantFieldService {
	
	Designations insertDesg(Designations desig ,Principal Principal,Integer id);
	
	Departments insertDepart(Departments depart, Principal Principal );
	
	SubDepartments insertSubDepart(SubDepartments subDepart, Principal principal, int departId);
	
	Clients insertClient(Clients clients ,Principal principal);
	
	EmployeeType insertEmpTypes(EmployeeType emoType);
	
	OnsiteExpensesType insertExpType(OnsiteExpensesType expType, Principal principal);
	
	AddressType insertAddressTyp(AddressType addType);
	
	List<SubDepartments>getAllSubDepartments();
	
	List<Departments> getAllDepartments();
	
	List<AddressType> getAllAddType();
	
   
    
	

}
