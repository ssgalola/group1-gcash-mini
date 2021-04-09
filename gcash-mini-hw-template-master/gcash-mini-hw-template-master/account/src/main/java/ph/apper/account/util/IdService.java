package ph.apper.account.util;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class IdService {
    public static UUID getNextUserId() {
        return UUID.randomUUID();
    }

    public static String generateCode(int size) {
        return RandomStringUtils.randomAlphanumeric(size);
    }
}
