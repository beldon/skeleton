package skeleton.web.security;

/**
 * @author Beldon
 */
public class AuthenticationContextHolder {
    private static final ThreadLocal<Authentication> CONTEXT_HOLDER = new ThreadLocal<>();

    public static Authentication getAuthentication() {
        return CONTEXT_HOLDER.get();
    }

    public static void setAuthentication(Authentication authentication) {
        CONTEXT_HOLDER.set(authentication);
    }

    public static void removeAuthentication() {
        CONTEXT_HOLDER.remove();
    }
}
