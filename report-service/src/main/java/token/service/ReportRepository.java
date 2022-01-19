package token.service;

import domain.Payment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportRepository {
    private Map<String, List<Payment>> customerReport;
    private Map<String, List<Payment>> merchantReport;
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

    public void addPaymentToMerchantList(String id, Payment payment){
        if(merchantReport.containsKey(id)){
            merchantReport.get(id).add(payment);
        }
        else {
            List<Payment> payments = new ArrayList<>();
            payments.add(payment);
            merchantReport.put(id, payments);
        }
    }

    public void addPaymentToManagerList(Payment payment){
        managerReport.add(payment);
    }

    public List<Payment> getCustomerReportById(String id) {
        return customerReport.get(id);
    }

    public List<Payment> getMerchantReportById(String id) {
        return merchantReport.get(id);
    }

    public List<Payment> getManagerReport() {
        return managerReport;
    }

}
