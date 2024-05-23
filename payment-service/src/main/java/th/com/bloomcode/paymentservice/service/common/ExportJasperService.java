package th.com.bloomcode.paymentservice.service.common;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.Exporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.*;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Service
@Slf4j
public class ExportJasperService {

	@SuppressWarnings({ "unchecked", "rawtypes" })

	public byte[] exportPDFfNrp(Map<String, Object> params, InputStream file) {
		byte[] bytes = null;
		JasperReport jasperReport = null;
		Exporter exporter = new JRPdfExporter();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			jasperReport = JasperCompileManager.compileReport(file);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JREmptyDataSource());
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporter.exportReport();
			bytes = out.toByteArray();
		} catch (JRException e) {
			e.printStackTrace();
		}
		return bytes;
	}

	public byte[] exportPDFfNrp(Map<String, Object> params, InputStream file, JRBeanCollectionDataSource dataSource) {
		byte[] bytes = null;
		JasperReport jasperReport = null;
		JRPdfExporter exporter = new JRPdfExporter();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			jasperReport = JasperCompileManager.compileReport(file);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporter.exportReport();
			bytes = out.toByteArray();
		} catch (JRException e) {
			e.printStackTrace();
		}
		return bytes;
	}
	
	public byte[] exportPDFGL(HashMap params, InputStream file, Connection con) {
		byte[] bytes = null;
//		JasperReport jasperReport = null;
		JRPdfExporter exporter = new JRPdfExporter();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(file);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, con);
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporter.exportReport();
			bytes = out.toByteArray();
		} catch (JRException e) {
			e.printStackTrace();
		}
		return bytes;
	}
	
	public byte[] exportExcelGL(Map<String, Object> params, InputStream file, Connection con) {
		byte[] bytes = null;
		
		JRXlsxExporter exporter = new JRXlsxExporter();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(file);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, con);
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporter.exportReport();
			bytes = out.toByteArray();
		} catch (JRException e) {
			e.printStackTrace();
		}
		return bytes;
	}

	public byte[] exportExcelNrp(Map<String, Object> params, InputStream file) {
		byte[] bytes = null;
		JasperReport jasperReport = null;
		JRXlsxExporter exporter = new JRXlsxExporter();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			jasperReport = JasperCompileManager.compileReport(file);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JREmptyDataSource());
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporter.exportReport();
			bytes = out.toByteArray();
		} catch (JRException e) {
			e.printStackTrace();
		}
		return bytes;
	}

	public byte[] exportExcelNrp(Map<String, Object> params, InputStream file, JRBeanCollectionDataSource dataSource) {
		byte[] bytes = null;
		JasperReport jasperReport = null;
		JRXlsxExporter exporter = new JRXlsxExporter();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			jasperReport = JasperCompileManager.compileReport(file);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporter.exportReport();
			bytes = out.toByteArray();
		} catch (JRException e) {
			e.printStackTrace();
		}
		return bytes;
	}

	//*Tao 2021-01-24 Test convert excel to pdf
	public byte[] exportExcelToPdf(byte[] excelFile) throws IOException, DocumentException {
		byte[] bytes = null;
		if (excelFile.length > 0) {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			try {
//				//PdfDocument pdf = new PdfDocument();
//				Workbook excel = new Workbook();
//				InputStream inputStream = new ByteArrayInputStream(excelFile);
//				excel.loadFromStream(inputStream);
//				excel.saveToStream(outputStream, com.spire.xls.FileFormat.PDF);
//
//
////				pdf.loadFromStream(inputStream);
////				//pdf.loadFromBytes(excelFile);
////				pdf.saveToStream(outputStream, FileFormat.PDF);
//				bytes = out.toByteArray();

				//InputStream inputStream = new ByteArrayInputStream(excelFile);
				FileInputStream inputStream = new FileInputStream(new File("C:\\test2.xlsx"));
				XSSFWorkbook my_xls_workbook = new XSSFWorkbook(inputStream);
				XSSFSheet my_worksheet = my_xls_workbook.getSheetAt(0);
				Iterator<Row> rowIterator = my_worksheet.iterator();
				Document iText_xls_2_pdf = new Document();
				PdfWriter.getInstance(iText_xls_2_pdf, new FileOutputStream("test2.pdf"));
				//PdfWriter.getInstance(iText_xls_2_pdf, outputStream);
				//PdfWriter.setEncryption("krishna".getBytes(), "testpass".getBytes(), PdfWriter.ALLOW_PRINTING, PdfWriter.ENCRYPTION_AES_256);

				int numColumns = 0;
				if (rowIterator.hasNext()) {
					Row row = rowIterator.next();
					numColumns = row.getLastCellNum();
				}
				iText_xls_2_pdf.open();
				//we have two columns in the Excel sheet, so we create a PDF table with two columns
				//Note: There are ways to make this dynamic in nature, if you want to.
				PdfPTable my_table = new PdfPTable(numColumns);
				//We will use the object below to dynamically add new data to the table
				PdfPCell table_cell;
				//Loop through rows.
				while(rowIterator.hasNext()) {
					Row row = rowIterator.next();
					Iterator<Cell> cellIterator = row.cellIterator();
					while(cellIterator.hasNext()) {
						Cell cell = cellIterator.next(); //Fetch CELL
						CellType cellType = cell.getCellType();
						switch(cellType.name().toUpperCase()) { //Identify CELL type
							//you need to add more code here based on
							//your requirement / transformations
							case "NUMERIC":
								if (DateUtil.isCellDateFormatted(cell)) {
									table_cell = new PdfPCell(new Phrase(new SimpleDateFormat("dd/MM/yyyy").format(cell.getDateCellValue())));
								} else {
									table_cell = new PdfPCell(new Phrase(String.valueOf((int) cell.getNumericCellValue())));
								}
								my_table.addCell(table_cell);
								break;
							case "STRING":
								table_cell = new PdfPCell(new Phrase(cell.getStringCellValue()));
								my_table.addCell(table_cell);
								break;
							case "FORMULA":
								table_cell = new PdfPCell(new Phrase(cell.getCellFormula()));
								my_table.addCell(table_cell);
								break;
							case "BLANK":
								table_cell = new PdfPCell(new Phrase(""));
								my_table.addCell(table_cell);
								break;
						}
						//next line
					}

				}
				//Finally add the table to PDF document
				iText_xls_2_pdf.add(my_table);
				//bytes = outputStream.toByteArray();
				iText_xls_2_pdf.close();
				//we created our pdf file..
				inputStream.close(); //close xls
			} catch (Exception e) {
				log.error(e.getMessage());
				e.printStackTrace();
			}
		}
		return bytes;
	}

}
