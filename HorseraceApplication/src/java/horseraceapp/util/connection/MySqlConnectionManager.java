package horseraceapp.util.connection;

import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.apache.log4j.Logger;

public class MySqlConnectionManager extends ConnectionManager {
    private static final String JAVA_COMP_ENV = "java:comp/env";
    private static final String CONNECTION_POOL_NAME = "jdbc/HorseracePool";
    private Context initContext;
    private Context envContext;
    private DataSource dataSource;
    private static MySqlConnectionManager instance;
    
    private MySqlConnectionManager() {
        try {
            initContext = new InitialContext();
            envContext = (Context) initContext.lookup(JAVA_COMP_ENV);
            dataSource = (DataSource) envContext.lookup(CONNECTION_POOL_NAME);
        } catch (NamingException ex) {
            Logger log = Logger.getLogger(MySqlConnectionManager.class);
            log.error("Error while creating DataSource");
        }
    }

    /**
     * Static method to get single instance of this object.
     *
     * @return instance of MySQL connection manager
     */
    public static MySqlConnectionManager getInstance() {
        if (instance == null) {
            instance = new MySqlConnectionManager();
        }
        return instance;
    }

    @Override
    public Connection getConnection() {
        Connection connection = null;

        try {
            connection = dataSource.getConnection();
        } catch (SQLException ex) {
            Logger log = Logger.getLogger(MySqlConnectionManager.class);
            log.error("Can't get connection from DataSource");   
        }

        return connection;
    }

}
