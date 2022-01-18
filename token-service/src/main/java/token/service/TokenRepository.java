package token.service;

import domain.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

public class TokenRepository {
    private Map<String, Token> tokenList;

    public TokenRepository() {
        this.tokenList = new HashMap<>();
    }

    public List<String> getTokenIdList(String customerId) throws Exception {
        if (customerId.length() == 0){
            throw new Exception("Customer id is empty");
        }
        List<String> tokens = new ArrayList<>();
        int requiredNumber = 0;
        Long numberOfTokens = tokenList.values().stream()
                .collect( groupingBy( Token::getUserID, Collectors.counting() ) ).get(customerId);
        if  (numberOfTokens == null) requiredNumber = 6;
        else requiredNumber = 6 - Math.toIntExact(numberOfTokens);
        if( numberOfTokens == null || numberOfTokens < 2) {
            for (int i = 0; i < requiredNumber; i++){
                Token token = new Token(customerId);
                tokenList.put( token.getTokenID(), token );
                tokens.add(token.getTokenID());
            }
            return tokens;
        }
        else {
            throw new Exception("Too many tokens");
        }
    }

    public void deleteToken(String tokenID) throws Exception {
        if ( checkToken( tokenID ) ){
            tokenList.remove(tokenID);
            System.out.println("deleted: " + tokenID);
        }else {
            throw new Exception ("Token not found");
        }
    }

    public boolean checkToken(String providedTokenID) {
        //Check if digital signature is correct?
        return tokenList.containsKey(providedTokenID);
    }

    public String getCustomerIdByTokenId(String tokenId) throws Exception{
        if (tokenList.containsKey(tokenId)) {
            return tokenList.get(tokenId).getUserID();
        }
        else {
            throw new Exception("CustomerId Not Found");
        }
    }
}
