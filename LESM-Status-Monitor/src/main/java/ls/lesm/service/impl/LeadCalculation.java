package ls.lesm.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ls.lesm.model.InternalExpenses;
import ls.lesm.model.MasterEmployeeDetails;
import ls.lesm.model.Sub_Profit;
import ls.lesm.repository.InternalExpensesRepository;
import ls.lesm.repository.MasterEmployeeDetailsRepository;
import ls.lesm.repository.Sub_ProfitRepository;


@Service
public class LeadCalculation {
	

	Long total = 0l;

	@Autowired
	MasterEmployeeDetailsRepository masterEmployeeDetailsRepository;

	@Autowired
	BusinessCalculation bc;
	@Autowired
    InternalExpensesRepository internalExpensesrepo;

    @Autowired
    Sub_ProfitRepository sub_ProfitRepository;

	public Double lead_cal(int leademployeeid) {

		// masterEmployeeDetailsRepository.findById(leademployeeid);

		List<MasterEmployeeDetails> ls = masterEmployeeDetailsRepository.findBymasterEmployeeDetails_Id(leademployeeid);

		// List<MasterEmployeeDetails>
		// ls=masterEmployeeDetailsRepository.findBymasterEmployeeDetailsId(leademployeeid);

		Double profit_or_loss = 0.0;
		Double sub_profit = 0.0;

		if (!ls.isEmpty()) {

			for (MasterEmployeeDetails Employeeid : ls) {

				System.out.println(Employeeid);

//			
//			Optional<MasterEmployeeDetails> id = masterEmployeeDetailsRepository.findById(Employeeid.getEmpId());
//
//			MasterEmployeeDetails epm = null;
//
//			if (id.isPresent()) {
//				epm = id.get();
//			}

				int a = Employeeid.getEmpId();

				profit_or_loss = (Double) bc.Employee_cal(a);
				sub_profit += profit_or_loss;

			}
		}
		
		Optional<InternalExpenses> i = internalExpensesrepo.findBymasterEmployeeDetails_Id(leademployeeid);

        InternalExpenses o = i.get();

        Double Total_profit_or_loss = sub_profit + bc.Employee_cal(leademployeeid);

        o.setProfitOrLoss(Total_profit_or_loss);

        //

        Optional<MasterEmployeeDetails> me = masterEmployeeDetailsRepository.findById(leademployeeid);
        MasterEmployeeDetails med = me.get();


        Optional<Sub_Profit> s_p=sub_ProfitRepository.findBymasterEmployeeDetails_Id(leademployeeid);

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
		
		//return (Double) (sub_profit - bc.Employee_cal(leademployeeid));

		// return 0.0;

	}

}
