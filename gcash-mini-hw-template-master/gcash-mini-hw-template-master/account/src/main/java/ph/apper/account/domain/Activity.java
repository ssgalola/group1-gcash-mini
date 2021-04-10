package ph.apper.account.domain;

import lombok.Data;

@Data
public class Activity {
    private String action;
    private String identifier;
    private String details;
}

