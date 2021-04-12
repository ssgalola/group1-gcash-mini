package ph.apper.account.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ph.apper.account.domain.Account;

import java.util.UUID;

@Data
public class AuthenticateResponse {
    public AuthenticateResponse(Account account) {
        this.accountId = account.getAccountId();
    }

    @JsonProperty(value = "accountId")
    private String accountId;

    public AuthenticateResponse() {
    }
}
