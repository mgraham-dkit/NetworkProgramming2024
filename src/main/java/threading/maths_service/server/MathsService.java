package threading.maths_service.server;

public class MathsService {
    // Address information
    public static final String HOST = "localhost";
    public static final int PORT = 12432;

    // Protocol commands
    public static final String SQUARE = "SQUARE";
    public static final String CUBE = "CUBE";
    public static final String MYLARGEST = "MYLARGEST";
    public static final String LARGEST = "LARGEST";
    public static final String EXIT = "EXIT";

    // RESPONSES
    public static final String NOT_NUMBER = "PLEASE_SUPPLY_NUMBER";
    public static final String SIGNOFF = "GOODBYE";
    public static final String INVALID = "UNKNOWN_COMMAND";

    // DELIMITER
    public static final String DELIMITER = "%%";
}
