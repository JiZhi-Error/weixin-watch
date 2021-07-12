/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  android.os.Binder
 *  android.os.Handler
 *  android.os.Looper
 *  android.os.Message
 *  android.os.Process
 *  android.util.Log
 */
package android.support.v4.content;

import android.os.Binder;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

abstract class ModernAsyncTask<Params, Progress, Result> {
    private static final int CORE_POOL_SIZE = 5;
    private static final int KEEP_ALIVE = 1;
    private static final String LOG_TAG = "AsyncTask";
    private static final int MAXIMUM_POOL_SIZE = 128;
    private static final int MESSAGE_POST_PROGRESS = 2;
    private static final int MESSAGE_POST_RESULT = 1;
    public static final Executor THREAD_POOL_EXECUTOR;
    private static volatile Executor sDefaultExecutor;
    private static InternalHandler sHandler;
    private static final BlockingQueue<Runnable> sPoolWorkQueue;
    private static final ThreadFactory sThreadFactory;
    private final FutureTask<Result> mFuture;
    private volatile Status mStatus = Status.PENDING;
    final AtomicBoolean mTaskInvoked = new AtomicBoolean();
    private final WorkerRunnable<Params, Result> mWorker = new WorkerRunnable<Params, Result>(){

        @Override
        public Result call() throws Exception {
            Object Result2;
            ModernAsyncTask.this.mTaskInvoked.set(true);
            Object Result3 = Result2 = null;
            try {
                Process.setThreadPriority((int)10);
                Result3 = Result2;
            }
            catch (Throwable throwable) {
                ModernAsyncTask.this.postResult(Result3);
                throw throwable;
            }
            Result3 = Result2 = (Object)ModernAsyncTask.this.doInBackground(this.mParams);
            Binder.flushPendingCommands();
            ModernAsyncTask.this.postResult(Result2);
            return Result2;
        }
    };

    static {
        sThreadFactory = new ThreadFactory(){
            private final AtomicInteger mCount = new AtomicInteger(1);

            @Override
            public Thread newThread(Runnable runnable) {
                return new Thread(runnable, "ModernAsyncTask #" + this.mCount.getAndIncrement());
            }
        };
        sPoolWorkQueue = new LinkedBlockingQueue<Runnable>(10);
        sDefaultExecutor = THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(5, 128, 1L, TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory);
    }

    public ModernAsyncTask() {
        this.mFuture = new FutureTask<Result>(this.mWorker){

            @Override
            protected void done() {
                try {
                    Object v2 = this.get();
                    ModernAsyncTask.this.postResultIfNotInvoked(v2);
                    return;
                }
                catch (InterruptedException interruptedException) {
                    Log.w((String)ModernAsyncTask.LOG_TAG, (Throwable)interruptedException);
                    return;
                }
                catch (ExecutionException executionException) {
                    throw new RuntimeException("An error occurred while executing doInBackground()", executionException.getCause());
                }
                catch (CancellationException cancellationException) {
                    ModernAsyncTask.this.postResultIfNotInvoked(null);
                    return;
                }
                catch (Throwable throwable) {
                    throw new RuntimeException("An error occurred while executing doInBackground()", throwable);
                }
            }
        };
    }

    public static void execute(Runnable runnable) {
        sDefaultExecutor.execute(runnable);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static Handler getHandler() {
        synchronized (ModernAsyncTask.class) {
            if (sHandler != null) return sHandler;
            sHandler = new InternalHandler();
            return sHandler;
        }
    }

    public static void setDefaultExecutor(Executor executor) {
        sDefaultExecutor = executor;
    }

    public final boolean cancel(boolean bl2) {
        return this.mFuture.cancel(bl2);
    }

    protected abstract Result doInBackground(Params ... var1);

    public final ModernAsyncTask<Params, Progress, Result> execute(Params ... ParamsArray) {
        return this.executeOnExecutor(sDefaultExecutor, ParamsArray);
    }

    /*
     * Unable to fully structure code
     */
    public final ModernAsyncTask<Params, Progress, Result> executeOnExecutor(Executor var1_1, Params ... var2_2) {
        if (this.mStatus == Status.PENDING) ** GOTO lbl-1000
        switch (4.$SwitchMap$android$support$v4$content$ModernAsyncTask$Status[this.mStatus.ordinal()]) {
            default: lbl-1000:
            // 2 sources

            {
                this.mStatus = Status.RUNNING;
                this.onPreExecute();
                this.mWorker.mParams = var2_2;
                var1_1.execute(this.mFuture);
                return this;
            }
            case 1: {
                throw new IllegalStateException("Cannot execute task: the task is already running.");
            }
            case 2: 
        }
        throw new IllegalStateException("Cannot execute task: the task has already been executed (a task can be executed only once)");
    }

    /*
     * Enabled aggressive block sorting
     */
    void finish(Result Result2) {
        if (this.isCancelled()) {
            this.onCancelled(Result2);
        } else {
            this.onPostExecute(Result2);
        }
        this.mStatus = Status.FINISHED;
    }

    public final Result get() throws InterruptedException, ExecutionException {
        return this.mFuture.get();
    }

    public final Result get(long l2, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
        return this.mFuture.get(l2, timeUnit);
    }

    public final Status getStatus() {
        return this.mStatus;
    }

    public final boolean isCancelled() {
        return this.mFuture.isCancelled();
    }

    protected void onCancelled() {
    }

    protected void onCancelled(Result Result2) {
        this.onCancelled();
    }

    protected void onPostExecute(Result Result2) {
    }

    protected void onPreExecute() {
    }

    protected void onProgressUpdate(Progress ... ProgressArray) {
    }

    Result postResult(Result Result2) {
        ModernAsyncTask.getHandler().obtainMessage(1, new AsyncTaskResult<Object>(this, Result2)).sendToTarget();
        return Result2;
    }

    void postResultIfNotInvoked(Result Result2) {
        if (!this.mTaskInvoked.get()) {
            this.postResult(Result2);
        }
    }

    protected final void publishProgress(Progress ... ProgressArray) {
        if (!this.isCancelled()) {
            ModernAsyncTask.getHandler().obtainMessage(2, new AsyncTaskResult<Progress>(this, ProgressArray)).sendToTarget();
        }
    }

    private static class AsyncTaskResult<Data> {
        final Data[] mData;
        final ModernAsyncTask mTask;

        AsyncTaskResult(ModernAsyncTask modernAsyncTask, Data ... DataArray) {
            this.mTask = modernAsyncTask;
            this.mData = DataArray;
        }
    }

    private static class InternalHandler
    extends Handler {
        public InternalHandler() {
            super(Looper.getMainLooper());
        }

        public void handleMessage(Message message) {
            AsyncTaskResult asyncTaskResult = (AsyncTaskResult)message.obj;
            switch (message.what) {
                default: {
                    return;
                }
                case 1: {
                    asyncTaskResult.mTask.finish(asyncTaskResult.mData[0]);
                    return;
                }
                case 2: 
            }
            asyncTaskResult.mTask.onProgressUpdate(asyncTaskResult.mData);
        }
    }

    public static enum Status {
        PENDING,
        RUNNING,
        FINISHED;

    }

    private static abstract class WorkerRunnable<Params, Result>
    implements Callable<Result> {
        Params[] mParams;

        WorkerRunnable() {
        }
    }
}

