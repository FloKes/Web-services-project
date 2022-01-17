package mappers;

import DTOs.BankAccountRequestDTO;
import DTOs.PaymentDTO;
import DTOs.TokenValidationDTO;
import domain.Payment;
import io.cucumber.gherkin.Token;

public class Mapper {
    public static void mapPaymentToDTO(Payment payment, PaymentDTO paymentDTO){
        paymentDTO.setAmount(payment.getAmount());
        paymentDTO.setCorrelationId(payment.getCorrelationId());
        paymentDTO.setCustomerToken(payment.getCustomerToken());
        paymentDTO.setMerchantId(payment.getMerchantId());
        paymentDTO.setDescription(payment.getDescription());
    }

    public static void mapPaymentDTOToPayment(PaymentDTO paymentDTO, Payment payment){
        payment.setAmount(paymentDTO.getAmount());
        payment.setCorrelationId(paymentDTO.getCorrelationId());
        payment.setCustomerToken(paymentDTO.getCustomerToken());
        payment.setMerchantId(paymentDTO.getMerchantId());
        payment.setDescription(paymentDTO.getDescription());
    }

    public static void mapBankAccountRequestDTO(Payment payment, TokenValidationDTO tokenValidationDTO, BankAccountRequestDTO bankAccountRequestDTO){
        bankAccountRequestDTO.setCorrelationId(payment.getCorrelationId());
        bankAccountRequestDTO.setCustomerId(tokenValidationDTO.getCustomerId());
        bankAccountRequestDTO.setMerchantId(payment.getMerchantId());
    }

    public static void mapTokenValidationDTO(Payment payment, TokenValidationDTO tokenValidationDTO){
        tokenValidationDTO.setCustomerToken(payment.getCustomerToken());
        tokenValidationDTO.setCorrelationId(payment.getCorrelationId());
    }
}
