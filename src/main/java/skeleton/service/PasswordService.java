package skeleton.service;

/**
 * @author Beldon
 */
public interface PasswordService {
    String encode(String password);

    boolean checkPassword(String raw, String encodePass);
}
