package payment.service;

import domain.Token;

import java.io.Serializable;
import lombok.Data;

@Data // Automatic getter and setters and equals etc
public class Payment implements Serializable {
    private static final long serialVersionUID = 821858579108456995L;
    private String correlationId;
    private String merchantId;
    private String tokenId;
    private float amount;
    private String description;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Payment)) {
            return false;
        }
        var c = (Payment) o;
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