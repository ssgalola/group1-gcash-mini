package ph.apper.account.domain;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
public class Account {
    @Id
    private String accountId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Double balance;
    private boolean isVerified;

    private LocalDateTime dateRegistered;
    private LocalDateTime dateVerified;
    private LocalDateTime lastLogin;

    public Account(String accountId){
        this.accountId = accountId;
    }

    public Account(){

    }
}
