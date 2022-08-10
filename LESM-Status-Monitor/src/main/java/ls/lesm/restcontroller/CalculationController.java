package ls.lesm.restcontroller;

import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ls.lesm.model.MasterEmployeeDetails;
import ls.lesm.model.Sub_Profit;
import ls.lesm.repository.MasterEmployeeDetailsRepository;
import ls.lesm.repository.Sub_ProfitRepository;
import ls.lesm.service.impl.BusinessCalculation;
import ls.lesm.service.impl.CountryHeadCalculation;
import ls.lesm.service.impl.GeneralManagerCalculation;
import ls.lesm.service.impl.LeadCalculation;
import ls.lesm.service.impl.ManagerCalculation;
import ls.lesm.service.impl.ManagingDirectorCalculation;

@RestController
@RequestMapping("/Total")
@CrossOrigin("*")
public class CalculationController {
	
	@Autowired
	BusinessCalculation businessCalculation;
	
	@Autowired
	LeadCalculation leadCalculation;
	
	@Autowired
	ManagerCalculation managerCalculation;
	
	@Autowired
	GeneralManagerCalculation generalManagerCalculation;
	
	@Autowired
	CountryHeadCalculation countryHeadCalculation;
	
	@Autowired
    ManagingDirectorCalculation managingDirectorCalculation;
	
	@Autowired
	MasterEmployeeDetailsRepository masterEmployeeDetailsRepository;
	
	@Autowired
	Sub_ProfitRepository sub_ProfitRepository;
	

	@GetMapping("/consultant")
	public Double s(Principal principal)
	
	{
		String emp1=principal.getName();
		MasterEmployeeDetails emp2=this.masterEmployeeDetailsRepository.findByLancesoft(emp1);
		int id=emp2.getEmpId();
		Double profit_or_loss=businessCalculation.Employee_cal(id);
		
		return profit_or_loss;
		
	}
	
//	
//	@PostMapping("/lead")
//	public Double lead( Principal principal)
//	{
//		String emp1=principal.getName();
//		MasterEmployeeDetails emp2=this.masterEmployeeDetailsRepository.findByLancesoft(emp1);
//		int id=emp2.getEmpId();
//		Double profit_or_loss=leadCalculation.lead_cal(id);
//	       Optional<Sub_Profit> sp = sub_ProfitRepository.findBymasterEmployeeDetails_Id(id);
//
//	        Sub_Profit spt = sp.get();
//
//	        Double subprofit = spt.getSubprofit();
//
//	        return (subprofit);
//		
//		//return profit_or_loss;
//		
//	}
	
	
	@GetMapping("/lead")
	public ResponseEntity<Double> lead( Principal principal)
	{
		String emp1=principal.getName();
		MasterEmployeeDetails emp2=this.masterEmployeeDetailsRepository.findByLancesoft(emp1);
		int id=emp2.getEmpId();
		Double profit_or_loss=leadCalculation.lead_cal(id);
	       Optional<Sub_Profit> sp = sub_ProfitRepository.findBymasterEmployeeDetails_Id(id);

	        Sub_Profit spt = sp.get();

	        Double subprofit = spt.getSubprofit();
	        
	        //return (subprofit);
	        return new ResponseEntity<Double>(subprofit,HttpStatus.OK);
		
		//return profit_or_loss;
		
	}
	
	
	

	@GetMapping("/managerCalculation")
	public Double managerCalculation(Principal principal)
	{
		String loggedIn=principal.getName();
		MasterEmployeeDetails employee=this.masterEmployeeDetailsRepository.findByLancesoft(loggedIn);
		int id=employee.getEmpId();
		Double profit_or_loss=managerCalculation.manager_cal(id);
		
		 Optional<Sub_Profit> sp = sub_ProfitRepository.findBymasterEmployeeDetails_Id(id);

	        Sub_Profit spt = sp.get();

	        Double subprofit = spt.getSubprofit();

	        return (subprofit);
		
	//	return profit_or_loss;
		
	}
	
	
	@GetMapping("/generalManagerCalculation")
	public Double generalManagerCalculationManagerCalculation(Principal principal)
	{
		String loggedIn=principal.getName();
		MasterEmployeeDetails employee=this.masterEmployeeDetailsRepository.findByLancesoft(loggedIn);
		int id=employee.getEmpId();
		Double profit_or_loss=generalManagerCalculation.generalManagercal(id);
		
		 Optional<Sub_Profit> sp = sub_ProfitRepository.findBymasterEmployeeDetails_Id(id);

	        Sub_Profit spt = sp.get();

	        Double subprofit = spt.getSubprofit();

	        return (subprofit);
		
		//return profit_or_loss;
		
	}
	
	

	@GetMapping("/countryHeadCalculation")
	public Double countryHeadCalculation(Principal principal)
	{
		String loggedIn=principal.getName();
		MasterEmployeeDetails employee=this.masterEmployeeDetailsRepository.findByLancesoft(loggedIn);
		int id=employee.getEmpId();
		Double profit_or_loss=countryHeadCalculation.countryHeadCal(id);
		
		 Optional<Sub_Profit> sp = sub_ProfitRepository.findBymasterEmployeeDetails_Id(id);

	        Sub_Profit spt = sp.get();

	        Double subprofit = spt.getSubprofit();

	        return (subprofit);
		
		//return profit_or_loss;
		
	}
	
	
	
	@GetMapping("/managingDirectorCalculation")
	public Double managingDirectorCalculation(Principal principal)
	{
		String loggedIn=principal.getName();
		MasterEmployeeDetails employee=this.masterEmployeeDetailsRepository.findByLancesoft(loggedIn);
		int id=employee.getEmpId();
		Double profit_or_loss=managingDirectorCalculation.managingDirectorCal(id);
		 Optional<Sub_Profit> sp = sub_ProfitRepository.findBymasterEmployeeDetails_Id(id);

	        Sub_Profit spt = sp.get();

	        Double subprofit = spt.getSubprofit();

	        return (subprofit);		
		
		
		
		
	}
	
}
