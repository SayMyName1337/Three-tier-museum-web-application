package org.servlet.museum_management.Servlet;

import org.servlet.museum_management.DAO.ArtObjectDAO;
import org.servlet.museum_management.DAO.MuseumDAO;
import org.servlet.museum_management.model.ArtObject;
import org.servlet.museum_management.model.Exhibition;
import org.servlet.museum_management.model.Museum;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

@MultipartConfig
@WebServlet("/art-objects/*")
public class ArtObjectServlet extends HttpServlet {
    private ArtObjectDAO artObjectDAO;
    private MuseumDAO museumDAO;

    public void init() {
        String jdbcURL = getServletContext().getInitParameter("jdbcURL");
        String jdbcUsername = getServletContext().getInitParameter("jdbcUsername");
        String jdbcPassword = getServletContext().getInitParameter("jdbcPassword");

        artObjectDAO = new ArtObjectDAO(jdbcURL, jdbcUsername, jdbcPassword);
        museumDAO = new MuseumDAO(jdbcURL, jdbcUsername, jdbcPassword);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = Utils.getAction(request);

        try {
            if (request.getParameter("museum-id") != null) {
                Integer museumId = Integer.valueOf(request.getParameter("museum-id"));
                Museum museum = museumDAO.getMuseum(museumId);
                request.setAttribute("museum", museum);
            }

            switch (action) {
                case "/new":
                    showNewForm(request, response);
                    break;
                case "/insert":
                    insertArtObject(request, response);
                    break;
                case "/delete":
                    deleteArtObject(request, response);
                    break;
                case "/edit":
                    showEditForm(request, response);
                    break;
                case "/update":
                    updateArtObject(request, response);
                    break;
                case "/exhibitions-taked-place":
                    listExhibitionsWhereAOTakedPlace(request, response);
                    break;
                case "/exibitions-count":
                    showExhibitonCount(request, response);
                    break;
                case "/list":
                default:
                    listArtObjects(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    private void listArtObjects(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        Museum museum = (Museum) request.getAttribute("museum");
        List<ArtObject> artObjects;
        if (museum != null) {
            artObjects = artObjectDAO.listAllArtObjects(museum.getId());
        } else {
            artObjects = artObjectDAO.listAllArtObjects();
        }
        request.setAttribute("artObjects", artObjects);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/art-objects/ArtObjectList.jsp");
        dispatcher.forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/art-objects/ArtObjectForm.jsp");
        dispatcher.forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        ArtObject existingArtObject = artObjectDAO.getArtObject(id);
        request.setAttribute("artObject", existingArtObject);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/art-objects/ArtObjectForm.jsp");
        dispatcher.forward(request, response);
    }

    private void insertArtObject(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        ArtObject newArtObject = createArtObjectObj(request, null);
        artObjectDAO.insertArtObject(newArtObject);
        response.sendRedirect("list");
    }

    private void updateArtObject(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        Integer id = Integer.parseInt(request.getParameter("id"));
        ArtObject updatedArtObject = createArtObjectObj(request, id);
        artObjectDAO.updateArtObject(updatedArtObject, updatedArtObject.getPhoto().length > 0);
        response.sendRedirect("list");
    }

    private void deleteArtObject(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        ArtObject artObject = new ArtObject();
        artObject.setId(id);
        artObjectDAO.deleteArtObject(artObject);
        response.sendRedirect("list");
    }

    private static ArtObject createArtObjectObj(HttpServletRequest request, Integer id) throws ServletException, IOException {
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        Integer creationYear = Integer.valueOf(request.getParameter("creationYear"));
        String artist = request.getParameter("artist");
        Integer museumId = Integer.valueOf(request.getParameter("museumId"));
        byte[] photo = readAllBytes(request.getPart("photo").getInputStream());

        ArtObject artObject = new ArtObject();
        artObject.setId(id);
        artObject.setName(name);
        artObject.setDescription(description);
        artObject.setCreationYear(creationYear);
        artObject.setArtist(artist);
        artObject.setMuseumId(museumId);
        artObject.setPhoto(photo);
        return artObject;
    }

    private static byte[] readAllBytes(InputStream inputStream) throws IOException {
        final int bufLen = 4 * 0x400; // 4KB
        byte[] buf = new byte[bufLen];
        int readLen;
        IOException exception = null;

        try {
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                while ((readLen = inputStream.read(buf, 0, bufLen)) != -1)
                    outputStream.write(buf, 0, readLen);

                return outputStream.toByteArray();
            }
        } catch (IOException e) {
            exception = e;
            throw e;
        } finally {
            if (exception == null) inputStream.close();
            else try {
                inputStream.close();
            } catch (IOException e) {
                exception.addSuppressed(e);
            }
        }
    }

    private void listExhibitionsWhereAOTakedPlace(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("id"));
        ArtObject existingArtObject = artObjectDAO.getArtObject(id);
        request.setAttribute("artObject", existingArtObject);
        List<Exhibition> listExhibition;
        listExhibition = artObjectDAO.getExhibitionsByArtObjectId(id);
        request.setAttribute("listExhibitionTakedPlace", listExhibition);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/art-objects/OnExhibitionList.jsp");
        dispatcher.forward(request, response);
    }

    private void showExhibitonCount(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        int id = Integer.parseInt(request.getParameter("id"));
        ArtObject existingArtObject = artObjectDAO.getArtObject(id);
        request.setAttribute("artObject", existingArtObject);
        int exhibitionsCount = artObjectDAO.getExhibitionsCountByArtObjectId(id);
        request.setAttribute("exhibitionsCount", exhibitionsCount);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/art-objects/ExhibitionCount.jsp");
        dispatcher.forward(request, response);
    }

}
