package ph.apper.activity.payload;

import lombok.Data;

@Data
public class LogActivity {
    private String action;
    private String identifier;
    private String details;
}

