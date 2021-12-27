package app.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class ThreadPoolUtilTest {
    Runnable[] runners;
    static volatile int count = 0;
    @Before
    public void setUp() throws Exception {
        runners = new Runnable[20];
        for(int i = 0;i< runners.length;i++){
            runners[i] = new Runnable() {
                @Override
                public void run() {
                    count++;
                }
            };
        }
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void execute() {
        for(int i = 0;i< runners.length;i++){
            ThreadPoolUtil.execute(runners[i]);
        }
        try {
            TimeUnit.MILLISECONDS.sleep(1000);
        }catch (Exception e){

        }
        assertEquals(count,runners.length);
    }
}