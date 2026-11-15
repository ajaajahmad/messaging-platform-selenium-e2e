package com.messaging.platform.utils;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WrapsDriver;

public final class HtmlReportUtil {

	private HtmlReportUtil() {
	}

	public static void createHtmlTable(WebElement table, String title, String outputDir, String filePrefix)
			throws IOException {

		createDir(outputDir);

		String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
		String filePath = outputDir + "/" + filePrefix + "_" + timestamp + ".html";

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
			writer.write(buildHtml(table, title));
		}
	}

	private static String buildHtml(WebElement table, String title) {
		StringBuilder html = new StringBuilder("""
				<html><head>
				<meta charset="UTF-8">
				<style>
				    body { font-family: sans-serif; }
				    table { border-collapse: collapse; width: 100%; }
				    th, td { border: 1px solid #333; padding: 8px; vertical-align: top; }
				    th { background: #f2f2f2; }
				    /* Optional: Add some spacing for inner divs if they are tight */
				    td div { margin-bottom: 2px; }
				</style>
				</head><body>
				""");

		html.append("<h2>").append(title).append("</h2>");
		html.append("<table>");

		// Read in one browser call: the table re-renders while a view loads, so walking it
		// cell by cell can hit a stale element.
		JavascriptExecutor js = (JavascriptExecutor) ((WrapsDriver) table).getWrappedDriver();
		@SuppressWarnings("unchecked")
		List<List<List<String>>> rows = (List<List<List<String>>>) js.executeScript(
				"return Array.from(arguments[0].querySelectorAll('tr')).map(function (row) {"
						+ " return Array.from(row.children).filter(function (cell) {"
						+ " return cell.tagName === 'TH' || cell.tagName === 'TD'; }).map(function (cell) {"
						+ " return [cell.tagName.toLowerCase(), cell.innerHTML]; }); });",
				table);

		for (List<List<String>> row : rows) {
			html.append("<tr>");

			for (List<String> cell : row) {
				html.append("<").append(cell.get(0)).append(">");
				html.append(cell.get(1));
				html.append("</").append(cell.get(0)).append(">");
			}
			html.append("</tr>");
		}

		html.append("</table></body></html>");
		return html.toString();
	}

	private static void createDir(String path) {
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
	}
}