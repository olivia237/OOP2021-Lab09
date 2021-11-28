package it.unibo.oop.lab.workers02;

import java.util.ArrayList;
import java.util.List;


/** 
 * 
 * Class multiThread to 
 * count matrix elements
 * 
 */
public class MultiThreadedSumMatrix implements SumMatrix {

    private final int n;
    
    public MultiThreadedSumMatrix(final int n) {
        this.n=n;
    }
    
    
    private static class Worker extends Thread{
        private final double[][]matrix;
        private final int startpos;
        private final int nelem;
        private long res;


    Worker(final double[][]matrix, final int startpos, final int nelem) {
        super();
        this.matrix = matrix;
        this.startpos = startpos;
        this.nelem = nelem;
    }

    @Override
    public void run() {
        for (int i = startpos; i < matrix.length && i < startpos + nelem; i++) {
            for (final double d: this.matrix[i]) {
            this.res += d;
        }
        }
    }

    /**
     * Returns the result of summing up the integers within the list.
     * 
     * @return the sum of every element in the array
     */
    public long getResult() {
        return this.res;
    }

}

@Override
public double sum(final double[][] matrix) {
    final int size = matrix.length / n + matrix.length % n;
    /*
     * Build a list of workers
     */
    final List<Worker> workers = new ArrayList<>(n);
    for (int start = 0; start < matrix.length; start += size) {
        workers.add(new Worker(matrix, start, size));
    }
    /*
     * Start them
     */
    for (final Worker w: workers) {
        w.start();
    }
    /*
     * Wait for every one of them to finish. This operation is _way_ better done by
     * using barriers and latches, and the whole operation would be better done with
     * futures.
     */
    long sum = 0;
    for (final Worker w: workers) {
        try {
            w.join();
            sum += w.getResult();
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }
    /*
     * Return the sum
     */
    return sum;
}
}
