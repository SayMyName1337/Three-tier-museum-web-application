package org.servlet.museum_management.Servlet;

import org.servlet.museum_management.DAO.MuseumDAO;
import org.servlet.museum_management.model.Museum;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.format.DateTimeParseException;

@WebServlet("/museums/*")
public class MuseumServlet extends HttpServlet {
    private MuseumDAO museumDAO;

    public void init() {
        String jdbcURL = getServletContext().getInitParameter("jdbcURL");
        String jdbcUsername = getServletContext().getInitParameter("jdbcUsername");
        String jdbcPassword = getServletContext().getInitParameter("jdbcPassword");

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
            switch (action) {
                case "/new":
                    showNewForm(request, response);
                    break;
                case "/insert":
                    insertMuseum(request, response);
                    break;
                case "/delete":
                    deleteMuseum(request, response);
                    break;
                case "/edit":
                    showEditForm(request, response);
                    break;
                case "/update":
                    updateMuseum(request, response);
                    break;
                case "/list":
                default:
                    listMuseum(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    private void listMuseum(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        List<Museum> listMuseum = museumDAO.listAllMuseums();
        request.setAttribute("listMuseum", listMuseum);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/museums/MuseumList.jsp");
        dispatcher.forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/museums/MuseumForm.jsp");
        dispatcher.forward(request, response);
//		request.getRequestDispatcher("MuseumForm.jsp").include(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Museum existingMuseum = museumDAO.getMuseum(id);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/museums/MuseumForm.jsp");
        request.setAttribute("museum", existingMuseum);
        dispatcher.forward(request, response);
    }

    private void insertMuseum(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        Museum newMuseum = convertMuseum(request);
        museumDAO.insertMuseum(newMuseum);
        response.sendRedirect("list");
    }

    private void updateMuseum(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        Museum museum = convertMuseum(request);
        museumDAO.updateMuseum(museum);
        response.sendRedirect("list");
    }

    private void deleteMuseum(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));

        Museum museum = new Museum();
        museum.setId(id);
        museumDAO.deleteMuseum(museum);
        response.sendRedirect("list");
    }

    private static Museum convertMuseum(HttpServletRequest request) {
        Integer id = null;
        if (request.getParameter("id")!=null) {
            id = Integer.parseInt(request.getParameter("id"));
        }
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String address = request.getParameter("address");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm[:ss]");
        Time TimeOpening = Time.valueOf(LocalTime.parse(request.getParameter("time_opened"), timeFormatter));
        Time TimeClosing = Time.valueOf(LocalTime.parse(request.getParameter("time_closed"), timeFormatter));

        Museum museum = new Museum(id, name, description, address, TimeOpening, TimeClosing);
        return museum;
    }

}
