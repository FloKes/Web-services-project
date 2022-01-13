package tokentests;


import java.io.Serializable;

public class Token implements Serializable {
    private static final long serialVersionUID = 9023222981284806610L;
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}