package org.servlet.museum_management.DAO;

import org.servlet.museum_management.model.Museum;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MuseumDAO extends AbstractDAO {

    public MuseumDAO(String jdbcURL, String jdbcUsername, String jdbcPassword) {
        super(jdbcURL, jdbcUsername, jdbcPassword);
    }

    private static final Logger logger = Logger.getLogger(MuseumDAO.class.getName());

    private static Museum convertMuseum(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        String description = resultSet.getString("description");
        String address = resultSet.getString("address");
        Time TimeOpening = resultSet.getTime("TimeOpening");
        Time TimeClosing = resultSet.getTime("TimeClosing");

        Museum museum = new Museum(id, name, description, address, TimeOpening, TimeClosing);
        return museum;
    }

    public boolean insertMuseum(Museum museum) throws SQLException {
        String sql = "INSERT INTO Museum (Name, Description, Address, TimeOpening, TimeClosing) VALUES (?, ?, ?, ?, ?)";
        Connection connection = null;
        PreparedStatement statement = null;
        boolean rowInserted = false;

        logger.info("Inserting museum: " + museum.getName());

        try {
            connection = getConnection();
            connection.setAutoCommit(false); // Начало транзакции

            statement = connection.prepareStatement(sql);
            statement.setString(1, museum.getName());
            statement.setString(2, museum.getDescription());
            statement.setString(3, museum.getAddress());
            statement.setTime(4, museum.getTimeOpening());
            statement.setTime(5, museum.getTimeClosing());

            rowInserted = statement.executeUpdate() > 0;
            connection.commit(); // Подтверждение транзакции

            logger.info("Museum inserted: " + museum.getName());
        } catch (SQLException e) {
            if (connection != null) {
                connection.rollback(); // Откат транзакции в случае ошибки
            }
            logger.log(Level.SEVERE, "Error inserting museum", e);
            e.printStackTrace();
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.setAutoCommit(true); // Восстановление автокоммита
                connection.close();
            }
        }

        return rowInserted;
    }

    public List<Museum> listAllMuseums() throws SQLException {
        List<Museum> listMuseum = new ArrayList<>();
        String sql = "SELECT * FROM MuseumWithExhibitionCount";

        logger.info("Listing all museums");

        try (Connection connection = getConnection()) {
            try (Statement statement = connection.createStatement()) {
                try (ResultSet resultSet = statement.executeQuery(sql)) {
                    while (resultSet.next()) {
                        Museum museum = convertMuseum(resultSet);
                        museum.setExhibitionCount(resultSet.getInt("ExhibitionCount"));
                        listMuseum.add(museum);
                    }
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error listing all museums", e);
            throw e;
        }

        return listMuseum;
    }

    public boolean deleteMuseum(Museum museum) throws SQLException {
        String sql = "DELETE FROM museum where id = ?";
        Connection connection = null;
        PreparedStatement statement = null;
        boolean rowDeleted = false;

        logger.info("Deleting museum: " + museum.getName());

        try {
            connection = getConnection();
            connection.setAutoCommit(false); // Начало транзакции

            statement = connection.prepareStatement(sql);
            statement.setInt(1, museum.getId());

            rowDeleted = statement.executeUpdate() > 0;
            connection.commit(); // Подтверждение транзакции

            logger.info("Museum deleted: " + museum.getName());
        } catch (SQLException e) {
            if (connection != null) {
                connection.rollback(); // Откат транзакции в случае ошибки
            }
            logger.log(Level.SEVERE, "Error deleting museum", e);
            e.printStackTrace();
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.setAutoCommit(true); // Восстановление автокоммита
                connection.close();
            }
        }

        return rowDeleted;
    }

    public boolean updateMuseum(Museum museum) throws SQLException {
        String sql = "UPDATE museum SET name = ?, description = ?, address = ?, TimeOpening = ?, TimeClosing = ? WHERE id = ?";
        Connection connection = null;
        PreparedStatement statement = null;
        boolean rowUpdated = false;

        logger.info("Updating museum: " + museum.getName());

        try {
            connection = getConnection();
            connection.setAutoCommit(false); // Начало транзакции

            statement = connection.prepareStatement(sql);
            statement.setString(1, museum.getName());
            statement.setString(2, museum.getDescription());
            statement.setString(3, museum.getAddress());
            statement.setTime(4, museum.getTimeOpening());
            statement.setTime(5, museum.getTimeClosing());
            statement.setInt(6, museum.getId());

            rowUpdated = statement.executeUpdate() > 0;
            connection.commit(); // Подтверждение транзакции

            logger.info("Museum updated: " + museum.getName());
        } catch (SQLException e) {
            if (connection != null) {
                connection.rollback(); // Откат транзакции в случае ошибки
            }
            logger.log(Level.SEVERE, "Error updating museum", e);
            e.printStackTrace();
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.setAutoCommit(true); // Восстановление автокоммита
                connection.close();
            }
        }

        return rowUpdated;
    }

    public Museum getMuseum(int id) throws SQLException {
        Museum museum = null;
        String sql = "SELECT * FROM museum WHERE id = ?";

        logger.info("Getting museum with id: " + id);

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                museum = convertMuseum(resultSet);
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error getting museum with id: " + id, e);
            throw e;
        }

        return museum;
    }
}
