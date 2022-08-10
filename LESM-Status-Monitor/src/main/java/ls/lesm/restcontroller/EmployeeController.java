package ls.lesm.restcontroller;

import java.security.Principal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ls.lesm.exception.RecordNotFoundException;
import ls.lesm.model.Address;
import ls.lesm.model.Designations;
import ls.lesm.model.EmployeeStatus;
import ls.lesm.model.EmployeesAtClientsDetails;
import ls.lesm.model.MasterEmployeeDetails;
import ls.lesm.model.User;
import ls.lesm.payload.request.EmployeeDetailsRequest;
import ls.lesm.payload.request.EmployeeDetailsUpdateRequest;
import ls.lesm.payload.response.ClientEmpDropDownResponse;
import ls.lesm.payload.response.EmpCorrespondingDetailsResponse;
import ls.lesm.payload.response.EmployeeDetailsResponse;
import ls.lesm.payload.response.Response;
import ls.lesm.payload.response.SupervisorDropDown;
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
@RequestMapping("/api/v1/emp")
@CrossOrigin("*")
public class EmployeeController {
	
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
	
	
	@PostMapping("/insert-address")
	public ResponseEntity<?> adressFieldsInsertion(@RequestParam int addTypeId, @RequestBody Address address, Principal principal ){
		this.employeeDetailsService.insertEmpAddress(address, principal, addTypeId);
		return new ResponseEntity<>(HttpStatus.CREATED);
		
	}
	

	@PostMapping("/insert-emp-details")
	public ResponseEntity<?> empDetailsInsertion(@RequestParam(required=false, defaultValue ="0") Integer subVId,
			                                     @RequestParam(required=false) Integer departId,
			                                     @RequestParam(required=false) Integer subDepartId,
			                                     @RequestParam(required=false) Integer desgId,
			                                     @RequestParam Integer addressTypeId,
			                                     @RequestParam(required=false) Integer typeId,
			                                     @RequestBody EmployeeDetailsRequest empReq,			                                   
			                                                  Principal principal ){
		
		
		this.masterEmployeeDetailsRepository.findById(subVId).map(id->{
			empReq.getMasterEmployeeDetails().setSupervisor(id);
			return id;
		});
		
		this.departmentsRepository.findById(departId).map(id->{
			empReq.getMasterEmployeeDetails().setDepartments(id);
			return id;
		});
		
		this.subDepartmentsRepositorye.findById(subDepartId).map(id->{
			empReq.getMasterEmployeeDetails().setSubDepartments(id);
			return id;
		});
		this.designationsRepository.findById(desgId).map(id->{
			empReq.getMasterEmployeeDetails().setDesignations(id);
			return id;
		});
		this.employeeTypeRepository.findById(typeId).map(id->{
			empReq.getMasterEmployeeDetails().setEmployeeType(id);
			return id;
		});		
		
		this.addressTypeRepository.findById(addressTypeId).map(id->{
			empReq.getAddress().setAdressType(id);
			return id;
		});
		
		this.employeeDetailsService.insetEmpDetails(empReq, principal);
		return new ResponseEntity<>(HttpStatus.CREATED);
		
	}
	
	@PostMapping("/inser-empat-client")
	public ResponseEntity<?> insertEmpAtClient(@RequestParam String empId,
                                               @RequestParam int clientId,
                                               @RequestBody  EmployeesAtClientsDetails clientDetails,
                                                             Principal principal){
	
		MasterEmployeeDetails employee=this.masterEmployeeDetailsRepository.findByLancesoft(empId);
		this.masterEmployeeDetailsRepository.findById(employee.getEmpId()).map(id->{
			clientDetails.setMasterEmployeeDetails(id);
			return id;
		});//.orElseThrow(()-> new RelationNotFoundExceptions("Employee with this id '" +empId+"' not exist","415",""));
		this.clientsRepository.findById(clientId).map(cId->{
			clientDetails.setClients(cId);
			return cId;
		});//.orElseThrow(()-> new RelationNotFoundExceptions("this client with this id '"+clientId+"' not exist","416",""));
		this.employeeDetailsService.insertClientsDetails(clientDetails, principal);
		return new ResponseEntity<>(HttpStatus.CREATED);
		
}
	
	@GetMapping("/get-all")
	public ResponseEntity<List<EmployeesAtClientsDetails>> allEmpDetailsAtClient(){
		
		List<EmployeesAtClientsDetails> all=this.employeesAtClientsDetailsRepository.findAll();
		
		return new ResponseEntity<List<EmployeesAtClientsDetails>>(all, HttpStatus.OK);
	}

	@GetMapping("/get-details-byId/{id}")
	public ResponseEntity<EmployeesAtClientsDetails> getDetailsOfEmpAtClientById(@RequestParam int id){
		
		EmployeesAtClientsDetails clientDetails=employeesAtClientsDetailsRepository.findById(id).orElseThrow(()->
		new RecordNotFoundException("Client Details with this id '"+id+"' not exist in database","51"));
		
		Optional<MasterEmployeeDetails> employee=this.masterEmployeeDetailsRepository.findById(clientDetails.getMasterEmployeeDetails().getEmpId());
		if(clientDetails.getPOEdate()==null) {
		clientDetails.setTenure(ChronoUnit.MONTHS.between(clientDetails.getPOSdate(), LocalDate.now()));
		
		employee.get().setStatus(EmployeeStatus.ACTIVE);
		masterEmployeeDetailsRepository.save(employee.get());
		}
		else 
			employee.get().setStatus(EmployeeStatus.BENCH);
		
			clientDetails.setTenure(ChronoUnit.MONTHS.between(clientDetails.getPOSdate(), clientDetails.getPOEdate()));
		
		clientDetails.setTotalEarningAtclient(clientDetails.getClientSalary()*clientDetails.getTenure());
		this.employeesAtClientsDetailsRepository.save(clientDetails);
		return new ResponseEntity<EmployeesAtClientsDetails>(clientDetails,HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/getAll-detail-empAtClient")
	public ResponseEntity<Map<String, Object>> getAllDetailsOfEmpAtClient(
			@RequestParam(value="pageNumber", defaultValue = "0", required = false) Integer pageNumber,
			@RequestParam(value="pageSize", defaultValue="10", required=false)Integer pageSize){
		
		try {
			Page<EmployeesAtClientsDetails> clientDetails=employeeDetailsService.getAllEmpClinetDetails(PageRequest.of(pageNumber, pageSize));
			Map<String, Object> response = new HashMap<>();
			List<EmployeesAtClientsDetails> allEmployee=clientDetails.getContent();
			response.put("User", allEmployee);
			response.put("currentPage", clientDetails.getNumber());
			response.put("totalItems", clientDetails.getTotalElements());
			response.put("totalPages", clientDetails.getTotalPages());
			List<EmployeesAtClientsDetails> deatils=allEmployee;
			List<Integer> bdId=deatils.stream().map(EmployeesAtClientsDetails::getEmpAtClientId).collect(Collectors.toList());
			List<EmployeesAtClientsDetails> clientDetailsAll=employeesAtClientsDetailsRepository.findAllById(bdId);
			  
			//for(int i=0; i<=clientDetailsAll.size(); i++) {
				for(EmployeesAtClientsDetails i: clientDetailsAll) {
					
				Optional<EmployeesAtClientsDetails> currentRecord=employeesAtClientsDetailsRepository.findById(i.getEmpAtClientId());
				Optional<MasterEmployeeDetails> employee=this.masterEmployeeDetailsRepository.findById(currentRecord.get().getMasterEmployeeDetails().getEmpId()); 
				if(currentRecord.get().getPOEdate()==null) {
					currentRecord.get().setTenure(ChronoUnit.MONTHS.between(currentRecord.get().getPOSdate(), LocalDate.now()));
					employee.get().setStatus(EmployeeStatus.ACTIVE);
					this.masterEmployeeDetailsRepository.save(employee.get());
				}
				else {
					employee.get().setStatus(EmployeeStatus.BENCH);
				this.masterEmployeeDetailsRepository.save(employee.get());
				currentRecord.get().setTenure(ChronoUnit.MONTHS.between(currentRecord.get().getPOSdate(), currentRecord.get().getPOEdate()));
				}
				currentRecord.get().setTotalEarningAtclient(currentRecord.get().getClientSalary()*currentRecord.get().getTenure());
				this.employeesAtClientsDetailsRepository.save(currentRecord.get());
			}
			
			return new ResponseEntity<>(response, HttpStatus.OK);
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/getingAll")
	public ResponseEntity<List<Response>> getEmpById(){
		
		 List<Response>all= this.employeesAtClientsDetailsRepository.findDataResponseAll();
		 
		return new ResponseEntity<List<Response>>(all,HttpStatus.OK); 
	
	}
	@GetMapping("/get-all-empDetails")
	public ResponseEntity<List<EmployeeDetailsResponse>> getAllEmp(){
		List<EmployeeDetailsResponse>all=this.masterEmployeeDetailsRepository.getAllEmpDetails();
		return new ResponseEntity<List<EmployeeDetailsResponse>>(all,HttpStatus.OK);
	}
	
	@GetMapping("/address-by-id")
	public ResponseEntity<List<Address>> findEmpAddById(@RequestParam int id){
		List<Address> add=  this.addressRepositoy.findByEmpIdFk(id);
		return new ResponseEntity<List<Address>>(add,HttpStatus.OK);
	}
	
	@GetMapping("/getEmps")
	public ResponseEntity<List<MasterEmployeeDetails>> getEmp( Principal principal){
		
		User loggedU=this.userRepository.findByUsername(principal.getName());
		String id=loggedU.getUsername();
		MasterEmployeeDetails employee=this.masterEmployeeDetailsRepository.findByLancesoft(id);
		int dbPk=employee.getEmpId();
		List<MasterEmployeeDetails> ls = masterEmployeeDetailsRepository.findBymasterEmployeeDetails_Id(dbPk);
		
		return new ResponseEntity<List<MasterEmployeeDetails>>(ls,HttpStatus.OK);
		
	}
	

	@GetMapping("/get-under-emps")
	public ResponseEntity<List<EmployeeDetailsResponse>> getUnderEmps(@RequestParam(value="id") int id){
		
		List<EmployeeDetailsResponse> emps=this.masterEmployeeDetailsRepository.getEmpDetails(id);
		
//		List<MasterEmployeeDetails> ls = masterEmployeeDetailsRepository.findBymasterEmployeeDetails_Id(dbPk);
//		
//		List<Integer> bdId=ls.stream().map(MasterEmployeeDetails::getEmpId).collect(Collectors.toList());
//		System.out.println("=================="+bdId+"==========");
//		List<EmployeeDetailsResponse> empss=null;
//	for(Integer iddd: bdId) {
//		System.out.println("====11  "+iddd);
//		empss=this.masterEmployeeDetailsRepository.getEmpDetails(iddd);
//		System.out.println("---=======---------=="+empss);
//	}
//			
//		System.out.println("----------"+empss);
		return new ResponseEntity<List<EmployeeDetailsResponse>>(emps,HttpStatus.OK);
	}
	@GetMapping("/get-emp-crosspnd-details")
	public ResponseEntity<EmpCorrespondingDetailsResponse> empCorresDetails(@RequestParam int id){
		EmpCorrespondingDetailsResponse e=new EmpCorrespondingDetailsResponse();
		
		EmpCorrespondingDetailsResponse details=this.employeeDetailsService.getEmpCorresDetails(e, id);
		return new ResponseEntity <EmpCorrespondingDetailsResponse>(details,HttpStatus.OK);
	}
	
	@GetMapping("/get-address")
	public ResponseEntity<List<Address>> getAddresses(Principal principal){
		User loggedU=this.userRepository.findByUsername(principal.getName());
		String id=loggedU.getUsername();
		MasterEmployeeDetails employee=this.masterEmployeeDetailsRepository.findByLancesoft(id);
		int dbPk=employee.getEmpId();
		List<MasterEmployeeDetails> ls = masterEmployeeDetailsRepository.findBymasterEmployeeDetails_Id(dbPk);
            for(MasterEmployeeDetails m:ls) {
			List<Address>add=this.addressRepositoy.findByEmpIdFk(m.getEmpId());
		
            }
           
		return new ResponseEntity<List<Address>>(HttpStatus.OK);
	}
	
	@GetMapping("get-emp-clientDetails")
	public ResponseEntity<Map<String, List<EmployeesAtClientsDetails>>> getEmpClientDetails(@RequestParam int id){
		List<EmployeesAtClientsDetails> employee=this.employeesAtClientsDetailsRepository.findsBymasterEmployeeDetails_Id(id);
		Map<String, List<EmployeesAtClientsDetails>> response = new HashMap<>();
		List<EmployeesAtClientsDetails> allEmployee=employee;
		response.put("details",allEmployee);
		//response.put("totalItem",employee.size());
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	
	

//	//@GetMapping("/get-supvisor-dropdown")
//	public ResponseEntity<List<SupervisorDropDown>> getSupDropDowns(@RequestParam int id){
//		
//		Optional<Designations> desg=this.designationsRepository.findById(id);
//
//		Integer cons=null;
//	
//		String desgg=desg.get().getDesgNames();
//		switch(desgg) {
//		case "Consultant":
//		    Designations name=this.designationsRepository.findByDesgNames("Lead");
//			cons=name.getDesgId();
//			break;
//		case "Lead":
//		    Designations name2=this.designationsRepository.findByDesgNames("Manager");
//			cons=name2.getDesgId();
//			break;
//		case "Manager":
//		    Designations name3=this.designationsRepository.findByDesgNames("General Manager");
//			cons=name3.getDesgId();
//			break;
//		case "General Manager":
//		    Designations name4=this.designationsRepository.findByDesgNames("Country Head");
//			cons=name4.getDesgId();
//			break;
//		case "Country Head":
//		    Designations name5=this.designationsRepository.findByDesgNames("MD");
//			cons=name5.getDesgId();
//			break;
//		case "MD":
//     	    List<SupervisorDropDown> name6=this.masterEmployeeDetailsRepository.hrId();
//     	    for(SupervisorDropDown sup: name6) {
//     	    	//cons=sup.getDesgId();
//     	    }
//     	   break;
//	    default:
//		   break;
//		}
//		 List<SupervisorDropDown>dropDown=this.masterEmployeeDetailsRepository.supDropDown(cons);
//			List<SupervisorDropDown> a=this.masterEmployeeDetailsRepository.hrId();
//			if(cons.equals(3))
//				  return new ResponseEntity<List<SupervisorDropDown>>(a,HttpStatus.OK);
//			else
//			  return new ResponseEntity<List<SupervisorDropDown>>(dropDown,HttpStatus.OK);		 	    	
//		 
//	}
	
	
	@GetMapping("/get-supvisor-dropdown")
	public ResponseEntity<List<SupervisorDropDown>> getSupDropDown(@RequestParam int id){
		Optional<Designations> desg=this.designationsRepository.findById(id);
		int desgPkId=desg.get().getDesignations().getDesgId();
		System.out.println("---------------"+desgPkId);
		List<SupervisorDropDown> ab=this.masterEmployeeDetailsRepository.supDropDown(desgPkId);
		return new ResponseEntity<List<SupervisorDropDown>>(ab,HttpStatus.OK);
	}
	
	@GetMapping("/client-emp-dropdown")
	public ResponseEntity<List<ClientEmpDropDownResponse>> clientEmpDropDown( Principal principal){
		User loggedU=this.userRepository.findByUsername(principal.getName());
		String loggedInId=loggedU.getUsername();
		MasterEmployeeDetails employee=this.masterEmployeeDetailsRepository.findByLancesoft(loggedInId);
		
		Integer loggedInUserId=employee.getEmpId();
	    List<ClientEmpDropDownResponse> allLeads=this.masterEmployeeDetailsRepository.clientEmpDropDown(loggedInUserId);
 
		 List<ClientEmpDropDownResponse> iterateLeads=null;
		 ArrayList<ClientEmpDropDownResponse> allConsultants=new  ArrayList<ClientEmpDropDownResponse>();
	   for(ClientEmpDropDownResponse client: allLeads) {
		   iterateLeads= this.masterEmployeeDetailsRepository.clientEmpDropDown(client.getEmpId());
		   allConsultants.addAll(iterateLeads);
	   }

	   return new ResponseEntity<List<ClientEmpDropDownResponse>>(allConsultants,HttpStatus.OK);
		
	}
	
	
	@PutMapping("/update-emp")
	public EmployeeDetailsUpdateRequest updateEmployeeDetails(@RequestParam int id, @RequestBody EmployeeDetailsUpdateRequest empReq){
		
		this.employeeDetailsService.updateEmployee(empReq, id);
		return empReq;
	}
	
	@PutMapping("/update-client-details")
	public ResponseEntity<List<EmployeesAtClientsDetails>> updateClientDetails(@RequestParam int empId, 
			                                                                   @RequestParam int newClientId,
			                                                                   @RequestParam int clientId,
			                                                                   @RequestBody EmployeesAtClientsDetails clientDetails){
		this.employeeDetailsService.updateEmpClientDetails(clientDetails, empId, newClientId, clientId);
		return new ResponseEntity<List<EmployeesAtClientsDetails>>(HttpStatus.ACCEPTED);
	}
	
	
	
	

}