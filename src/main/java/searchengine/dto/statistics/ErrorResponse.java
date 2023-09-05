package searchengine.dto.statistics;

import lombok.Data;

@Data
public class ErrorResponse {
    private final boolean result;
    private final String error;

    public ErrorResponse(String error) {
        this.result = false;
        this.error = error;
    }
}
