package org.servlet.museum_management.Servlet;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.servlet.museum_management.DAO.MuseumDAO;
import org.servlet.museum_management.model.Museum;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/ReportServlet")
public class ReportServlet extends HttpServlet {
    private MuseumDAO museumDAO;

    public void init() {
        String jdbcURL = getServletContext().getInitParameter("jdbcURL");
        String jdbcUsername = getServletContext().getInitParameter("jdbcUsername");
        String jdbcPassword = getServletContext().getInitParameter("jdbcPassword");

        museumDAO = new MuseumDAO(jdbcURL, jdbcUsername, jdbcPassword);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Установите путь к директории со шрифтами
            String fontDir = getServletContext().getRealPath("/WEB-INF/classes/fonts");
            System.setProperty("jasperreports.fonts.dir", fontDir);

            List<Museum> museums = museumDAO.listAllMuseums();
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(museums);

            String reportPath = getServletContext().getRealPath("/WEB-INF/reports/MuseumReport.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(reportPath);

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("ReportTitle", "Museum Report");
            parameters.put("CurrentDate", new Date()); // Передача текущей даты
            String logoPath = getServletContext().getRealPath("/WEB-INF/reports/logo.png");
            parameters.put("LogoPath", logoPath); // Передача пути к логотипу

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=MuseumReport.pdf");

            JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
        } catch (SQLException | JRException e) {
            throw new ServletException(e);
        }
    }
}
