package ph.apper.account.payload;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AuthenticateAccountRequest {

    @NotBlank (message = "Email is required")
    private String email;

    @NotBlank (message = "Password is required")
    private String password;
}
