public class ErrorResponse {
    private String message;
    private String error;

    // Getters e Setters
    public String getMessage() {
        return message != null ? message : error;
    }
}