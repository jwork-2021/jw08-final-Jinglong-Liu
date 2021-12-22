package app.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolUtil {
    public static ExecutorService exec = Executors.newCachedThreadPool();
    public static void execute(Runnable runnable){
        exec.execute(runnable);
    }
}
