package ls.lesm.restcontroller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ls.lesm.model.Designations;
import ls.lesm.model.MasterEmployeeDetails;
import ls.lesm.payload.response.EmployeeUnderHRDropDownResponse;
import ls.lesm.repository.AddressRepositoy;
import ls.lesm.repository.AddressTypeRepository;
import ls.lesm.repository.ClientsRepository;
import ls.lesm.repository.DepartmentsRepository;
import ls.lesm.repository.DesignationsRepository;
import ls.lesm.repository.EmployeeTypeRepository;
import ls.lesm.repository.EmployeesAtClientsDetailsRepository;
import ls.lesm.repository.InternalExpensesRepository;
import ls.lesm.repository.MasterEmployeeDetailsRepository;
import ls.lesm.repository.SubDepartmentsRepository;
import ls.lesm.repository.UserRepository;
import ls.lesm.service.impl.EmployeeDetailsServiceImpl;

@RestController
@RequestMapping("/api/v1/hr")
@CrossOrigin("*")
public class HRController {
	
	@Autowired
	private EmployeeDetailsServiceImpl employeeDetailsService;
	
	
	@Autowired
	private MasterEmployeeDetailsRepository masterEmployeeDetailsRepository;
	@Autowired
	private DepartmentsRepository departmentsRepository;
	@Autowired
	private SubDepartmentsRepository subDepartmentsRepositorye;
	@Autowired
	private DesignationsRepository designationsRepository;
	@Autowired
	private EmployeeTypeRepository employeeTypeRepository;
	
	@Autowired
	private EmployeesAtClientsDetailsRepository employeesAtClientsDetailsRepository;

	@Autowired
	private ClientsRepository clientsRepository;
	
	@Autowired
	private AddressRepositoy addressRepositoy;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AddressTypeRepository addressTypeRepository;
	
	@Autowired
	private InternalExpensesRepository internalExpensesRepository;
	
	
	
	
	@PatchMapping("/update-desg-hierarchy")
	public ResponseEntity<?> updateDesgHierar(@RequestParam int desgId, @RequestParam int newSupId){
		Optional<Designations> desg=this.designationsRepository.findById(desgId);
		this.designationsRepository.findById(newSupId).map(id->{
			desg.get().setDesignations(id);
			return this.designationsRepository.save(id);
		});
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	

	@GetMapping("/Hr-dropDown")
	public List<EmployeeUnderHRDropDownResponse> getDropDown(Principal principal){
		String loggedInUsername=principal.getName();
		MasterEmployeeDetails loggedInEmp= this.masterEmployeeDetailsRepository.findByLancesoft(loggedInUsername);
		
		List<EmployeeUnderHRDropDownResponse> dropDown=this.masterEmployeeDetailsRepository.getEmpUnderHrDropDown(loggedInEmp.getEmpId());
	
		return dropDown;
	}
	
	@GetMapping("/update-supervisor-id/{empId}/{newSupId}")//this is update operation
	public ResponseEntity<?> updateSupervisiorId(@PathVariable String empId, @PathVariable int newSupId){
		
		MasterEmployeeDetails employee=this.masterEmployeeDetailsRepository.findByLancesoft(empId);
		this.masterEmployeeDetailsRepository.findById(newSupId).map(id->{
			employee.setSupervisor(id);
			return this.masterEmployeeDetailsRepository.save(id);
		});
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
