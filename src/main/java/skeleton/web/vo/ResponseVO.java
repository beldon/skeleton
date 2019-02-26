package skeleton.web.vo;

import lombok.Getter;

/**
 * @author Beldon.
 */
@Getter
public class ResponseVO<T> {
    private final Integer code;
    private final String msg;
    private final T data;

    private ResponseVO(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static ResponseVO success() {
        return success(null);
    }

    public static ResponseVO success(String msg) {
        return success(msg, null);
    }

    public static <T> ResponseVO<T> successWithData(T data) {
        return response(0, null, data);
    }

    public static <T> ResponseVO success(String msg, T data) {
        return response(0, msg, data);
    }

    public static ResponseVO error(String msg) {
        return response(1, msg, null);
    }

    public static <T> ResponseVO<T> response(int code, String msg, T data) {
        return new ResponseVO<>(code, msg, data);
    }
}
