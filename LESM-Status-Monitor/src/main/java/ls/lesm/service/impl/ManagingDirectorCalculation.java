package ls.lesm.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ls.lesm.model.Designations;
import ls.lesm.model.InternalExpenses;
import ls.lesm.model.MasterEmployeeDetails;
import ls.lesm.model.Sub_Profit;
import ls.lesm.repository.InternalExpensesRepository;
import ls.lesm.repository.MasterEmployeeDetailsRepository;
import ls.lesm.repository.Sub_ProfitRepository;

@Service
public class ManagingDirectorCalculation {
	
	Long total=0l;
	@Autowired
	BusinessCalculation bc;
	
	@Autowired
	MasterEmployeeDetailsRepository masterEmployeeDetailsRepository;
	
	@Autowired
	CountryHeadCalculation countryHeadCalculation;
	
	@Autowired
    InternalExpensesRepository internalExpensesrepo;

    @Autowired
    Sub_ProfitRepository sub_ProfitRepository;
	
	
	
	public Double managingDirectorCal(int managingdirector)
	{

		List<MasterEmployeeDetails> ls = masterEmployeeDetailsRepository.findBymasterEmployeeDetails_Id(managingdirector);

		Double profit_or_loss=0.0;
		Double sub_profit=0.0;
		
		if(!ls.isEmpty())
		{
		
		for (MasterEmployeeDetails Employeeid : ls) {

			System.out.println(Employeeid);

//			Optional<MasterEmployeeDetails> id = masterEmployeeDetailsRepository.findById(Employeeid.getEmpId());
//
//			MasterEmployeeDetails epm = null;
//
//			if (id.isPresent()) {
//				epm = id.get();
//			}

			int a = Employeeid.getEmpId();

			String compare = "HR";

            Designations designationOfEmployee = Employeeid.getDesignations();
          String designationNameOfEmployee = designationOfEmployee.getDesgNames();

          Pattern p = Pattern.compile(compare, Pattern.CASE_INSENSITIVE);

          Matcher m = p.matcher(designationNameOfEmployee);

          System.out.println(m.matches());

          if (m.matches()) {
              sub_profit += bc.Employee_cal(a);
              continue;
          }
			profit_or_loss = (Double)countryHeadCalculation.countryHeadCal(a);
			sub_profit += profit_or_loss;

		}
		}
		
		Optional<InternalExpenses> i = internalExpensesrepo.findBymasterEmployeeDetails_Id( managingdirector);

        InternalExpenses o = i.get();

        Double Total_profit_or_loss = sub_profit + bc.Employee_cal( managingdirector);

        o.setProfitOrLoss(Total_profit_or_loss);

        //

        Optional<MasterEmployeeDetails> me = masterEmployeeDetailsRepository.findById( managingdirector);
        MasterEmployeeDetails med = me.get();


        Optional<Sub_Profit> s_p=sub_ProfitRepository.findBymasterEmployeeDetails_Id( managingdirector);

        if(s_p.isPresent())
        {
            Sub_Profit s=s_p.get();

            s.setSubprofit(sub_profit);

            sub_ProfitRepository.save(s);

        }

        else
        {

        Sub_Profit sp = new Sub_Profit(sub_profit, med);

        System.out.println(sp);

        sub_ProfitRepository.save(sp);

        }
        return Total_profit_or_loss;
		//return (Double)(sub_profit - bc.Employee_cal(managingdirector));


	}
	
	

}
