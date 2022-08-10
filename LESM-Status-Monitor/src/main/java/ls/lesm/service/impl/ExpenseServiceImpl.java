package ls.lesm.service.impl;

import java.security.Principal;
import java.time.LocalDate;
import java.time.Period;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ls.lesm.exception.DateMissMatchException;
import ls.lesm.exception.EmployeeAreadyExistException;
import ls.lesm.exception.RelationNotFoundExceptions;
import ls.lesm.model.EmployeeStatus;
import ls.lesm.model.InternalExpenses;
import ls.lesm.model.MasterEmployeeDetails;
import ls.lesm.model.OnsiteBillExpenses;
import ls.lesm.model.Salary;
import ls.lesm.repository.InternalExpensesRepository;
import ls.lesm.repository.MasterEmployeeDetailsRepository;
import ls.lesm.repository.OnsiteBillExpensesRepository;
import ls.lesm.repository.OnsiteExpensesTypeRepository;
import ls.lesm.repository.SalaryRepository;
import ls.lesm.service.ExpenseService;

@Service
public class ExpenseServiceImpl implements ExpenseService {
	
	@Autowired
	private OnsiteBillExpensesRepository onsiteBillExpensesRepository;
	
	@Autowired
	private OnsiteExpensesTypeRepository onsiteExpensesTypeRepository;
	
	@Autowired
	private MasterEmployeeDetailsRepository masterEmployeeDetailsRepository;
	
	@Autowired
	private InternalExpensesRepository internalExpensesRepositoryal;
	
	@Autowired
	private SalaryRepository salaryRepository;
	
	@Override
	public OnsiteBillExpenses insertBillExp(OnsiteBillExpenses billExp, Principal principal, int expTypeId, String empId)  {
		billExp.setCreatedAt(new Date());
		billExp.setCreatedBy(principal.getName());
		
		Integer months=Period.between(billExp.getTravelSDate(),billExp.getTravelEDate()).getMonths();
		Integer days=Period.between(billExp.getTravelSDate(),billExp.getTravelEDate()).getDays();
		
		billExp.setTotalTravelPeriod(days+" days "+months+" Month");
		
		if(billExp.getTravelSDate().isAfter(billExp.getTravelEDate()))
			throw new DateMissMatchException("","Travel start date can not be before travel end date");
		
		this.onsiteExpensesTypeRepository.findById(expTypeId).map(bill->{
			billExp.setOnsiteExpensesType(bill);
			return bill;
		});//.orElseThrow(()-> new RelationNotFoundExceptions("This expense Type with this id "+empId+" Not exist","501",""));
		
		MasterEmployeeDetails employee=this.masterEmployeeDetailsRepository.findByLancesoft(empId);
		this.masterEmployeeDetailsRepository.findById(employee.getEmpId()).map(id->{
			billExp.setMasterEmployeeDetails(id);
			return id;
			
		});
		
		
		return onsiteBillExpensesRepository.save(billExp);
	}

	@Override
	public InternalExpenses  insertIntExp(InternalExpenses intExp, Principal principal,String empId) {
		MasterEmployeeDetails employee=this.masterEmployeeDetailsRepository.findByLancesoft(empId);
		intExp.setCreatedAt(LocalDate.now());
		intExp.setCreatedBy(principal.getName());
		Optional<Object>obj=this.masterEmployeeDetailsRepository.findById(employee.getEmpId()).map(id->{
			intExp.setMasterEmployeeDetails(id);
			return id;
		});
		System.out.println("=============="+obj.get());
		Optional<InternalExpenses> employee1=this.internalExpensesRepositoryal.findByEmployeeById(employee.getEmpId());
	   //  if(employee.isPresent())
	    //	if( employee.get().getMasterEmployeeDetails().getStatus()==EmployeeStatus.ACTIVE && employee.get().getMasterEmployeeDetails().getStatus()==EmployeeStatus.MANAGMENT)
	      //        throw new EmployeeAreadyExistException();
		
			//throw new RelationNotFoundExceptions("Inserting detail is not alld","","");
			//System.out.println("hiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii");
		
		return internalExpensesRepositoryal.save(intExp);
	
	}

	@Override
	public Salary inserSal(Salary sal, Principal principal, Integer empId) {
		sal.setCreatedAt(LocalDate.now());
		sal.setCreatedBy(principal.getName());
		this.masterEmployeeDetailsRepository.findById(empId).map(id->{
			sal.setMasterEmployeeDetails(id);
			return id;
		});
		
		//Optional employee= this.salaryRepository.findByEmployeeById(empId);
		//System.out.println("------------"+sal.getMasterEmployeeDetails().getEmpId());
		//if(null!=sal.getMasterEmployeeDetails().getEmpId())
			//throw new EmployeeAreadyExistException("Salary for this emplyee already present","801");
		//System.out.println("=============="+employee);
		//if(employee!=null)
			
		return salaryRepository.save(sal);
	}

}
