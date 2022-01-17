package DTOs;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data // Automatic getter and setters and equals etc
public class PaymentDTO implements Serializable {
    private static final long serialVersionUID = -2830691737166555950L;
    private String correlationId;
    private String merchantId;
    private String customerToken;
    private BigDecimal amount;
    private String description;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof domain.Payment)) {
            return false;
        }
        var c = (domain.Payment) o;
        return correlationId != null && correlationId.equals(c.getCorrelationId()) ||
                correlationId == null && c.getCorrelationId() == null;
    }

    @Override
    public int hashCode() {
        return correlationId == null ? 0 : correlationId.hashCode();
    }

    @Override
    public String toString() {
        return String.format("CorrelationID:  %s", correlationId);
    }
}

