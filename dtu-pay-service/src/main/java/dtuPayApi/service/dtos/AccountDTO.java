package dtuPayApi.service.dtos;

import dtuPayApi.service.CorrelationId;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class AccountDTO implements Serializable{
    private static final long serialVersionUID = -1005048725971420371L;
    private String firstname;
    private String lastname;
    private String cpr;
    private String accountId;
    private String bankAccount;
}
