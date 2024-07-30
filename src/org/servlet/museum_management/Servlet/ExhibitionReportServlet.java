package org.servlet.museum_management.Servlet;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.servlet.museum_management.DAO.ExhibitionDAO;
import org.servlet.museum_management.model.Exhibition;

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

@WebServlet("/ExhibitionReportServlet")
public class ExhibitionReportServlet extends HttpServlet {
    private ExhibitionDAO exhibitionDAO;

    public void init() {
        String jdbcURL = getServletContext().getInitParameter("jdbcURL");
        String jdbcUsername = getServletContext().getInitParameter("jdbcUsername");
        String jdbcPassword = getServletContext().getInitParameter("jdbcPassword");

        exhibitionDAO = new ExhibitionDAO(jdbcURL, jdbcUsername, jdbcPassword);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Установите путь к директории со шрифтами
            String fontDir = getServletContext().getRealPath("/WEB-INF/classes/fonts");
            System.setProperty("jasperreports.fonts.dir", fontDir);

            List<Exhibition> exhibitions = exhibitionDAO.listAllExhibitions();
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(exhibitions);

            String reportPath = getServletContext().getRealPath("/WEB-INF/reports/ExhibitionReport.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(reportPath);

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("ReportTitle", "Exhibition Report");
            parameters.put("CurrentDate", new Date()); // Передача текущей даты
            String logoPath = getServletContext().getRealPath("/WEB-INF/reports/logo.png");
            parameters.put("LogoPath", logoPath); // Передача пути к логотипу

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=ExhibitionReport.pdf");

            JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
        } catch (SQLException | JRException e) {
            throw new ServletException(e);
        }
    }
}
