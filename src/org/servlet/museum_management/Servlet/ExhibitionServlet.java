package org.servlet.museum_management.Servlet;

import org.servlet.museum_management.DAO.ArtObjectDAO;
import org.servlet.museum_management.DAO.ExhibitionDAO;
import org.servlet.museum_management.DAO.MuseumDAO;
import org.servlet.museum_management.model.ArtObject;
import org.servlet.museum_management.model.Exhibition;
import org.servlet.museum_management.model.Museum;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/exhibitions/*")
public class ExhibitionServlet extends HttpServlet {
    private ExhibitionDAO exhibitionDAO;
    private MuseumDAO museumDAO;
    private ArtObjectDAO artObjectDAO;

    public void init() {
        String jdbcURL = getServletContext().getInitParameter("jdbcURL");
        String jdbcUsername = getServletContext().getInitParameter("jdbcUsername");
        String jdbcPassword = getServletContext().getInitParameter("jdbcPassword");

        exhibitionDAO = new ExhibitionDAO(jdbcURL, jdbcUsername, jdbcPassword);
        museumDAO = new MuseumDAO(jdbcURL, jdbcUsername, jdbcPassword);
        artObjectDAO = new ArtObjectDAO(jdbcURL, jdbcUsername, jdbcPassword);
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
                    insertExhibition(request, response);
                    break;
                case "/delete":
                    deleteExhibition(request, response);
                    break;
                case "/edit":
                    showEditForm(request, response);
                    break;
                case "/update":
                    updateExhibition(request, response);
                    break;
                case "/exhibited-ao":
                    listExhibitedArtObjects(request, response);
                    break;
                case "/remove-ao-from-ex":
                    removeArtObjectFromExhibition(request, response);
                    break;
                case "/select-ao-to-attach":
                    listArtObjectsCanBeAttachedToExhibition(request, response);
                    break;
                case "/attach-to-exhibition":
                    attachArtObjectFromExhibition(request, response);
                    break;
                case "/list":
                default:
                    listExhibition(request, response);
                    break;
            }
        } catch (SQLException ex) {
            request.setAttribute("javax.servlet.error.exception", ex);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/error.jsp");
            dispatcher.forward(request, response);
        }
    }

    private void listExhibition(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        Museum museum = (Museum) request.getAttribute("museum");
        List<Exhibition> listExhibition;
        if (museum != null) {
            listExhibition = exhibitionDAO.listAllExhibitions(museum.getId());
        } else {
            listExhibition = exhibitionDAO.listAllExhibitions();
        }
        request.setAttribute("listExhibition", listExhibition);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/exhibitions/ExhibitionList.jsp");
        dispatcher.forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/exhibitions/ExhibitionForm.jsp");
        dispatcher.forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Exhibition existingExhibition = exhibitionDAO.getExhibition(id);
        request.setAttribute("museum", museumDAO.getMuseum(existingExhibition.getMuseumId()));
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/exhibitions/ExhibitionForm.jsp");
        request.setAttribute("exhibition", existingExhibition);
        dispatcher.forward(request, response);
    }

    private void insertExhibition(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        try {
            Exhibition newExhibition = createExhibitionObject(request, null);
            exhibitionDAO.insertExhibition(newExhibition);
            response.sendRedirect("list?museum-id=" + newExhibition.getMuseumId());
        } catch (SQLException e) {
            request.setAttribute("javax.servlet.error.exception", e);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/error.jsp");
            dispatcher.forward(request, response);
        }
    }

    private void updateExhibition(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        try {
            request.setCharacterEncoding("UTF-8");
            Integer id = Integer.parseInt(request.getParameter("id"));
            Exhibition updatedExhibition = createExhibitionObject(request, id);
            exhibitionDAO.updateExhibition(updatedExhibition);
            response.sendRedirect("list?museum-id=" + updatedExhibition.getMuseumId());
        } catch (SQLException e) {
            request.setAttribute("javax.servlet.error.exception", e);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/error.jsp");
            dispatcher.forward(request, response);
        }
    }

    private void deleteExhibition(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));

        Exhibition exhibition = new Exhibition();
        exhibition.setId(id);
        exhibitionDAO.deleteExhibition(exhibition);
        response.sendRedirect("list");
    }

    private static Exhibition createExhibitionObject(HttpServletRequest request, Integer id) {
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        Date started = null;
        if (!request.getParameter("started").equals("")) {
            started = Date.valueOf(request.getParameter("started"));
        }
        Date ended = null;
        if (!request.getParameter("ended").equals("")) {
            ended = Date.valueOf(request.getParameter("ended"));
        }
        Integer museumId = Integer.valueOf(request.getParameter("museum-id"));

        return new Exhibition(id, name, description, started, ended, museumId);
    }

    private void listExhibitedArtObjects(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        int exhbitionId = Integer.parseInt(request.getParameter("id"));
        Exhibition exhibition = exhibitionDAO.getExhibition(exhbitionId);
        request.setAttribute("exhibition", exhibition);
        Museum museum = museumDAO.getMuseum(exhibition.getMuseumId());
        request.setAttribute("museum", museum);

        List<ArtObject> exhibitedArtObjects = exhibitionDAO.listExhibitedArtObjects(exhbitionId);
        request.setAttribute("exhibitedArtObjects", exhibitedArtObjects);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/exhibitions/ExhibitedArtObjects.jsp");
        dispatcher.forward(request, response);
    }

    private void removeArtObjectFromExhibition(HttpServletRequest request, HttpServletResponse response)
            throws IOException, SQLException {
        int exhibitionId = Integer.parseInt(request.getParameter("ex-id"));
        int artObjectId = Integer.parseInt(request.getParameter("ao-id"));
        exhibitionDAO.unbindArtObjectFromExhibition(artObjectId, exhibitionId);
        response.sendRedirect(request.getHeader("referer"));
    }

    private void listArtObjectsCanBeAttachedToExhibition(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        int exhibitionId = Integer.parseInt(request.getParameter("ex-id"));
        Exhibition exhibition = exhibitionDAO.getExhibition(exhibitionId);
        request.setAttribute("exhibition", exhibition);

        Museum museum = museumDAO.getMuseum(exhibition.getMuseumId());
        request.setAttribute("museum", museum);

        List<ArtObject> artObjects;
        artObjects = artObjectDAO.listAllArtObjectsNotAttachedToExhibition(exhibition.getMuseumId(), exhibition.getId());
        request.setAttribute("artObjects", artObjects);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/exhibitions/AttachArtObjectToExhibition.jsp");
        dispatcher.forward(request, response);
    }

    private void attachArtObjectFromExhibition(HttpServletRequest request, HttpServletResponse response)
            throws IOException, SQLException {
        int exhibitionId = Integer.parseInt(request.getParameter("ex-id"));
        int artObjectId = Integer.parseInt(request.getParameter("ao-id"));
        exhibitionDAO.attachArtObjectFromExhibition(artObjectId, exhibitionId);
        response.sendRedirect("/exhibitions/exhibited-ao?id=" + exhibitionId);
    }
}
