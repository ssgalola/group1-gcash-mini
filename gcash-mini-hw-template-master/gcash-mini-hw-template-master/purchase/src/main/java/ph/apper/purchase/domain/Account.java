package ph.apper.purchase.domain;


import lombok.Data;

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

    private LocalDateTime dateRegistered;
    private LocalDateTime dateVerified;
    private LocalDateTime lastLogin;

    public Account(UUID accountId){
        this.accountId = accountId;
    }

    public Account(){

    }
}
