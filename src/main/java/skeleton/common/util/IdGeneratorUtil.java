package skeleton.common.util;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Beldon
 */
public class IdGeneratorUtil {
    private final AtomicLong lastTimeContainer = new AtomicLong(0);
    private final AtomicLong countContainer = new AtomicLong();

    private static IdGeneratorUtil instance = new IdGeneratorUtil();

    private IdGeneratorUtil() {

    }

    public static IdGeneratorUtil getInstance() {
        return instance;
    }

    public synchronized String generateId() {
        long currentTime = System.currentTimeMillis() / 1000;

        long lastTime = lastTimeContainer.getAndSet(currentTime);

        long count;
        if (lastTime == currentTime) {
            count = countContainer.incrementAndGet();
        } else {
            countContainer.set(0);
            count = 0;
        }
        StringBuilder codeStr = new StringBuilder().append(currentTime);
        if (count != 0) {
            codeStr.append(countContainer);
        }
        long code = Long.parseLong(codeStr.toString());
        return Long.toString(code, 36);
    }
}
