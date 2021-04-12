package ph.apper.gateway.payload;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class AccountRequest {

    @NotBlank (message = "First Name is required")
    private String firstName;

    @NotBlank (message = "Last Name is required")
    private String lastName;

    @Email (message = "Email must be valid")
    @NotBlank (message = "Email is required")
    private String email;

    @NotBlank (message = "Password is required")
    private String password;

}
