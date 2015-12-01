package com.kifile.android.utils;

import junit.framework.TestCase;

/**
 * @author kifile
 */
public class WorkerThreadPoolTest extends TestCase {

    public void testGetInstance() throws Exception {
        for (int i = 0; i < 20; i++) {
            final int b = i;
            WorkerThreadPool.getInstance().execute(new WorkerThreadPool.PriorityRunnable() {
                @Override
                public int getPriority() {
                    return b;
                }

                @Override
                public void run() {
                    try {
                        Thread.sleep(100);
                        System.out.println(b);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        Thread.sleep(3000);
    }
}