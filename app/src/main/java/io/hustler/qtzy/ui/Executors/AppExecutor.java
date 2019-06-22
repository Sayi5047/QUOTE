package io.hustler.qtzy.ui.Executors;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppExecutor {
    private static AppExecutor mAppExecutorinstance;
    private static final Object LOCK = new Object();
    private Executor networkExecutor;
    private Executor fileExecutor;
    private Executor diskExecutor;
    private Executor mainThreadExecutor;

    private AppExecutor(Executor networkExecutor, Executor diskExecutor, Executor mainThreadExecutor, Executor fileExecutor) {
        this.networkExecutor = networkExecutor;
        this.diskExecutor = diskExecutor;
        this.mainThreadExecutor = mainThreadExecutor;
        this.fileExecutor = fileExecutor;
    }

    public static AppExecutor getInstance() {
        if (mAppExecutorinstance == null) {
            synchronized (LOCK) {
                mAppExecutorinstance = new AppExecutor(Executors.newFixedThreadPool(3),
                        Executors.newSingleThreadExecutor(), new MainThreadExecutor(), Executors.newSingleThreadExecutor());
            }
        }
        return mAppExecutorinstance;
    }

    public static class MainThreadExecutor implements Executor {
        Handler handler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            handler.post(command);
        }
    }

    public Executor getNetworkExecutor() {
        return networkExecutor;
    }

    public Executor getFileExecutor() {
        return fileExecutor;
    }

    public Executor getDiskExecutor() {
        return diskExecutor;
    }

    public Executor getMainThreadExecutor() {
        return mainThreadExecutor;
    }
}
