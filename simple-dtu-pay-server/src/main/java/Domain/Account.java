package Domain;

import javax.xml.bind.annotation.XmlRootElement;

import dtu.ws.fastmoney.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@XmlRootElement // Needed for XML serialization and deserialization
@Data // Automatic getter and setters and equals etc
@NoArgsConstructor // Needed for JSON deserialization and XML serialization and deserialization
@AllArgsConstructor
public class Account {
    private User user;
    private Integer balance;
}
