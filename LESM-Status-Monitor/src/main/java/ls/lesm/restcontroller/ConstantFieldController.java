package ls.lesm.restcontroller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ls.lesm.exception.DuplicateEntryException;
import ls.lesm.model.AddressType;
import ls.lesm.model.Clients;
import ls.lesm.model.Departments;
import ls.lesm.model.Designations;
import ls.lesm.model.EmployeeType;
import ls.lesm.model.OnsiteExpensesType;
import ls.lesm.model.SubDepartments;
import ls.lesm.repository.AddressRepositoy;
import ls.lesm.repository.ClientsRepository;
import ls.lesm.repository.DepartmentsRepository;
import ls.lesm.repository.DesignationsRepository;
import ls.lesm.repository.EmployeeTypeRepository;
import ls.lesm.repository.OnsiteExpensesTypeRepository;
import ls.lesm.repository.SubDepartmentsRepository;
import ls.lesm.service.ConstantFieldService;

@RestController
@RequestMapping("/api/v1/fields")
@CrossOrigin("*")
public class ConstantFieldController {
	
	@Autowired
	private ConstantFieldService contantConstantFieldService;
	
	@Autowired
	private ClientsRepository clientsRepository;
	
	@Autowired
	private DesignationsRepository designationsRepository;
	
	@Autowired
	private EmployeeTypeRepository employeeTypeRepository;
	
	@Autowired
	private OnsiteExpensesTypeRepository onsiteExpensesTypeRepository;
	
	@Autowired
	private AddressRepositoy addressRepositoy;
	
	@Autowired
	private DepartmentsRepository departmentsRepository;
	
	@Autowired
	private SubDepartmentsRepository subDepartmentsRepository;
	

	
	@PostMapping("/insert-desig")
	public ResponseEntity<?> desigFieldInsertions(@RequestBody Designations desig,
			                                      @RequestParam(required=false, defaultValue ="0") Integer id,
			                                      Principal principal){	
		 
		 Designations desg=this.designationsRepository.findByDesgNames(desig.getDesgNames());
		 System.out.println("================"+desg);
		 if(desg==null) {
		 this.contantConstantFieldService.insertDesg(desig, principal,id);
	     return new ResponseEntity<>(desig,HttpStatus.CREATED);
		 }
		 else {
			 return new ResponseEntity<>(new DuplicateEntryException("This designation with this name "+desig.getDesgNames()+" alreday exist in database","") ,HttpStatus.INTERNAL_SERVER_ERROR);
		 }
	}
	
	@PostMapping("/insert-depart")
	public ResponseEntity<?> departFieldInsertions(@RequestBody Departments depart, Principal principal){
		this.contantConstantFieldService.insertDepart(depart, principal);
		Departments local=this.departmentsRepository.findByDepart(depart.getDepart());
		if(local!=null)
		return new ResponseEntity<>(new DuplicateEntryException("This Department Already exist in database","400"),HttpStatus.INTERNAL_SERVER_ERROR);
		else {
			this.departmentsRepository.save(depart);
		return new ResponseEntity<>(HttpStatus.CREATED);
		}
	}
	
	@PostMapping("/insert-sub-depart/{departId}")
	public ResponseEntity<?> subDepartFieldInsertions(@RequestBody SubDepartments subDepart, Principal principal,@PathVariable int departId){
		this.contantConstantFieldService.insertSubDepart(subDepart, principal,departId);
		 this.departmentsRepository.findById(departId).map(depart->{
			    subDepart.setDepartments(depart);
			    return depart;
		 });
		SubDepartments local=this.subDepartmentsRepository.findBySubDepartmentNames(subDepart.getSubDepartmentNames());
		if(local!=null)
			return new ResponseEntity<>(new DuplicateEntryException("This Sub-Department Already exist in database","103"),HttpStatus.INTERNAL_SERVER_ERROR);
		else {
			 subDepartmentsRepository.save(subDepart);
			 return new ResponseEntity<>(HttpStatus.CREATED);
		}
		
	}
	
	@PostMapping("/insert-clients")
	public ResponseEntity<?> clientsFieldInsertions(@RequestBody Clients clients, Principal principal){
		this.contantConstantFieldService.insertClient(clients, principal);
		Clients local=this.clientsRepository.findByClientsNames(clients.getClientsNames());
		if(local!=null) {
			//throw new DuplicateEntryException("200","This Client Already exist in database");
			return new ResponseEntity<>(new DuplicateEntryException("This Client Already exist in database","201") ,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		else {
			this.clientsRepository.save(clients);
			return new ResponseEntity<>(HttpStatus.ACCEPTED);
		}
		
	}
	
	@PostMapping("/insert-employee-type")
	public ResponseEntity<?> empTypesFieldInsertions(@RequestBody EmployeeType type){
		this.contantConstantFieldService.insertEmpTypes(type);
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}
	
	@PostMapping("/insert-expens-types")
	public ResponseEntity<?> expTypesFieldInsertions(@RequestBody OnsiteExpensesType exptype, Principal principal){
		this.contantConstantFieldService.insertExpType(exptype, principal);
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}
	
	@PostMapping("/insert-adsress-types")
	public ResponseEntity<?> addTypeFieldInsertions(@RequestBody AddressType addType){
		this.contantConstantFieldService.insertAddressTyp(addType);
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/get-all-subDepart")
	public ResponseEntity<List<SubDepartments>> allSubDeparts(){
		List<SubDepartments> all=this.contantConstantFieldService.getAllSubDepartments();
		return new ResponseEntity<List<SubDepartments>>(all, HttpStatus.OK);
	}
	
	@GetMapping("/get-all-Depart")
	public ResponseEntity<List<Departments>> allDeparts(){
		List<Departments> all=this.contantConstantFieldService.getAllDepartments();
		return new ResponseEntity<List<Departments>>(all, HttpStatus.OK);
	}
	
	@GetMapping("get-all-addType")
	public ResponseEntity<List<AddressType>> allAddType(){
		List<AddressType> addType=contantConstantFieldService.getAllAddType();
		return new ResponseEntity<List<AddressType>>(addType, HttpStatus.OK);
	}
	
	@GetMapping("get-all-clients")
	public ResponseEntity<List<Clients>> getAllClients(){
		List<Clients> allC=this.clientsRepository.findAll();
		return new ResponseEntity<List<Clients>>(allC,HttpStatus.OK);
	}
	
	@GetMapping("/get-all-desg")
	public ResponseEntity<List<Designations>> getAllDesgList(){
		List<Designations> allDsg=designationsRepository.findAll();
		return new ResponseEntity<List<Designations>>(allDsg,HttpStatus.OK);
	}
	
	@GetMapping("/get-all-empTypes")
	public ResponseEntity<?> getallEmpTypeList(){
		List<EmployeeType> allType=this.employeeTypeRepository.findAll();
		return new ResponseEntity<List<EmployeeType>>(allType,HttpStatus.OK);
	}
	
	@GetMapping("/get-all-expType")
	public ResponseEntity<List<OnsiteExpensesType>> getExpType(){
		List<OnsiteExpensesType> allType=this.onsiteExpensesTypeRepository.findAll();
		return new ResponseEntity<List<OnsiteExpensesType>>(allType, HttpStatus.OK);
	}
	
	

}
