package pl.rspective.voucherify.android.client.exception;

public class VoucherifyError extends Exception {

    private int code;
    private String message;
    private String details;

    public VoucherifyError(Throwable cause) {
        super(cause);
        this.code = 0;
        this.message = "Unexpected error";
    }

    public int getCode() {
        return code;
    }
    
    public String getMessage() {
        return message;
    }
    
    public String getDetails() {
        return details;
    }
    
    @Override
    public String toString() {
        return "VoucherifyError[code=" + code + ", message='" + message + "', details='" + details + "']";
    }
}