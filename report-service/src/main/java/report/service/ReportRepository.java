package report.service;

import domain.MerchantPayment;
import domain.Payment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportRepository {
    private Map<String, List<Payment>> customerReport;
    private Map<String, List<MerchantPayment>> merchantReport;
    private List<Payment> managerReport;

    public ReportRepository() {
        this.customerReport = new HashMap<>();
        this.merchantReport = new HashMap<>();
        this.managerReport = new ArrayList<>();
    }

    public void addPaymentToCustomerList(String id, Payment payment){
        if(customerReport.containsKey(id)){
            customerReport.get(id).add(payment);
        }
        else {
            List<Payment> payments = new ArrayList<>();
            payments.add(payment);
            customerReport.put(id, payments);
        }
    }

    public void addPaymentToMerchantList(String id, MerchantPayment payment){
        System.out.println(id);
        if(merchantReport.containsKey(id)){
            merchantReport.get(id).add(payment);
        }
        else {
            List<MerchantPayment> payments = new ArrayList<>();
            payments.add(payment);
            merchantReport.put(id, payments);
        }
    }

    public void addPaymentToManagerList(Payment payment){
        managerReport.add(payment);
    }

    public List<Payment> getCustomerReportById(String id) throws Exception {
        if(customerReport.containsKey(id)){
            return customerReport.get(id);
        }
        else throw new Exception("No report for customer");
    }

    public List<MerchantPayment> getMerchantReportById(String id) throws Exception {
        if(merchantReport.containsKey(id)){
            return merchantReport.get(id);
        }
        else throw new Exception("No report for merchant");
    }

    public List<Payment> getManagerReport() throws Exception {
        if(managerReport.size()==0) {
            throw new Exception("No report for manager");
        }
        return managerReport;
    }

}
