package org.servlet.museum_management.Servlet;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xddf.usermodel.chart.*;

import org.servlet.museum_management.DAO.MuseumDAO;
import org.servlet.museum_management.model.Museum;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/ExportToExcelServlet")
public class ExportToExcelServlet extends HttpServlet {
    private MuseumDAO museumDAO;

    public void init() {
        String jdbcURL = getServletContext().getInitParameter("jdbcURL");
        String jdbcUsername = getServletContext().getInitParameter("jdbcUsername");
        String jdbcPassword = getServletContext().getInitParameter("jdbcPassword");

        museumDAO = new MuseumDAO(jdbcURL, jdbcUsername, jdbcPassword);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=museums.xlsx");

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Museums");
            createHeaderRow(sheet);

            List<Museum> listMuseums = museumDAO.listAllMuseums();
            int rowCount = 1;

            for (Museum museum : listMuseums) {
                Row row = sheet.createRow(rowCount++);
                writeMuseum(museum, row);
            }

            // Create a sheet for charts
            XSSFSheet chartSheet = (XSSFSheet) workbook.createSheet("Charts");
            createChart(chartSheet, listMuseums);

            workbook.write(response.getOutputStream());
        } catch (Exception e) {
            throw new ServletException("Exception in Excel Export", e);
        }
    }

    private void createHeaderRow(Sheet sheet) {
        Row headerRow = sheet.createRow(0);
        CellStyle headerStyle = sheet.getWorkbook().createCellStyle();
        Font font = sheet.getWorkbook().createFont();
        font.setBold(true);
        headerStyle.setFont(font);

        Cell cell = headerRow.createCell(0);
        cell.setCellValue("ID");
        cell.setCellStyle(headerStyle);

        cell = headerRow.createCell(1);
        cell.setCellValue("Name");
        cell.setCellStyle(headerStyle);

        cell = headerRow.createCell(2);
        cell.setCellValue("Description");
        cell.setCellStyle(headerStyle);

        cell = headerRow.createCell(3);
        cell.setCellValue("Address");
        cell.setCellStyle(headerStyle);

        cell = headerRow.createCell(4);
        cell.setCellValue("Opening Time");
        cell.setCellStyle(headerStyle);

        cell = headerRow.createCell(5);
        cell.setCellValue("Closing Time");
        cell.setCellStyle(headerStyle);

        cell = headerRow.createCell(6);
        cell.setCellValue("Number of Exhibitions");
        cell.setCellStyle(headerStyle);
    }

    private void writeMuseum(Museum museum, Row row) {
        Cell cell = row.createCell(0);
        cell.setCellValue(museum.getId());

        cell = row.createCell(1);
        cell.setCellValue(museum.getName());

        cell = row.createCell(2);
        cell.setCellValue(museum.getDescription());

        cell = row.createCell(3);
        cell.setCellValue(museum.getAddress());

        cell = row.createCell(4);
        cell.setCellValue(museum.getTimeOpening());

        cell = row.createCell(5);
        cell.setCellValue(museum.getTimeClosing());

        cell = row.createCell(6);
        cell.setCellValue(museum.getExhibitionCount());
    }

    private void createChart(XSSFSheet chartSheet, List<Museum> listMuseums) {
        System.out.println("Creating chart with data size: " + listMuseums.size());

        // Заголовки
        Row headerRow = chartSheet.createRow(0);
        headerRow.createCell(0).setCellValue("Museum Name");
        headerRow.createCell(1).setCellValue("Number of Exhibitions");

        int rowNum = 1;
        for (Museum museum : listMuseums) {
            Row row = chartSheet.createRow(rowNum++);
            row.createCell(0).setCellValue(museum.getName());
            row.createCell(1).setCellValue(museum.getExhibitionCount());
        }

        try {
            XSSFDrawing drawing = chartSheet.createDrawingPatriarch();
            
            // Создаем линейный график
            XSSFClientAnchor lineAnchor = drawing.createAnchor(0, 0, 0, 0, 0, 5, 10, 20);
            XSSFChart lineChart = drawing.createChart(lineAnchor);
            XDDFChartLegend lineLegend = lineChart.getOrAddLegend();
            lineLegend.setPosition(LegendPosition.BOTTOM);

            XDDFCategoryAxis lineBottomAxis = lineChart.createCategoryAxis(AxisPosition.BOTTOM);
            XDDFValueAxis lineLeftAxis = lineChart.createValueAxis(AxisPosition.LEFT);
            lineLeftAxis.setCrosses(AxisCrosses.AUTO_ZERO);

            XDDFDataSource<String> xs = XDDFDataSourcesFactory.fromStringCellRange(chartSheet, new CellRangeAddress(1, listMuseums.size(), 0, 0));
            XDDFNumericalDataSource<Double> ys = XDDFDataSourcesFactory.fromNumericCellRange(chartSheet, new CellRangeAddress(1, listMuseums.size(), 1, 1));

            XDDFLineChartData lineData = (XDDFLineChartData) lineChart.createData(ChartTypes.LINE, lineBottomAxis, lineLeftAxis);
            XDDFLineChartData.Series lineSeries = (XDDFLineChartData.Series) lineData.addSeries(xs, ys);
            lineSeries.setTitle("Number of Exhibitions", null);
            lineSeries.setMarkerStyle(MarkerStyle.CIRCLE);

            lineChart.plot(lineData);

            // Создаем гистограмму
            XSSFClientAnchor barAnchor = drawing.createAnchor(0, 0, 0, 0, 12, 5, 22, 20); // Задаем другое положение
            XSSFChart barChart = drawing.createChart(barAnchor);
            XDDFChartLegend barLegend = barChart.getOrAddLegend();
            barLegend.setPosition(LegendPosition.BOTTOM);

            XDDFCategoryAxis barBottomAxis = barChart.createCategoryAxis(AxisPosition.BOTTOM);
            XDDFValueAxis barLeftAxis = barChart.createValueAxis(AxisPosition.LEFT);
            barLeftAxis.setCrosses(AxisCrosses.AUTO_ZERO);

            XDDFBarChartData barData = (XDDFBarChartData) barChart.createData(ChartTypes.BAR, barBottomAxis, barLeftAxis);
            XDDFBarChartData.Series barSeries = (XDDFBarChartData.Series) barData.addSeries(xs, ys);
            barSeries.setTitle("Number of Exhibitions", null);
            barData.setBarDirection(BarDirection.COL);

            barChart.plot(barData);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
