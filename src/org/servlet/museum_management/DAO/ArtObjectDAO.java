package org.servlet.museum_management.DAO;

import org.servlet.museum_management.model.ArtObject;
import org.servlet.museum_management.model.Exhibition;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ArtObjectDAO extends AbstractDAO {

    public ArtObjectDAO(String jdbcURL, String jdbcUsername, String jdbcPassword) {
        super(jdbcURL, jdbcUsername, jdbcPassword);
    }
	
	private static final Logger logger = Logger.getLogger(ArtObjectDAO.class.getName());

    public boolean insertArtObject(ArtObject artObject) throws SQLException {
        String sql = "INSERT INTO ArtObject (Name, Description, CreationYear, artist, MuseumId, photo) VALUES (?, ?, ?, ?, ?, ?)";
        Connection connection = null;
        PreparedStatement statement = null;
        boolean rowInserted = false;

		logger.info("Inserting art object: " + artObject.getName());
		
        try {
            connection = getConnection();
            connection.setAutoCommit(false); // Начало транзакции

            statement = connection.prepareStatement(sql);
            statement.setString(1, artObject.getName());
            statement.setString(2, artObject.getDescription());
            statement.setInt(3, artObject.getCreationYear());
            statement.setString(4, artObject.getArtist());
            statement.setInt(5, artObject.getMuseumId());
            statement.setBytes(6, artObject.getPhoto());

            rowInserted = statement.executeUpdate() > 0;
            connection.commit(); // Подтверждение транзакции
			
			logger.info("Art object inserted: " + artObject.getName());
        } catch (SQLException ex) {
            if (connection != null) {
                connection.rollback(); // Откат транзакции при ошибке
            }
			logger.log(Level.SEVERE, "Error inserting art object", ex);
            throw ex;
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.setAutoCommit(true);
                connection.close();
            }
        }

        return rowInserted;
    }

    public List<ArtObject> listAllArtObjects(Integer museumId) throws SQLException {
        List<ArtObject> artObjects = new ArrayList<>();
        String sql = "SELECT * FROM ArtObject WHERE museumId = ?";
		
		logger.info("Listing all art objects for museumId: " + museumId);

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, museumId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                ArtObject artObject = convertArtObject(resultSet);
                artObjects.add(artObject);
            }

            resultSet.close();
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error listing all art objects for museumId: " + museumId, ex);
            throw ex;
        }

        return artObjects;
    }

    public List<ArtObject> listAllArtObjectsNotAttachedToExhibition(Integer museumId, int exhibitionId) throws SQLException {
        List<ArtObject> artObjects = new ArrayList<>();
        String sql = "SELECT ao.* " +
                "FROM ArtObject ao " +
                "LEFT JOIN ArtObjectOnExhibition aoe  " +
                "    ON ao.Id = aoe.ArtObjectId AND aoe.ExhibitionId = ? " +
                "WHERE ao.MuseumId = ? AND aoe.ArtObjectId IS NULL; ";
				
		logger.info("Listing all art objects not attached to exhibitionId: " + exhibitionId + " for museumId: " + museumId);

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, exhibitionId);
            statement.setInt(2, museumId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                ArtObject artObject = convertArtObject(resultSet);
                artObjects.add(artObject);
            }

            resultSet.close();
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error listing all art objects not attached to exhibitionId: " + exhibitionId + " for museumId: " + museumId, ex);
            throw ex;
        }

        return artObjects;
    }

    public List<ArtObject> listAllArtObjects() throws SQLException {
        List<ArtObject> artObjects = new ArrayList<>();
        String sql = "SELECT * FROM ArtObject";
		
		logger.info("Listing all art objects");
		
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                ArtObject artObject = convertArtObject(resultSet);
                artObjects.add(artObject);
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error listing all art objects", ex);
            throw ex;
        }

        return artObjects;
    }

    public boolean deleteArtObject(ArtObject artObject) throws SQLException {
        String sql1 = "DELETE FROM ArtObjectOnExhibition WHERE ArtObjectId = ?";
        String sql2 = "DELETE FROM ArtObject WHERE id = ?";
        Connection connection = null;
        PreparedStatement statement1 = null;
        PreparedStatement statement2 = null;
        boolean rowDeleted = false;
		
		logger.info("Deleting art object: " + artObject.getName());

        try {
            connection = getConnection();
            connection.setAutoCommit(false); // Начало транзакции

            statement1 = connection.prepareStatement(sql1);
            statement1.setInt(1, artObject.getId());
            statement1.executeUpdate();

            statement2 = connection.prepareStatement(sql2);
            statement2.setInt(1, artObject.getId());
            rowDeleted = statement2.executeUpdate() > 0;

            connection.commit(); // Подтверждение транзакции
			
			logger.info("Art object deleted: " + artObject.getName());
        } catch (SQLException ex) {
            if (connection != null) {
                connection.rollback(); // Откат транзакции при ошибке
            }
			logger.log(Level.SEVERE, "Error deleting art object", ex);
            throw ex;
        } finally {
            if (statement1 != null) {
                statement1.close();
            }
            if (statement2 != null) {
                statement2.close();
            }
            if (connection != null) {
                connection.setAutoCommit(true);
                connection.close();
            }
        }

        return rowDeleted;
    }

    public boolean updateArtObject(ArtObject artObject, boolean updatePhoto) throws SQLException {
        String sql = "UPDATE ArtObject SET Name = ?, Description = ?, CreationYear = ?, artist = ?, MuseumId = ?" +
                (updatePhoto ? ", photo = ? " : "") +
                "WHERE id = ?";
        Connection connection = null;
        PreparedStatement statement = null;
        boolean rowUpdated = false;
		
		logger.info("Updating art object: " + artObject.getName());

        try {
            connection = getConnection();
            connection.setAutoCommit(false); // Начало транзакции

            statement = connection.prepareStatement(sql);
            statement.setString(1, artObject.getName());
            statement.setString(2, artObject.getDescription());
            statement.setInt(3, artObject.getCreationYear());
            statement.setString(4, artObject.getArtist());
            statement.setInt(5, artObject.getMuseumId());
            if (updatePhoto) {
                statement.setBytes(6, artObject.getPhoto());
                statement.setInt(7, artObject.getId());
            } else {
                statement.setInt(6, artObject.getId());
            }

            rowUpdated = statement.executeUpdate() > 0;
            connection.commit(); // Подтверждение транзакции
			
			logger.info("Art object updated: " + artObject.getName());
        } catch (SQLException ex) {
            if (connection != null) {
                connection.rollback(); // Откат транзакции при ошибке
            }
			logger.log(Level.SEVERE, "Error updating art object", ex);
            throw ex;
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.setAutoCommit(true);
                connection.close();
            }
        }

        return rowUpdated;
    }

    public ArtObject getArtObject(int id) throws SQLException {
        ArtObject artObject = null;
        String sql = "SELECT * FROM ArtObject WHERE id = ?";
		
		logger.info("Getting art object with id: " + id);

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                artObject = convertArtObject(resultSet);
            }

            resultSet.close();
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error getting art object with id: " + id, ex);
            throw ex;
		}
        return artObject;
    }

    public static ArtObject convertArtObject(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        String description = resultSet.getString("description");
        Integer creationYear = resultSet.getInt("creationYear");
        String artist = resultSet.getString("artist");
        Integer museumId = resultSet.getInt("museumId");
        byte[] photo = resultSet.getBytes("photo");
		
		logger.info("Converting ResultSet to ArtObject with id: " + id);
        return new ArtObject(id, name, description, creationYear, artist, museumId, photo);
    }

    public List<Exhibition> getExhibitionsByArtObjectId(int artObjectId) throws SQLException {
        List<Exhibition> listExhibition = new ArrayList<>();
        String sql = "select * from GetExhibitionsByArtObjectId(?)";
		
		logger.info("Getting exhibitions for ArtObject with id: " + artObjectId);

        try (Connection connection = getConnection();
             CallableStatement stmt = connection.prepareCall(sql)) {
            stmt.setInt(1, artObjectId);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Exhibition exhibition = ExhibitionDAO.convertExhibition(rs);
                listExhibition.add(exhibition);
            }
			logger.info("Found " + listExhibition.size() + " exhibitions for ArtObject with id: " + artObjectId);
        } catch (SQLException e) {
            logger.severe("Error getting exhibitions for ArtObject with id: " + artObjectId + " - " + e.getMessage());
            throw e;
        }

        return listExhibition;
    }

    public int getExhibitionsCountByArtObjectId(int artObjectId) throws SQLException {
        String sql = "{ ? = CALL GetExhibitionsCountByArtObjectId(?) }";
		
		logger.info("Getting exhibition count for ArtObject with id: " + artObjectId);

        try (Connection connection = getConnection();
             CallableStatement stmt = connection.prepareCall(sql)) {
            stmt.setInt(2, artObjectId);
            stmt.registerOutParameter(1, java.sql.Types.INTEGER);

            stmt.execute();
            return stmt.getInt(1);
        } catch (SQLException e) {
            logger.severe("Error getting exhibition count for ArtObject with id: " + artObjectId + " - " + e.getMessage());
            throw e;
        }
    }
    
    public void saveDrawing(int id, byte[] drawing) throws SQLException {
        String sql = "UPDATE ArtObject SET photo = ? WHERE Id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setBytes(1, drawing);
            statement.setInt(2, id);
            statement.executeUpdate();
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error saving drawing", ex);
            throw ex;
        }
    }
}