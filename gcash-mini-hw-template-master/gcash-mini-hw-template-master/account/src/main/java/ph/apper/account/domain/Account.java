package ph.apper.account.domain;


import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class Account {
    private UUID accountId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private BigDecimal balance;
    private boolean isVerified;

    private LocalDateTime dateRegistered;
    private LocalDateTime dateVerified;
    private LocalDateTime lastLogin;

    public Account(UUID accountId){
        this.accountId = accountId;
    }

    public Account(){

    }
}
