package org.servlet.museum_management.DAO;

import org.servlet.museum_management.model.ArtObject;
import org.servlet.museum_management.model.Exhibition;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExhibitionDAO extends AbstractDAO {

    public ExhibitionDAO(String jdbcURL, String jdbcUsername, String jdbcPassword) {
        super(jdbcURL, jdbcUsername, jdbcPassword);
    }

    private static final Logger logger = Logger.getLogger(ExhibitionDAO.class.getName()); // Определяем поле логера

    public boolean insertExhibition(Exhibition exhibition) throws SQLException {
        String sql = "INSERT INTO Exhibition (Name, Description, Started, Ended, MuseumId) VALUES (?, ?, ?, ?, ?)";
        Connection connection = null;
        PreparedStatement statement = null;
        boolean rowInserted = false;

        try {
            connection = getConnection();
            connection.setAutoCommit(false); // Начало транзакции

            // Проверка существования музея
            if (!museumExists(exhibition.getMuseumId())) {
                throw new SQLException("Museum with ID " + exhibition.getMuseumId() + " does not exist.");
            }

            statement = connection.prepareStatement(sql);
            statement.setString(1, exhibition.getName());
            statement.setString(2, exhibition.getDescription());
            statement.setDate(3, exhibition.getStarted());
            statement.setDate(4, exhibition.getEnded());
            statement.setInt(5, exhibition.getMuseumId());

            rowInserted = statement.executeUpdate() > 0;
            connection.commit(); // Подтверждение транзакции

            logger.info("Exhibition inserted: " + exhibition.getName()); // Лог транзакции
        } catch (SQLException e) {
            if (connection != null) {
                connection.rollback(); // Откат транзакции в случае ошибки
            }
            logger.log(Level.SEVERE, "Error inserting exhibition", e); // Логи ошибок транзакции
            throw e; // Переброс исключения для обработки в сервлете
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

    private boolean museumExists(int museumId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Museum WHERE id = ?";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, museumId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
            return false;
        }
    }

    public List<Exhibition> listAllExhibitions(Integer museumId) throws SQLException {
        List<Exhibition> listExhibition = new ArrayList<>();
        String sql = "SELECT * FROM exhibition WHERE museumId = ?";
        logger.info("Listing all exhibitions for museumId: " + museumId);

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, museumId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Exhibition exhibition = convertExhibition(resultSet);
                listExhibition.add(exhibition);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error listing exhibitions for museumId: " + museumId, e);
            e.printStackTrace();
        }

        return listExhibition;
    }

    public List<Exhibition> listAllExhibitions() throws SQLException {
        List<Exhibition> listExhibition = new ArrayList<>();
        String sql = "SELECT * FROM exhibition";

        try (Connection connection = getConnection()) {
            ResultSet resultSet = connection.createStatement().executeQuery(sql);

            while (resultSet.next()) {
                Exhibition exhibition = convertExhibition(resultSet);
                listExhibition.add(exhibition);
            }

            resultSet.close();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error listing all exhibitions", e);
            e.printStackTrace();
        }

        return listExhibition;
    }

    public boolean deleteExhibition(Exhibition exhibition) throws SQLException {
        String sql = "DELETE FROM exhibition where id = ?";
        Connection connection = null;
        PreparedStatement statement = null;
        boolean rowDeleted = false;

        try {
            connection = getConnection();
            connection.setAutoCommit(false); // Начало транзакции

            statement = connection.prepareStatement(sql);
            statement.setInt(1, exhibition.getId());

            rowDeleted = statement.executeUpdate() > 0;
            connection.commit(); // Подтверждение транзакции

            logger.info("Exhibition deleted: " + exhibition.getId());
        } catch (SQLException e) {
            if (connection != null) {
                connection.rollback(); // Откат транзакции в случае ошибки
            }
            logger.log(Level.SEVERE, "Error deleting exhibition: " + exhibition.getId(), e);
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

    public boolean updateExhibition(Exhibition exhibition) throws SQLException {
        String sql = "UPDATE exhibition SET name = ?, description = ?, started = ?, ended = ?, museumId = ? WHERE id = ?";
        Connection connection = null;
        PreparedStatement statement = null;
        boolean rowUpdated = false;

        try {
            connection = getConnection();
            connection.setAutoCommit(false); // Начало транзакции

            statement = connection.prepareStatement(sql);
            statement.setString(1, exhibition.getName());
            statement.setString(2, exhibition.getDescription());
            statement.setDate(3, exhibition.getStarted());
            statement.setDate(4, exhibition.getEnded());
            statement.setInt(5, exhibition.getMuseumId());
            statement.setInt(6, exhibition.getId());

            rowUpdated = statement.executeUpdate() > 0;
            connection.commit(); // Подтверждение транзакции

            logger.info("Exhibition updated: " + exhibition.getName());
        } catch (SQLException e) {
            if (connection != null) {
                connection.rollback(); // Откат транзакции в случае ошибки
            }
            logger.log(Level.SEVERE, "Error updating exhibition: " + exhibition.getName(), e);
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

    public Exhibition getExhibition(int id) throws SQLException {
        Exhibition exhibition = null;
        String sql = "SELECT * FROM exhibition WHERE id = ?";
        logger.info("Getting exhibition with id: " + id);

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                exhibition = convertExhibition(resultSet);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error getting exhibition with id: " + id, e);
            e.printStackTrace();
        }

        return exhibition;
    }

    public static Exhibition convertExhibition(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        String description = resultSet.getString("description");
        Date started = resultSet.getDate("started");
        Date ended = resultSet.getDate("ended");
        Integer museumId = resultSet.getInt("museumId");

        return new Exhibition(id, name, description, started, ended, museumId);
    }

    public List<ArtObject> listExhibitedArtObjects(int exhibitionId) throws SQLException {
        List<ArtObject> artObjects = new ArrayList<>();
        String sql = "SELECT ao.* FROM ArtObject ao " +
                "INNER JOIN ArtObjectOnExhibition aoe ON ao.Id = aoe.ArtObjectId " +
                "WHERE aoe.ExhibitionId = ?";
        logger.info("Listing art objects for exhibitionId: " + exhibitionId);

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, exhibitionId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                ArtObject artObject = ArtObjectDAO.convertArtObject(resultSet);
                artObjects.add(artObject);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error listing art objects for exhibitionId: " + exhibitionId, e);
            e.printStackTrace();
        }

        return artObjects;
    }

    public void unbindArtObjectFromExhibition(int artObjectId, int exhibitionId) throws SQLException {
        String sql = "{ call UnbindArtObjectFromExhibition(?, ?) }";
        Connection connection = null;
        logger.info("Unbinding art object with id: " + artObjectId + " from exhibitionId: " + exhibitionId);

        try {
            connection = getConnection();
            connection.setAutoCommit(false); // Начало транзакции

            try (CallableStatement stmt = connection.prepareCall(sql)) {
                stmt.setInt(1, artObjectId);
                stmt.setInt(2, exhibitionId);

                stmt.execute();
                connection.commit(); // Подтверждение транзакции

                logger.info("Art object with id: " + artObjectId + " unbound from exhibitionId: " + exhibitionId);
            }
        } catch (SQLException e) {
            if (connection != null) {
                connection.rollback(); // Откат транзакции в случае ошибки
            }
            logger.log(Level.SEVERE, "Error unbinding art object from exhibition", e);
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.setAutoCommit(true); // Восстановление автокоммита
                connection.close();
            }
        }
    }

    public void attachArtObjectFromExhibition(int artObjectId, int exhibitionId) throws SQLException {
        String sql = "{ call AttachArtObjectFromExhibition(?, ?) }";
        Connection connection = null;
        logger.info("Attaching art object with id: " + artObjectId + " to exhibitionId: " + exhibitionId);

        try {
            connection = getConnection();
            connection.setAutoCommit(false); // Начало транзакции

            try (CallableStatement stmt = connection.prepareCall(sql)) {
                stmt.setInt(1, artObjectId);
                stmt.setInt(2, exhibitionId);

                stmt.execute();
                connection.commit(); // Подтверждение транзакции

                logger.info("Art object with id: " + artObjectId + " attached to exhibitionId: " + exhibitionId);
            }
        } catch (SQLException e) {
            if (connection != null) {
                connection.rollback(); // Откат транзакции в случае ошибки
            }
            logger.log(Level.SEVERE, "Error attaching art object to exhibition", e);
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.setAutoCommit(true); // Восстановление автокоммита
                connection.close();
            }
        }
    }
    
    public boolean exists(int museumId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Museum WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, museumId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        }
        return false;
    }

}
