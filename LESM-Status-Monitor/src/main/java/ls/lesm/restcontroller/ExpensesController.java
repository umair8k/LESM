package ls.lesm.restcontroller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ls.lesm.model.InternalExpenses;
import ls.lesm.model.OnsiteBillExpenses;
import ls.lesm.model.Salary;
import ls.lesm.service.impl.ExpenseServiceImpl;

@RestController
@RequestMapping("api/v1/exp")
@CrossOrigin("*")
public class ExpensesController {
	
	@Autowired
	private ExpenseServiceImpl expenseService;
	
	@PostMapping("/insert-expenses")
	public ResponseEntity<?> expensesFieldInsertion(@RequestParam int expTypeId,
			                                        @RequestParam String empId,
			                                        @RequestBody OnsiteBillExpenses billExp,
			                                        Principal principal ){
		
		this.expenseService.insertBillExp(billExp, principal, expTypeId, empId);
		
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
		
	}
	
	@PostMapping("/inser-internal-expens")
	public ResponseEntity<InternalExpenses> insertIntExpses(@RequestParam String empId, 
			                                                @RequestBody  InternalExpenses intExp,
			                                                              Principal principal){		
		InternalExpenses exp=this.expenseService.insertIntExp(intExp, principal, empId);
		return new ResponseEntity<InternalExpenses>(HttpStatus.CREATED);
	}
	
	@PostMapping("/insert-sal")
	public ResponseEntity<?> insertSal(@RequestParam Integer empId,
			                           @RequestBody  Salary sal,
			                                         Principal principal){
		this.expenseService.inserSal(sal, principal, empId);
		return new ResponseEntity<>(HttpStatus.CREATED);
		
	}
	
}
