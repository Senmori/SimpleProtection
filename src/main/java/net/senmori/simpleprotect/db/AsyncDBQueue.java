package net.senmori.simpleprotect.db;

import java.sql.SQLException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import net.senmori.simpleprotect.util.Log;

public class AsyncDBQueue implements Runnable {
    private static final Queue<AsyncDBStatement> queue = new ConcurrentLinkedQueue<>();
    private static final Lock lock = new ReentrantLock();

    @Override
    public void run() {
        processQueue();
    }

    public static void processQueue() {
        if(queue.isEmpty() || ! lock.tryLock()) {
            return;
        }

        AsyncDBStatement stm = null;
        DBStatement dbStatement;

        try {
            dbStatement = new DBStatement();
        } catch(Exception e) {
            lock.unlock();
            Log.printException("Exception getting DbStatement in AsyncDbQueue", e);
            return;
        }

        while(( stm = queue.poll() ) != null) {
            try {
                if(dbStatement.isClosed()) {
                    dbStatement = new DBStatement();
                }
                stm.process(dbStatement);
            } catch(SQLException e) {
                stm.onError(e);
            }
        }
        dbStatement.close();
        lock.unlock();
    }

    public static boolean queue(AsyncDBStatement stm) {
        return queue.offer(stm);
    }
}
