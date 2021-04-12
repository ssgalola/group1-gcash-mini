package ph.apper.gateway.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ph.apper.gateway.domain.Account;

import java.util.UUID;

@Data
public class AuthenticateResponse {
    public AuthenticateResponse(Account account) {
        this.accountId = account.getAccountId();
    }

    @JsonProperty(value = "accountId")
    private UUID accountId;

    public AuthenticateResponse() {
    }
}
