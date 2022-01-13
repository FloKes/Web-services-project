package token.service;

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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Token)) {
            return false;
        }
        var c = (Token) o;
        return token != null && token.equals(c.getToken()) ||
                token == null && c.getToken() == null;
    }

    @Override
    public int hashCode() {
        return token == null ? 0 : token.hashCode();
    }

    @Override
    public String toString() {
        return String.format("Token:  %s", token);
    }
}
