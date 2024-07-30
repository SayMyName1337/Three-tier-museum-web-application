package org.servlet.museum_management.Servlet;

import java.io.IOException;
import java.util.Base64;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.servlet.museum_management.DAO.ArtObjectDAO;
import org.servlet.museum_management.model.ArtObject;
import java.sql.SQLException;

@WebServlet("/art-objects/draw")
public class PictureDrawServlet extends HttpServlet {
    private ArtObjectDAO artObjectDAO;

    public void init() {
        String jdbcURL = getServletContext().getInitParameter("jdbcURL");
        String jdbcUsername = getServletContext().getInitParameter("jdbcUsername");
        String jdbcPassword = getServletContext().getInitParameter("jdbcPassword");

        artObjectDAO = new ArtObjectDAO(jdbcURL, jdbcUsername, jdbcPassword);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        ArtObject artObject = null;
        try {
            artObject = artObjectDAO.getArtObject(id);
        } catch (SQLException e) {
            throw new ServletException("Error retrieving art object", e);
        }
        request.setAttribute("artObject", artObject);
        request.getRequestDispatcher("/WEB-INF/art-objects/ArtObjectDraw.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String drawingDataURL = request.getParameter("drawing");

        if (drawingDataURL == null || !drawingDataURL.startsWith("data:image/")) {
            throw new ServletException("Invalid image data");
        }

        byte[] drawingData = Base64.getDecoder().decode(drawingDataURL.split(",")[1]);

        try {
            artObjectDAO.saveDrawing(id, drawingData);
        } catch (SQLException e) {
            throw new ServletException("Error saving drawing", e);
        }

        response.sendRedirect("list");
    }
}
