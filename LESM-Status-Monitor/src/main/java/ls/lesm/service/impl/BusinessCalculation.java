package ls.lesm.service.impl;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ls.lesm.model.EmployeeStatus;
import ls.lesm.model.EmployeesAtClientsDetails;
import ls.lesm.model.InternalExpenses;
import ls.lesm.model.MasterEmployeeDetails;
import ls.lesm.model.OnsiteBillExpenses;
import ls.lesm.model.OnsiteExpensesType;
import ls.lesm.model.Salary;
import ls.lesm.repository.DepartmentsRepository;
import ls.lesm.repository.DesignationsRepository;
import ls.lesm.repository.EmployeesAtClientsDetailsRepository;
import ls.lesm.repository.InternalExpensesRepository;
import ls.lesm.repository.MasterEmployeeDetailsRepository;
import ls.lesm.repository.OnsiteBillExpensesRepository;
import ls.lesm.repository.OnsiteExpensesTypeRepository;
import ls.lesm.repository.SalaryRepository;
import ls.lesm.repository.SubDepartmentsRepository;



@Service
public class BusinessCalculation {

	@Autowired
	private DepartmentsRepository DPR;

	@Autowired
	private DesignationsRepository DSR;

	@Autowired
	private EmployeesAtClientsDetailsRepository clientDetail;

	@Autowired
	private MasterEmployeeDetailsRepository MER;

	@Autowired
	private OnsiteBillExpensesRepository os;

	@Autowired
	InternalExpensesRepository internalExpenseRepo;

	@Autowired
	private OnsiteExpensesTypeRepository ot;

	@Autowired
	private SalaryRepository sr;

	@Autowired
	private SubDepartmentsRepository sdr;

	public double Employee_cal(int employeeId) {

		
		double Total_expenses = 0l;// u
		Integer Total_client_tenure = 0;// u
		double Total_salary_from_client = 0l;// u
		Integer tenure = 0;

		Optional<MasterEmployeeDetails> mn = MER.findById(employeeId);

		MasterEmployeeDetails trackemp = null;

		if (mn.isPresent()) {
			trackemp = mn.get();
		}

		LocalDate joining_date = trackemp.getJoiningDate();
		LocalDate released_date = null;
		
		
		
	    Optional<Salary> tr = sr.findBymasterEmployeeDetails_Id(employeeId);

		Salary salary = null;

		if (tr.isPresent()) {
			salary = tr.get();
		}

		Double salarys = salary.getSalary();
		
		
		if (trackemp.getStatus() == EmployeeStatus.ACTIVE || trackemp.getStatus() == EmployeeStatus.BENCH)

		{

			Period monthsinternal = Period.between(joining_date, LocalDate.now());

			// int workingAtclientMonths = monthsinternal.getYears() * 12 +
			// monthsinternal.getMonths();

			tenure =  (monthsinternal.getYears() * 12 + monthsinternal.getMonths());

			System.out.println("\n\n\n\n\n\n" + tenure);
		}

//		else {
//			tenure = ChronoUnit.MONTHS.between(joining_date, released_date);
//		}
		
		
		
		
	


		Optional<InternalExpenses> inten = internalExpenseRepo.findBymasterEmployeeDetails_Id(employeeId);

		InternalExpenses expenses = null;

		if (inten.isPresent()) {
			expenses = inten.get();
		}

		Double cubical = expenses.getCubicleCost();

		Double transport = expenses.getTransportationCost();

		Double food = expenses.getFoodCost();
		

		double profit_or_loss = 0l;

		
		
		
		
		
		

          List<EmployeesAtClientsDetails> slidet = clientDetail.findsBymasterEmployeeDetails_Id(employeeId);// u
          
          
		
//		Optional<ClientDetails> cli = clientDetail.findById(employeeId);
//
//		ClientDetails details = null;
//
//		if (cli.isPresent()) {
//			
//			//details = cli.get();
          
          if(!slidet.isEmpty())
			
		
          {
		
		


		     for (EmployeesAtClientsDetails cl : slidet) {
			
			       
			

			LocalDate PoSdate = cl.getPOSdate();

			LocalDate PoEdate = cl.getPOEdate();
			LocalDate systemdate = LocalDate.now();
			try
			{
			if (PoEdate == null || systemdate.isBefore( PoEdate)) {

				PoEdate = LocalDate.now();

			}
			}
			
			catch(Exception e)
			{
				PoEdate = LocalDate.now();
				
			}
			Period monthsAtClient = Period.between(PoSdate, PoEdate);

		Integer client_tenure = (monthsAtClient.getYears() * 12 + monthsAtClient.getMonths());

			System.out.println(client_tenure);
			
			Total_client_tenure += client_tenure;
			
			double Bill_at_client=cl.getClientSalary() * client_tenure;
            cl.setTotalEarningAtclient(Bill_at_client);
            clientDetail.save(cl);

			Total_salary_from_client =Total_salary_from_client + (cl.getClientSalary() * client_tenure);

		}
          
		
		}
		
		
		

		List<OnsiteBillExpenses> obex = os.findByMasterEmployeeDetails_Id(employeeId);
		
		
		

		if (!obex.isEmpty()) {
			
	

			for (OnsiteBillExpenses ol : obex) {

				OnsiteExpensesType exptype = ol.getOnsiteExpensesType();// (get value from db);

				double Total_expenses_of_single_record = ol.getTravel() + ol.getCab() + ol.getAccommodation()
						+ ol.getFood();

				if (exptype.getExpId() == 3 || exptype.getExpId() == 4) {

					Total_expenses_of_single_record = ol.getTravel() + ol.getCab() + ol.getAccommodation()
							+ ol.getFood();

					Total_expenses += Total_expenses_of_single_record;
				}

			}

		}

		
		
		double paid_till_now = tenure * salarys + Total_expenses;

		expenses.setTotalSalPaidTillNow(paid_till_now);
//		expenses.setTotalExpenses(Total_expenses);

//		Long position_id = 0l;// (positionfk of employeeId)
//		Long sub_profit = 0l;
//		Long consultatnt_id = 1l;// (positionfk of consultant);

//		if (position_id != consultatnt_id) {
//			Total_number_records=Lsit_of_records_of_employees_with_my_employeeId_As_Their_supervisor_id;
//				 	for(record:Total_number_records)
//					{
//
//			Long Total_profit_or_loss = 0l;
//
//			sub_profit = +Total_profit_or_loss;
//			// }

		Integer Bench_tenure =tenure - Total_client_tenure;

		expenses.setBenchTenure(Bench_tenure);

		double Beanch_expences = Bench_tenure * (cubical + transport + food);

		profit_or_loss = Total_salary_from_client - (paid_till_now + Beanch_expences);
		expenses.setTotalExpenses(paid_till_now + Beanch_expences);
		expenses.setProfitOrLoss(profit_or_loss);

		System.out.println(expenses);

		internalExpenseRepo.save(expenses);
		return profit_or_loss;
	}

	
}