import lombok.val;
import org.junit.AssumptionViolatedException;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;

/**
 * @author bin
 * @since 2023/10/09
 */
public class CustomStopwatch extends Stopwatch {

    @Override
    protected void succeeded(long nanos, Description description) {
        logInfo(description, "\033[32m[成功]\033[0m", nanos);
    }

    @Override
    protected void failed(long nanos, Throwable e, Description description) {
        logInfo(description, "\033[31m[失败]\033[0m", nanos);
    }

    @Override
    protected void skipped(long nanos, AssumptionViolatedException e, Description description) {
        logInfo(description, "\033[33m[跳过]\033[0m", nanos);
    }

//    @Override
//    protected void finished(long nanos, Description description) {
//        logInfo(description, "\033[35m[结束]\033[0m", nanos);
//    }

    private static void logInfo(Description description, String status, long nanos) {
        String testName = description.getMethodName();
        val log = String.format("Test [%s] %s, spent %s", testName, status, toTime(nanos));
        System.out.println(log);
    }

    private static String toTime(long nanos) {
        long first = nanos;
        if (first < 1000) {
            return first + " ns";
        }
        long second = first % 1000;
        first = first / 1000;
        if (first < 1000) {
            return first + " us " + second + " ns";
        }
        second = first % 1000;
        first = first / 1000;
        if (first < 1000) {
            return first + " ms " + second + " us";
        }
        second = first % 1000;
        first = first / 1000;
        if (first < 1000) {
            return first + " s " + second + " ms";
        }
        second = first % 1000;
        first = first / 1000;
        if (first < 1000) {
            return first + " m " + second + " s";
        }
        second = first % 1000;
        first = first / 1000;
        if (first < 1000) {
            return first + " h " + second + " m";
        }
        second = first % 1000;
        first = first / 1000;
        return first + " d " + second + " h";
    }

}
