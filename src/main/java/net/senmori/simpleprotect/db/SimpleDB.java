package net.senmori.simpleprotect.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import net.senmori.simpleprotect.SimpleProtect;
import net.senmori.simpleprotect.util.Log;

public class SimpleDB {
    public static HikariDataSource hikariDataSource;

    private SimpleDB() {
    }

    public static void init() {

        try {
            HikariConfig config = new HikariConfig();
            String database = "SimpleProtectDB";
            File dataBaseFile = new File(SimpleProtect.getInstance().getDataFolder(), "sql" + File.separatorChar + database + ".db3");

            config.setPoolName("SimpleDBSQLitePool");
            config.setDriverClassName("org.sqlite.JDBC");
            config.setJdbcUrl("jdbc:sqlite:" + dataBaseFile);
            config.addDataSourceProperty("cachePrepStmts", true);
            config.addDataSourceProperty("prepStmtCacheSize", 250);
            config.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
            config.addDataSourceProperty("useServerPrepStmts", true);
            config.addDataSourceProperty("cacheCallableStmts", true);
            config.addDataSourceProperty("cacheResultSetMetadata", true);
            config.addDataSourceProperty("cacheServerConfiguration", true);
            config.addDataSourceProperty("useLocalSessionState", true);
            config.addDataSourceProperty("elideSetAutoCommits", true);
            config.addDataSourceProperty("alwaysSendSetIsolation", false);

            config.setConnectionTestQuery("SELECT 1");
            config.setMinimumIdle(3);
            config.setMaximumPoolSize(5);

            hikariDataSource = new HikariDataSource(config);
            hikariDataSource.setTransactionIsolation("TRANSACTION_READ_COMMITTED");
        } catch(Exception e) {
            hikariDataSource = null;
            Log.printException("Error connection to SQLite DB: ", e);
        }
    }

    /**
     * Initiates a new DbStatement and prepares the first query.
     * <p/>
     * YOU MUST MANUALLY CLOSE THIS STATEMENT IN A finally {} BLOCK!
     *
     * @param query
     *
     * @return
     *
     * @throws SQLException
     */
    public static DBStatement query(String query) throws SQLException {
        return ( new DBStatement() ).query(query);
    }

    /**
     * Utility method to execute a query and retrieve the first row, then close statement.
     * You should ensure result will only return 1 row for maximum performance.
     *
     * @param query  The query to run
     * @param params The parameters to execute the statement with
     *
     * @return DbRow of your results (HashMap with template return type)
     *
     * @throws SQLException
     */
    public static DBRow getFirstRow(String query, Object... params) throws SQLException {
        try(DBStatement statement = SimpleDB.query(query).execute(params)) {
            return statement.getNextRow();
        }
    }

    /**
     * Utility method to execute a query and retrieve the first column of the first row, then close statement.
     * You should ensure result will only return 1 row for maximum performance.
     *
     * @param query  The query to run
     * @param params The parameters to execute the statement with
     *
     * @return DbRow of your results (HashMap with template return type)
     *
     * @throws SQLException
     */
    public static <T> T getFirstColumn(String query, Object... params) throws SQLException {
        try(DBStatement statement = SimpleDB.query(query).execute(params)) {
            return statement.getFirstColumn();
        }
    }

    /**
     * Utility method to execute a query and retrieve first column of all results, then close statement.
     * <p>
     * Meant for single queries that will not use the statement multiple times.
     *
     * @param query
     * @param params
     * @param <T>
     *
     * @return
     *
     * @throws SQLException
     */
    public static <T> List<T> getFirstColumnResults(String query, Object... params) throws SQLException {
        List<T> dbRows = new ArrayList<>();
        T result;
        try(DBStatement statement = SimpleDB.query(query).execute(params)) {
            while(( result = statement.getFirstColumn() ) != null) {
                dbRows.add(result);
            }
        }
        return dbRows;
    }

    /**
     * Utility method to execute a query and retrieve all results, then close statement.
     * <p>
     * Meant for single queries that will not use the statement multiple times.
     *
     * @param query  The query to run
     * @param params The parameters to execute the statement with
     *
     * @return List of DbRow of your results (HashMap with template return type)
     *
     * @throws SQLException
     */
    public static List<DBRow> getResults(String query, Object... params) throws SQLException {
        try(DBStatement statement = SimpleDB.query(query).execute(params)) {
            return statement.getResults();
        }
    }

    /**
     * Utility method for executing an update synchronously, and then close the statement.
     *
     * @param query  Query to run
     * @param params Params to execute the statement with.
     *
     * @return Number of rows modified.
     *
     * @throws SQLException
     */
    public static int executeUpdate(String query, Object... params) throws SQLException {
        try(DBStatement statement = SimpleDB.query(query)) {
            return statement.executeUpdate(params);
        }
    }

    /**
     * Utility method to execute an update statement asynchronously and close the connection.
     *
     * @param query  Query to run
     * @param params Params to execute the update with
     */
    public static void executeUpdateAsync(String query, final Object... params) {
        new AsyncDBStatement(query) {
            @Override
            public void run(DBStatement statement) throws SQLException {
                statement.executeUpdate(params);
            }
        };
    }
}
