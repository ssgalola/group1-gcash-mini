package ph.apper.account.domain;

import lombok.Data;
import lombok.Generated;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import static javax.persistence.GenerationType.*;

@Data
@Entity
public class VerificationCode {

    public VerificationCode(String email, String code) {
        this.email = email;
        this.code = code;
    }

    public VerificationCode() {
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String email;
    private String code;

}
