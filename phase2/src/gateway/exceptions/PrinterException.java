package gateway.exceptions;

public class PrinterException extends RuntimeException {
    public PrinterException() {
        super("Unable to print document.");
    }
}
