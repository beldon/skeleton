package skeleton.common.util;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Beldon.
 */
public class IpUtils {

    private static final String UNKNOWN = "unknown";
    private static final String HEADER_PROXY_CLIENT_IP = "Proxy-Client-IP";
    private static final String HEADER_WL_PROXY_CLIENT_IP = "WL-Proxy-Client-IP";
    private static final String HEADER_X_REAL_IP = "X-Real-IP";

    private IpUtils() {

    }

    public static String getRequestRealIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0];
        }

        if (!isTrueIp(ip)) {
            ip = request.getHeader(HEADER_PROXY_CLIENT_IP);
        }
        if (!isTrueIp(ip)) {
            ip = request.getHeader(HEADER_WL_PROXY_CLIENT_IP);
        }
        if (!isTrueIp(ip)) {
            ip = request.getHeader(HEADER_X_REAL_IP);
        }

        if (!isTrueIp(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    private static boolean isTrueIp(String ip) {
        return ip != null && ip.length() != 0 && !UNKNOWN.equalsIgnoreCase(ip);
    }
}
