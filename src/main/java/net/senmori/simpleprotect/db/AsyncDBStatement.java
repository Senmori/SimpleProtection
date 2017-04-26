package net.senmori.simpleprotect.db;

import java.sql.SQLException;
import net.senmori.simpleprotect.util.Log;

public abstract class AsyncDBStatement {
    protected String query;
    private boolean done = false;

    public AsyncDBStatement() {
        queue(null);
    }

    public AsyncDBStatement(String query) {
        queue(query);
    }

    /**
     * Schedules this async statement to run on anther thread. This is the only method that should be
     * called on the main thread and it should only be called once.
     *
     * @param query
     */
    private void queue(final String query) {
        this.query = query;
        AsyncDBQueue.queue(this);
    }

    /**
     * Implement this method with your code that does Async SQL logic.
     *
     * @param statement
     *
     * @throws SQLException
     */
    protected abstract void run(DBStatement statement) throws SQLException;

    /**
     * Override this event to have special logic for when an exception is fired.
     *
     * @param e
     */
    public void onError(SQLException e) {
        Log.printException("Exception in AsyncDbStatement" + query, e);
    }

    public void process(DBStatement stm) throws SQLException {
        synchronized(this) {
            if(! done) {
                if(query != null) {
                    stm.query(query);
                }
                run(stm);
                done = true;
            }
        }
    }
}
