package searchengine.dto.statistics;

import lombok.Data;

@Data
public class SuccessResponse {
    private final boolean result;

    public SuccessResponse() {
        this.result = true;
    }
}
