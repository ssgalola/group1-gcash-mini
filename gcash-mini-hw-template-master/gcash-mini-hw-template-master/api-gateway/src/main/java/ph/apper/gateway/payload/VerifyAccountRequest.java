package ph.apper.gateway.payload;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class VerifyAccountRequest {
    @NotBlank (message = "Verification Code is required")
    private String verificationCode;

    @Email (message = "Email must be valid")
    @NotBlank (message = "Email is required")
    private String email;
}
