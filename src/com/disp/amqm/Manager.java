package com.disp.amqm;

 
/**
 * This is sample scenario of an intermediation 
 * platform that consumes users reporting
 * @author TORKHANI Rami
 */
public class Manager {
 
    public static void main(String[] args) throws Exception {
       // thread(new Producer(), false);
        thread(new Consumer(), false);
       //thread(new Consumer(), false);
        Thread.sleep(1000);
    }
    /**
     * this method make my manager as a daemon 
     * 	allows us to create multiple instance of consumers 
     * @param runnable
     * @param daemon
     * @author Rami
     */
    public static void thread(Runnable runnable, boolean daemon) {
        Thread brokerThread = new Thread(runnable);
        brokerThread.setDaemon(daemon);
        brokerThread.start();
    }
}
