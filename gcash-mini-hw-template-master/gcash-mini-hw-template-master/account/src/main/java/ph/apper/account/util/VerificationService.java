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

    public void verifyAccount(String email){
        verificationCodes.remove(email);
    }
}
