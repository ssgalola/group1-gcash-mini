package ph.apper.account.util;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class VerificationService {
    private final Map<String, String> verificationCodes = new HashMap<>();

    public void addVerificationCode(String email, String code) {
        verificationCodes.put(email, code);
    }

    public boolean verifyAccount(String email, String code){
        if(verificationCodes.get(email).equals(code)){
            verificationCodes.remove(email);
            return true;
        }
        return false;
    }

}
