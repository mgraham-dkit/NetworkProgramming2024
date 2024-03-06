package udp.file_server_system;

public class FileServerProtocol {
    public static final int PORT = 5784;

    // REQUEST STRINGS
    public static final String EXISTS = "EXISTS";
    public static final String LENGTH = "LENGTH";
    public static final String ACCESSED = "LAST_ACCESSED";
    public static final String LINE = "LINE";
    public static final String DELIMITER = "%%";

    // RESPONSE STRINGS
    public static final String NOT_FOUND_RESPONSE = "FILE_NOT_FOUND";
    public static final String FOUND_RESPONSE = "FOUND";
    public static final String INVALID = "INVALID_COMMAND";
    public static final String BOUNDS = "OUT_OF_BOUNDS";
}
