package org.example.sync.completableFuture;

import java.util.concurrent.*;
import java.util.function.Supplier;

public class CompletableFutureTest {

    private static ExecutorService executorService = Executors.newFixedThreadPool(3, new ThreadFactory() {
        int count = 0;
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "current_thread_" + count++);
        }
    });

    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        runAsyncExample();
//        thenApplyExample();
//        thenApplySyncExample();
//        thenApplySyncWithExecutorExample();
//        thenAcceptExample();
//        thenAcceptSyncExample();
//        applyToEitherExample();
//        runAfterBothExample();
        thenCombineExample();
    }

    /**
     * 异步执行
     */
    static void runAsyncExample() {
        CompletableFuture cf = CompletableFuture.runAsync(() -> {
            System.out.println(Thread.currentThread().isDaemon());
        });
        System.out.println(cf.isDone());
    }

    /**
     *
     * 1，第一步执行的结果作为第二步方法的入参
     * getString() 作为 toUpperCase 的入参，两步方法同步执行
     *
     * 2，get() 阻塞式返回  getNow() 非阻塞式返回
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    static void thenApplyExample() throws ExecutionException, InterruptedException {
        CompletableFuture<String> completedFuture = CompletableFuture.completedFuture(getString()).thenApply(String::toUpperCase);
        System.out.println(completedFuture.get());
        System.out.println(completedFuture.getNow(null));
    }

    /**
     * 两步方法异步执行，总耗时约为2S，若使用thenApply，总耗时为4S
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    static void thenApplySyncExample() throws ExecutionException, InterruptedException {
        long l = System.currentTimeMillis();
        System.out.println("处理开始,时间：" + l);
        CompletableFuture<String> completedFuture = CompletableFuture.completedFuture(getString()).thenApplyAsync(string -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return string.toUpperCase();
        });
        System.out.println("处理结束,时间：" + (System.currentTimeMillis() - l));

        long l1 = System.currentTimeMillis();
        System.out.println("获取结果耗时，get()开始：" + l1);
        /**
         * 测试get()和getNow()的获取结果时间比较
         * get()耗时2S，基本等于任务执行完就立刻拿到结果
         * getNow()耗时0S，但是返回的为null，确实是非阻塞->_->，具体用法有待探讨
         */
        System.out.println(completedFuture.get());
//        System.out.println(completedFuture.getNow(null));
        System.out.println("获取结果耗时，get()结束：" + (System.currentTimeMillis() - l1));
    }



    /**
     * 使用自定义线程池执行异步
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    static void thenApplySyncWithExecutorExample() throws ExecutionException, InterruptedException {
        long l = System.currentTimeMillis();
        System.out.println("处理开始,时间：" + l);
        CompletableFuture<String> completedFuture = CompletableFuture.completedFuture(getString()).thenApplyAsync(string -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return string.toUpperCase();
        }, executorService);
        System.out.println("处理结束,时间：" + (System.currentTimeMillis() - l));

        /**
         * get()和join()本质都是返回结果
         * 区别：
         * 前者  抛出的是经过检查的异常，ExecutionException, InterruptedException 需要用户手动处理（抛出或者 try catch）
         * 后者  抛出的是uncheck异常（即未经检查的异常),不会强制开发者抛出，
         *      会将异常包装成CompletionException异常 /CancellationException异常，但是本质原因还是代码内存在的真正的异常
         */
        System.out.println(completedFuture.get());
        System.out.println(completedFuture.join());
    }

    /**
     * 同步执行 不需要调用join or get
     * 如果下一阶段接收了当前阶段的结果，但是在计算的时候不需要返回值(它的返回类型是void)，
     * 那么它可以不应用一个函数，而是一个消费者， 调用方法也变成了thenAccept
     */
    static void thenAcceptExample() {
        StringBuilder result = new StringBuilder();
        CompletableFuture.completedFuture(getStringWithoutDelay())
                .thenAccept(s -> result.append(s));
        System.out.println(result.toString());
    }

    /**
     * 异步执行 需要join or get
     * 总耗时：2S 对比非同步就需要4S（将thenAcceptAsync -> thenAccept）
     */
    static void thenAcceptSyncExample() throws InterruptedException {
        long l = System.currentTimeMillis();
        System.out.println("处理开始,时间：" + l);
        StringBuilder result = new StringBuilder();
        CompletableFuture<Void> voidCompletableFuture = CompletableFuture.completedFuture(getString())
                .thenAcceptAsync(s -> {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    result.append(s);
                });
        System.out.println("处理结束,时间：" + (System.currentTimeMillis() - l));
        voidCompletableFuture.join();
        System.out.println(result.toString());
    }

    /**
     *  创建两个任务，cf1，cf2，二者用applyToEither相关联，返回其中执行最快的结果
     */
    static void applyToEitherExample() {
        String original = "Message";
        CompletableFuture cf1 = CompletableFuture.completedFuture(original)
                .thenApplyAsync(string -> {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return string.toLowerCase();
                });
        CompletableFuture cf2 = cf1.applyToEither(
                CompletableFuture.completedFuture(original).thenApplyAsync(string -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return string.toUpperCase();
                }),
                s -> s + "最快返回！");
        System.out.println(cf2.join());
    }

    /**
     * 在两个阶段都执行完后运行一个Runnable
     * eg:所有操作完成记录时间等
     */
    static void runAfterBothExample() {
        String original = "Message";
        StringBuilder result = new StringBuilder();
        CompletableFuture.completedFuture(original).thenApply(String::toUpperCase).runAfterBoth(
                CompletableFuture.completedFuture(original).thenApply(String::toLowerCase),
                () -> result.append("done"));
        System.out.println(result.toString());
    }

    /**
     * thenCombine关联两个阶段并组合最终返回
     */
    static void thenCombineExample() {
        String original = "Message";
        CompletableFuture cf = CompletableFuture.completedFuture(original).thenApply(String::toUpperCase)
                .thenCombine(CompletableFuture.completedFuture(original).thenApply(String::toLowerCase),
                        (s1, s2) -> s1 + s2)
                .thenCombine(CompletableFuture.completedFuture(original).thenApply(String::toLowerCase),(s1, s2) -> s1 + s2);
        System.out.println(cf.join());
    }

    static String getStringWithoutDelay() {
        return "completedFuture";
    }

    static String getString() throws InterruptedException {
        Thread.sleep(2000);
        return "completedFuture";
    }
}
