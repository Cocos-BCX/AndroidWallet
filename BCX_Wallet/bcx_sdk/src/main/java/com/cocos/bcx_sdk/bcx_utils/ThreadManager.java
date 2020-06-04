package com.cocos.bcx_sdk.bcx_utils;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadManager {

    private static class ThreadPollProxyInstanceHolder {
        static final ThreadPollProxy INSTANCE = new ThreadPollProxy(6, 10, 2000);
    }

    public static ThreadPollProxy getThreadPollProxy() {
        return ThreadManager.ThreadPollProxyInstanceHolder.INSTANCE;
    }

    public static class ThreadPollProxy {
        private ThreadPoolExecutor poolExecutor;
        private int corePoolSize;
        private int maximumPoolSize;
        private long keepAliveTime;

        ThreadPollProxy(int corePoolSize, int maximumPoolSize, long keepAliveTime) {
            this.corePoolSize = corePoolSize;
            this.maximumPoolSize = maximumPoolSize;
            this.keepAliveTime = keepAliveTime;
        }

        public void execute(Runnable r) {
            if (poolExecutor == null || poolExecutor.isShutdown()) {
                poolExecutor = new ThreadPoolExecutor(

                        corePoolSize,

                        maximumPoolSize,

                        keepAliveTime,

                        TimeUnit.MILLISECONDS,

                        new LinkedBlockingQueue<Runnable>(),

                        Executors.defaultThreadFactory());
            }
            poolExecutor.execute(r);
        }
    }
}