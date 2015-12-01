package com.kifile.android.utils;

import android.support.annotation.NonNull;

import java.util.Comparator;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 异步线程池，建议所有新开线程都通过这里进行execute执行，避免重复创建线程.
 * <p/>
 * Created by zhouyi on 15/5/14.
 */
public class WorkerThreadPool implements Executor {

    private static final int THREAD_NUM = Runtime.getRuntime().availableProcessors();

    private static final WorkerThreadPool sInstance = new WorkerThreadPool();

    private final Executor mExecutor;

    private WorkerThreadPool() {
        mExecutor = new ThreadPoolExecutor(THREAD_NUM, THREAD_NUM, 0L, TimeUnit.MILLISECONDS,
                new PriorityBlockingQueue<>(THREAD_NUM, new PriorityRunnableComparator()), Executors.defaultThreadFactory());
    }

    public static WorkerThreadPool getInstance() {
        return sInstance;
    }

    @Override
    public void execute(@NonNull Runnable command) {
        mExecutor.execute(command);
    }

    /**
     * 拥有优先级的Runnable对象.
     */
    public interface PriorityRunnable extends Runnable {

        /**
         * 获取当前的优先级.
         *
         * @return
         */
        int getPriority();
    }

    /**
     * 任务优先级计算工具类.
     */
    public class PriorityRunnableComparator implements Comparator<Runnable> {

        @Override
        public int compare(Runnable lhs, Runnable rhs) {
            int lPriority = 0;
            int rPriority = 0;
            if (lhs instanceof PriorityRunnable) {
                lPriority = ((PriorityRunnable) lhs).getPriority();
            }
            if (rhs instanceof PriorityRunnable) {
                rPriority = ((PriorityRunnable) rhs).getPriority();
            }
            return rPriority - lPriority;
        }
    }

}
