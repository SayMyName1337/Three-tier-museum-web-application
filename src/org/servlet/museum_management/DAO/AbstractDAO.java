package org.servlet.museum_management.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractDAO {
    private String jdbcURL;
    private String jdbcUsername;
    private String jdbcPassword;
	
	private static final Logger LOGGER = Logger.getLogger(AbstractDAO.class.getName());

    public AbstractDAO(String jdbcURL, String jdbcUsername, String jdbcPassword) {
        this.jdbcURL = jdbcURL;
        this.jdbcUsername = jdbcUsername;
        this.jdbcPassword = jdbcPassword;
    }

    protected Connection getConnection() throws SQLException {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			LOGGER.log(Level.INFO, "===== Connection established successfully! =====");
        } catch (ClassNotFoundException e) {
			LOGGER.log(Level.SEVERE, "===== Failed to establish connection! =====", e);
            throw new SQLException(e);
        }
        return DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
    }
}
