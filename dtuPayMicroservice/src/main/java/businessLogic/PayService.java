package businessLogic;

import domain.Payment;
import domain.UserRest;
import dtu.ws.fastmoney.BankServiceException_Exception;
import io.cucumber.java.bs.A;


import javax.jws.soap.SOAPBinding;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class PayService {

    private static PayService instance = new PayService();

    private ArrayList<Payment> payments;
    private HashMap<String, UserRest> users;


    private PayService() {
        this.payments = new ArrayList<>();
        this.users = new HashMap<>();
    }

    public static PayService getInstance() {return instance;}

    public String createUser(UserRest user){
        try {

            String DTUAccountID = UUID.randomUUID().toString();
            users.put( DTUAccountID, user);
            return DTUAccountID;
        } catch (Exception e) {
            return "User does not have a bank account"; //TODO: handle exception
        }
    }

    public String getBalance(String userId) {
        String accountId = null;
        if (users.containsKey(userId)) {
            accountId = users.get(userId).getBankAccountId();

        }
        try {
        } catch (Exception e) {
            return "User does not have a bank account"; //TODO: handle exception
        }
        return "";
    }

    public boolean hasDTUPayaccount( UserRest user ) {
        return users.containsValue( user );
    }

    public void addPayment(Payment payment) throws BankServiceException_Exception {
        UserRest customer = payment.getCid();
        UserRest merchant = payment.getMid();
        Integer amount = payment.getAmount();
        //soapController.setBalance(customer.getBankAccountId(), Integer.valueOf(getBalance(customer.getUserId())) - amount);
        //soapController.setBalance(merchant.getBankAccountId(), Integer.valueOf(getBalance(merchant.getUserId())) + amount);
        this.payments.add(payment);
    }

    public Payment getPayment(int id){
        return payments.get(id - 1);
    }

    public ArrayList<Payment> getPayments() {
        return payments;
    }

    public int getNewPaymentId(){
        return payments.size() + 1;
    }

    public void addUser(UserRest user) {this.users.put(user.getUserId(), user);}
}

