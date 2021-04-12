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
    private Double balance;
    private boolean isVerified;

    private LocalDateTime dateVerified;

    public Account(UUID accountId){
        this.accountId = accountId;
    }

    public Account(){

    }
}
