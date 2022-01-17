package dtos;

import domain.CorrelationId;
import lombok.Data;

import java.io.Serializable;

@Data
public class TokenValidationDTO implements Serializable {
    private static final long serialVersionUID = 8009166186081976286L;
    private CorrelationId correlationId;
    private String customerToken;
    private String customerId;
    private String errorMessage;
}
