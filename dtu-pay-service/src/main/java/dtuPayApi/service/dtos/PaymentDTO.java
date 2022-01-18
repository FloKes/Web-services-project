package dtuPayApi.service.dtos;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

@Data // Automatic getter and setters and equals etc
@ToString
public class PaymentDTO implements Serializable {
    private static final long serialVersionUID = -2531884656471531975L;
    private String paymentId;
    private String merchantId;
    private String customerToken;
    private BigDecimal amount;
    private String description;
}


