package mappers;

import DTOs.PaymentReportDTO;
import domain.Payment;

public class Mapper {

    public static void PaymentReportDTOtoPaymentMapper(PaymentReportDTO paymentReportDTO, Payment payment) {
        payment.setPaymentId(paymentReportDTO.getPaymentId());
        payment.setDescription(paymentReportDTO.getDescription());
        payment.setAmount(paymentReportDTO.getAmount());
        payment.setMerchantId(payment.getMerchantId());
        payment.setCustomerId(payment.getCustomerId());
    }
}
