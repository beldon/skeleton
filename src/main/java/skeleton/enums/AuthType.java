package skeleton.enums;

import lombok.Getter;

/**
 * @author Beldon
 */
@Getter
public enum AuthType {

    /**
     * guest
     */
    GUEST(true, false, false),

    REMEMBER_ME(false, true, false),

    AUTHENTICATED(false, false, true);

    private final boolean guest;
    private final boolean rememberMe;
    private final boolean authenticated;

    AuthType(boolean guest, boolean rememberMe, boolean authenticated) {
        this.guest = guest;
        this.rememberMe = rememberMe;
        this.authenticated = authenticated;
    }}
