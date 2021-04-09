package ph.apper.account.payload.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ph.apper.account.domain.Account;

@Data
public class GetAccountResponse {
    public GetAccountResponse(Account account){
        this.firstName = account.getFirstName();
        this.lastName = account.getLastName();
        this.email = account.getEmail();
        this.balance = Double.toString(account.getBalance());
    }

    @JsonProperty(value="firstName")
    private String firstName;

    @JsonProperty(value="lastName")
    private String lastName;

    @JsonProperty(value="email")
    private String email;

    @JsonProperty(value="balance")
    private String balance;

}
