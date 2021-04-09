package ph.apper.purchase.domain;


import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class Account {
    private String firstName;
    private String lastName;
    private String email;
    private String password;


    public Account(){

    }

}
