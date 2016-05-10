package priv.azx.mpp.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class ExcelUtility {

	public static final String EXCEL_2003_FILE_TYPE = "xls";

	public static final String EXCEL_2007_FILE_TYPE = "xlsx";

	public static String getTitleName(String currentTitleName, boolean isLower) {
		if (isLower)
			return currentTitleName.toLowerCase().trim();
		else
			return currentTitleName.trim();
	}

	private static String getCheckedString(String s) {
		char[] chars = s.toCharArray();
		StringBuilder stringBuilder = new StringBuilder();
		for (char c : chars) {
			if (Character.isDigit(c))
				stringBuilder.append(c);
			if (Character.isLetter(c))
				stringBuilder.append(c);
		}
		return stringBuilder.toString();
	}

	/*
	 * @SuppressWarnings("unused") private static String getCellContent(XSSFCell
	 * cell) { switch (cell.getCellType()) { case XSSFCell.CELL_TYPE_NUMERIC:
	 * return cell.getNumericCellValue() + ""; case XSSFCell.CELL_TYPE_STRING:
	 * return cell.getStringCellValue(); case XSSFCell.CELL_TYPE_BOOLEAN: return
	 * cell.getBooleanCellValue() ? "true" : "false"; case
	 * XSSFCell.CELL_TYPE_FORMULA: return cell.getCellFormula(); case
	 * XSSFCell.CELL_TYPE_BLANK: return null; case XSSFCell.CELL_TYPE_ERROR:
	 * return null;
	 * 
	 * }
	 * 
	 * return null;
	 * 
	 * }
	 */

	private static String getCellContent(HSSFCell cell) {
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_NUMERIC:
			return cell.getNumericCellValue() + "";
		case HSSFCell.CELL_TYPE_STRING:
			return cell.getStringCellValue();
		case HSSFCell.CELL_TYPE_BOOLEAN:
			return cell.getBooleanCellValue() ? "true" : "false";
		case HSSFCell.CELL_TYPE_FORMULA:
			return cell.getCellFormula();
		case HSSFCell.CELL_TYPE_BLANK:
			return null;
		case HSSFCell.CELL_TYPE_ERROR:
			return null;

		}

		return null;

	}

	private static List<String> getFileTitle(POIFSFileSystem poifsFileSystem) throws IOException {
		List<String> titleList = new ArrayList<String>();
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(poifsFileSystem);
		HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);

		int rowStart = hssfSheet.getFirstRowNum();
		if (rowStart >= 0) {
			HSSFRow row = hssfSheet.getRow(rowStart);
			if (row != null) {
				int cellStart = row.getFirstCellNum();
				int cellEnd = row.getLastCellNum();
				for (int c = cellStart; c <= cellEnd; c++) {
					HSSFCell cell = row.getCell(c);
					if ((cell != null) && (getCellContent(cell) != null))
						titleList.add(getCheckedString(getCellContent(cell).trim()));
				}
			}
		}
		hssfWorkbook.close();

		return titleList;
	}

	public static List<String> getFileTitle(File file) {
		List<String> titleList = new ArrayList<String>();
		try {
			POIFSFileSystem poifsFileSystem = new POIFSFileSystem(new FileInputStream(file));
			titleList = getFileTitle(poifsFileSystem);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		return titleList;
	}

	public static List<String> getFileTitle(byte[] fileContent) {
		List<String> titleList = new ArrayList<String>();
		try {
			InputStream is = new ByteArrayInputStream(fileContent);
			POIFSFileSystem poifsFileSystem = new POIFSFileSystem(is);
			titleList = getFileTitle(poifsFileSystem);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		return titleList;
	}

	private static String[] getFileTitleOrder(POIFSFileSystem poifsFileSystem) throws IOException {
		String[] titleOrder = new String[0];

		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(poifsFileSystem);
		HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);

		int rowStart = hssfSheet.getFirstRowNum();
		if (rowStart >= 0) {
			HSSFRow row = hssfSheet.getRow(rowStart);
			if (row != null) {
				int cellStart = row.getFirstCellNum();
				int cellEnd = row.getLastCellNum();
				titleOrder = new String[cellEnd];
				for (int c = cellStart; c < cellEnd; c++) {
					HSSFCell cell = row.getCell(c);
					if ((cell != null) && (getCellContent(cell) != null))
						titleOrder[c] = getCheckedString(getCellContent(cell).trim());
					else
						titleOrder[c] = null;
				}
			}
		}
		hssfWorkbook.close();
		return titleOrder;
	}

	private static String[] getFileTitleOrder(File file) {
		String[] titleOrder = new String[0];
		try {
			POIFSFileSystem poifsFileSystem = new POIFSFileSystem(new FileInputStream(file));
			titleOrder = getFileTitleOrder(poifsFileSystem);
			return titleOrder;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	private static String[] getFileTitleOrder(byte[] fileContent) {
		String[] titleOrder = new String[0];
		try {
			InputStream is = new ByteArrayInputStream(fileContent);
			POIFSFileSystem poifsFileSystem = new POIFSFileSystem(is);
			titleOrder = getFileTitleOrder(poifsFileSystem);
			return titleOrder;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * public static List<String> getFileTitle2007(File file) { List<String>
	 * titleList = new ArrayList<String>();
	 * 
	 * try (InputStream fis = new FileInputStream(file);) { // XSSFWorkbook
	 * xssfWorkbook = new XSSFWorkbook(fis); // XSSFSheet xssfSheet =
	 * xssfWorkbook.getSheetAt(0);
	 * 
	 * System.out.println("666666666666666666666666666666666666666666666666666")
	 * ; System.out.println(file.length()); System.out.println(file.getPath());
	 * // OPCPackage pkg = OPCPackage.open(file.getPath()); // XSSFWorkbook
	 * xssfWorkbook = new XSSFWorkbook(pkg);
	 * 
	 * // XSSFWorkbook xssfWorkbook = new //
	 * XSSFWorkbook(ExcelUtility.class.getClassLoader().getResourceAsStream(file
	 * .getPath())); XSSFWorkbook xssfWorkbook = new XSSFWorkbook(file);
	 * //XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0); xssfWorkbook.close();
	 * // pkg.close(); } catch (IOException e) { // TODO Auto-generated catch
	 * block e.printStackTrace(); } catch (InvalidFormatException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); }
	 * 
	 * return titleList; }
	 */

	private static String[] getFileTitleOrder2007(File file) {
		String[] titleOrder = new String[0];
		try {
			POIFSFileSystem poifsFileSystem = new POIFSFileSystem(new FileInputStream(file));
			HSSFWorkbook hssfWorkbook = new HSSFWorkbook(poifsFileSystem);
			HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);

			int rowStart = hssfSheet.getFirstRowNum();
			if (rowStart > 0) {
				HSSFRow row = hssfSheet.getRow(rowStart);
				if (row != null) {
					int cellStart = row.getFirstCellNum();
					int cellEnd = row.getLastCellNum();
					titleOrder = new String[cellEnd];
					for (int c = cellStart; c <= cellEnd; c++) {
						HSSFCell cell = row.getCell(c);
						if ((cell != null) && (getCellContent(cell) != null))
							titleOrder[c] = getCheckedString(getCellContent(cell).trim());
						else
							titleOrder[c] = null;
					}
				}
			}
			hssfWorkbook.close();
			return titleOrder;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public static List<String> checkFileTitle(List<String> currentTitleList, List<String> necessaryTitleList) {
		List<String> resultList = new ArrayList<String>();

		if (necessaryTitleList.size() == 0)
			return resultList;

		if (currentTitleList.size() > 0) {
			Set<String> necessaryTitleSet = new HashSet<String>();
			necessaryTitleSet.addAll(necessaryTitleList);
			Set<String> currentTitleSet = new HashSet<String>();
			currentTitleSet.addAll(currentTitleList);
			if (necessaryTitleSet.removeAll(currentTitleSet))
				if (necessaryTitleSet.size() == 0)
					return resultList;
				else
					resultList.addAll(necessaryTitleSet);
		}

		return resultList;
	}

	public static List<String> checkFileTitle(File file, List<String> necessaryTitleList) {
		List<String> titleList = getFileTitle(file);
		return checkFileTitle(titleList, necessaryTitleList);
	}

	public static List<String> checkFileTitle(byte[] fileContent, List<String> necessaryTitleList) {
		List<String> titleList = getFileTitle(fileContent);
		return checkFileTitle(titleList, necessaryTitleList);
	}

	/*
	 * public static List<List<String>> getFileContentList(File file) {
	 * List<List<String>> contentList = new ArrayList<List<String>>();
	 * 
	 * return contentList; }
	 */

	public static List<Map<String, String>> getFileContentMap(File file) {
		return getFileContentMap(file, true);
	}
	
	public static void xxx(File file, boolean isLower, List<String> titleList) {
		try {
			POIFSFileSystem poifsFileSystem = new POIFSFileSystem(new FileInputStream(file));
			HSSFWorkbook hssfWorkbook = new HSSFWorkbook(poifsFileSystem);
			HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);

			int columnCount = titleList.size();
			HSSFRow titleRow = hssfSheet.getRow(0);
			for (int c = 0; c < columnCount; c++) {
				HSSFCell cell = titleRow.createCell(c);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(new String(titleList.get(c).getBytes(), "utf-8"));
			}
			
			FileOutputStream fos = new FileOutputStream(file);
			hssfWorkbook.write(fos);

			hssfWorkbook.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}

	}

	public static List<Map<String, String>> getFileContentMap(File file, boolean isLower) {
		List<Map<String, String>> contentList = new ArrayList<Map<String, String>>();
		String[] titleOrder = getFileTitleOrder(file);

		try {
			POIFSFileSystem poifsFileSystem = new POIFSFileSystem(new FileInputStream(file));
			HSSFWorkbook hssfWorkbook = new HSSFWorkbook(poifsFileSystem);
			HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);

			int rowStart = hssfSheet.getFirstRowNum();
			int rowEnd = hssfSheet.getLastRowNum();
			if (rowStart >= 0)
				for (int r = 1; r <= rowEnd; r++) {
					Map<String, String> tempMap = new HashMap<String, String>();
					HSSFRow row = hssfSheet.getRow(r);
					if (row != null) {
						for (int i = 0; i < titleOrder.length; i++) {
							HSSFCell cell = row.getCell(i);
							if ((titleOrder[i] != null) && (cell != null)) {
								String content = getCellContent(cell);
								if (StringUtils.isNotBlank(content))
									tempMap.put(getCheckedString(getTitleName(titleOrder[i].trim(), isLower)), content);
							}
							// if ((titleOrder[i] != null) && (cell != null))
							// System.out.println(titleOrder[i] + " " +
							// getCellContent(cell));
						}
					}
					contentList.add(tempMap);
				}

			hssfWorkbook.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		return contentList;
	}
	
	public static List<Map<String, String>> getFileContentMap(byte[] fileContent) {
		return getFileContentMap(fileContent, true);
	}

	public static List<Map<String, String>> getFileContentMap(byte[] fileContent, boolean isLower) {
		List<Map<String, String>> contentList = new ArrayList<Map<String, String>>();
		String[] titleOrder = getFileTitleOrder(fileContent);

		try {
			InputStream is = new ByteArrayInputStream(fileContent);
			POIFSFileSystem poifsFileSystem = new POIFSFileSystem(is);
			HSSFWorkbook hssfWorkbook = new HSSFWorkbook(poifsFileSystem);
			HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);

			int rowStart = hssfSheet.getFirstRowNum();
			int rowEnd = hssfSheet.getLastRowNum();
			if (rowStart >= 0)
				for (int r = 1; r <= rowEnd; r++) {
					Map<String, String> tempMap = new HashMap<String, String>();
					HSSFRow row = hssfSheet.getRow(r);
					if (row != null) {
						for (int i = 0; i < titleOrder.length; i++) {
							HSSFCell cell = row.getCell(i);
							if ((titleOrder[i] != null) && (cell != null)) {
								String content = getCellContent(cell);
								if (StringUtils.isNotBlank(content))
									tempMap.put(getCheckedString(getTitleName(titleOrder[i].trim(), isLower)), content);
							}
						}
					}
					contentList.add(tempMap);
				}

			hssfWorkbook.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		return contentList;
	}

	public static List<Map<String, String>> getFileContentMap2007(File file) {
		return getFileContentMap2007(file, true);
	}

	public static List<Map<String, String>> getFileContentMap2007(File file, boolean isLower) {
		List<Map<String, String>> contentList = new ArrayList<Map<String, String>>();
		String[] titleOrder = getFileTitleOrder2007(file);

		try {
			POIFSFileSystem poifsFileSystem = new POIFSFileSystem(new FileInputStream(file));
			HSSFWorkbook hssfWorkbook = new HSSFWorkbook(poifsFileSystem);
			HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);

			int rowStart = hssfSheet.getFirstRowNum();
			int rowEnd = hssfSheet.getLastRowNum();
			if (rowStart >= 0)
				for (int r = 1; r <= rowEnd; r++) {
					Map<String, String> tempMap = new HashMap<String, String>();
					HSSFRow row = hssfSheet.getRow(r);
					if (row != null) {
						for (int i = 0; i < titleOrder.length; i++) {
							HSSFCell cell = row.getCell(i);
							if ((titleOrder[i] != null) && (cell != null)) {
								String content = getCellContent(cell);
								if (StringUtils.isNotBlank(content))
									tempMap.put(getCheckedString(getTitleName(titleOrder[i].trim(), isLower)), content);
							}
							// if ((titleOrder[i] != null) && (cell != null))
							// System.out.println(titleOrder[i] + " " +
							// getCellContent(cell));
						}
					}
					contentList.add(tempMap);
				}

			hssfWorkbook.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		return contentList;
	}

	private static String getExcelFileName(String oldFileName, String fileType) {
		String[] strings = oldFileName.split("\\.");
		if (strings.length == 0)
			return oldFileName + "." + fileType;
		else {
			String extension = strings[strings.length - 1];
			return oldFileName.replace(extension, "." + fileType);
		}
	}

	public static String getExcelFileName(String oldFileName) {
		return getExcelFileName(oldFileName, EXCEL_2003_FILE_TYPE);
	}

	public static String getExcelFileName2007(String oldFileName) {
		return getExcelFileName(oldFileName, EXCEL_2007_FILE_TYPE);
	}

	public static boolean generateEmptyExcelFile(String fullFilePath) throws IOException {
		File file = new File(fullFilePath);
		return file.createNewFile();
	}

	public static boolean generateExcelFile(File file, List<String> titleList, List<List<String>> contentList) {
		System.out.println("generate Excel file: " + file.getAbsolutePath());

		try {
			HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
			int columnCount = titleList.size();
			int rowCount = contentList.size();
			HSSFSheet sheet = hssfWorkbook.createSheet("sheet1");

			for (int i = 0; i < columnCount; i++)
				sheet.setColumnWidth(i, 3500);

			HSSFFont font = hssfWorkbook.createFont();
			font.setFontName("Veradana");
			font.setBoldweight((short) 100);
			font.setFontHeight((short) 300);
			font.setColor(HSSFColor.BLUE.index);

			HSSFCellStyle style = hssfWorkbook.createCellStyle();
			style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
			style.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
			style.setFillForegroundColor(HSSFColor.LIGHT_TURQUOISE.index);
			style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

			style.setBottomBorderColor(HSSFColor.RED.index);
			style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style.setBorderTop(HSSFCellStyle.BORDER_THIN);

			style.setFont(font);

			HSSFRow titleRow = sheet.createRow(0);
			for (int c = 0; c < columnCount; c++) {
				HSSFCell cell = titleRow.createCell(c);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(new String(titleList.get(c).getBytes(), "utf-8"));
			}

			for (int r = 1; r < rowCount; r++) {
				HSSFRow contentRow = sheet.createRow(r);
				int c = 0;
				List<String> contents = contentList.get(r - 1);
				for (String content : contents) {
					HSSFCell cell = contentRow.createCell(c);
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					if (StringUtils.isNotBlank(content))
						cell.setCellValue(new String(content.getBytes(), "utf-8"));
					c++;
				}
			}

			FileOutputStream fos = new FileOutputStream(file);
			hssfWorkbook.write(fos);

			hssfWorkbook.close();
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public static byte[] generateExcelFileContent(String fileName, List<String> titleList,
			List<List<String>> contentList) {
		System.out.println("generate Excel file: " + fileName);

		try {
			HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
			int columnCount = titleList.size();
			int rowCount = contentList.size();
			HSSFSheet sheet = hssfWorkbook.createSheet("sheet1");

			for (int i = 0; i < columnCount; i++)
				sheet.setColumnWidth(i, 3500);

			HSSFFont font = hssfWorkbook.createFont();
			font.setFontName("Veradana");
			font.setBoldweight((short) 100);
			font.setFontHeight((short) 300);
			font.setColor(HSSFColor.BLUE.index);

			HSSFCellStyle style = hssfWorkbook.createCellStyle();
			style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
			style.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
			style.setFillForegroundColor(HSSFColor.LIGHT_TURQUOISE.index);
			style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

			style.setBottomBorderColor(HSSFColor.RED.index);
			style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style.setBorderTop(HSSFCellStyle.BORDER_THIN);

			style.setFont(font);

			HSSFRow titleRow = sheet.createRow(0);
			for (int c = 0; c < columnCount; c++) {
				HSSFCell cell = titleRow.createCell(c);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(new String(titleList.get(c).getBytes(), "utf-8"));
			}

			for (int r = 1; r <= rowCount; r++) {
				HSSFRow contentRow = sheet.createRow(r);
				int c = 0;
				List<String> contents = contentList.get(r - 1);
				for (String content : contents) {
					HSSFCell cell = contentRow.createCell(c);
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					if (StringUtils.isNotBlank(content))
						cell.setCellValue(new String(content.getBytes(), "utf-8"));
					c++;
				}
			}

			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			hssfWorkbook.write(byteArrayOutputStream);
			hssfWorkbook.close();

			return byteArrayOutputStream.toByteArray();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static boolean generateExcelFile2007(File file, List<String> titleList, List<List<String>> contentList) {
		System.out.println("generate Excel file: " + file.getAbsolutePath());

		try {
			HSSFWorkbook workbook = new HSSFWorkbook();
			int columnCount = titleList.size();
			int rowCount = contentList.size();
			HSSFSheet sheet = workbook.createSheet();

			HSSFRow titleRow = sheet.createRow(0);
			for (int c = 0; c < columnCount; c++) {
				HSSFCell cell = titleRow.createCell(c);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(titleList.get(c));
			}

			for (int r = 1; r < rowCount; r++) {
				HSSFRow contentRow = sheet.createRow(r);
				int c = 0;
				List<String> contents = contentList.get(r - 1);
				for (String content : contents) {
					HSSFCell cell = contentRow.createCell(c);
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue(new String(content.getBytes(), "utf-8"));
					c++;
				}
			}

			FileOutputStream fos = new FileOutputStream(file);
			workbook.write(fos);

			workbook.close();
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

}
