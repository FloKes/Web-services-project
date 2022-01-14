package domain;

import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.UUID;
//import javax.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;



//@XmlRootElement // Needed for XML serialization and deserialization
@Data // Automatic getter and setters and equals etc
@NoArgsConstructor // Needed for JSON deserialization and XML serialization and deserialization
@AllArgsConstructor
public class Token implements Serializable {
    private String userID;
    //private static final long serialVersionUID = 9023222981284806610L;
    public String tokenID;

    public String getTokenID() {
        return tokenID;
    }
    public void setTokenID(String tokenID) {
        this.tokenID = tokenID;
    }

    public Token(String userID){
        this.userID = userID;
        //this.tokenID = UUID.randomUUID().toString();
        this.tokenID = "PorkoltSzaft";
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Token)) {
            return false;
        }
        var c = (Token) o;
        return tokenID != null && tokenID.equals(c.getTokenID()) ||
                tokenID == null && c.getTokenID() == null;
    }

    @Override
    public int hashCode() {
        return tokenID == null ? 0 : tokenID.hashCode();
    }

    @Override
    public String toString() {
        return String.format("Token:  %s", tokenID);
    }
}
