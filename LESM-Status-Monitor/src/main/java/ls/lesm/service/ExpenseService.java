package ls.lesm.service;

import java.security.Principal;

import ls.lesm.model.InternalExpenses;
import ls.lesm.model.OnsiteBillExpenses;
import ls.lesm.model.Salary;

public interface ExpenseService {
	
	OnsiteBillExpenses insertBillExp(OnsiteBillExpenses billExp, Principal principal, int expTypeId, String empId);
	
	InternalExpenses insertIntExp(InternalExpenses intExp, Principal principal, String empId);
	
	Salary inserSal(Salary sal, Principal principal, Integer empId);

}
