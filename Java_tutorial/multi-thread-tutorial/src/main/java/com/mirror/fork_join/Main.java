package com.mirror.fork_join;

import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * @author mirror
 */
public class Main {
    public static void main(String[] args) {
        //创建两千个随机数组成的数组
        long[] array = new long[2000];
        long expectedSum = 0;
        for (int i = 0; i < array.length; i++) {
            array[i] = random();
            expectedSum += array[i];
        }
        System.out.println("expected sun: " + expectedSum);
        //fork/join
        ForkJoinTask<Long> task = new SumTask(array, 0, array.length);
        //这两个只是计算话费时间的 ，不用管
        long startTime = System.currentTimeMillis();
        Long result = ForkJoinPool.commonPool().invoke(task);
        long endTime = System.currentTimeMillis();
        System.out.println("Fork/join sum: " + result + "in " + (endTime - startTime) + "ms.");


    }
    //获取随机数的
    static Random random = new Random(0);

    static long random() {
        return random.nextInt(10000);
    }
}
//重点是这个
class SumTask extends RecursiveTask<Long> {
    static final int THRESHOLD = 500;
    long[] array;
    int start;
    int end;

    SumTask(long[] array, int start, int end) {
        this.array = array;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {
        //如果达到最小可计算的数组的话，开始计算
        if (end - start == THRESHOLD) {
            long sum = 0;
            //这就是在五百的范围内进行求和
            for (int i = start; i < end; i++) {
                sum += this.array[i];
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            return sum;
        }
        // 任务太大,一分为二:
        int middle = (end + start) / 2;
        System.out.println(String.format("split %d~%d ==> %d~%d, %d~%d", start, end, start, middle, middle, end));
        //递归调用自身
        SumTask subtask1 = new SumTask(this.array, start, middle);
        SumTask subtask2 = new SumTask(this.array, middle, end);
        //并行运行两个子任务
        invokeAll(subtask1, subtask2);
        //获取两个子任务的结果
        Long subresult1 = subtask1.join();
        Long subresult2 = subtask2.join();
        Long result = subresult1 + subresult2;
        System.out.println("result = " + subresult1 + " + " + subresult2 + " ==> " + result);
        return result;
    }
}
