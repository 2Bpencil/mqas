package com.tyf.mqas.utils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.tyf.mqas.base.dto.PoiDto;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFHyperlink;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;



public class PoiUtil {

	public static DateFormat DATEFORMAT_TIME = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static DateFormat DATEFORMAT_DATE = new SimpleDateFormat("yyyy-MM-dd");
	public static DateFormat DATEFORMAT = new SimpleDateFormat("yyyyMMdd");
	public static DecimalFormat DF_DECIMAL2 = new DecimalFormat("##.00");

	public static String FORMAT_TIME = "yyyy-MM-dd HH:mm:ss";
	public static String FORMAT_DATE = "yyyy-MM-dd";
	
	public static String REGEX_IP = "((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))";//IP正则

	// 导出标题列超链接后缀
	public static final String LINK_FLAG = "@#@";

	/*
	 * 导出内容列超链接拼接符，格式：'#'+sheetName+'!'+colNum+rowNum+LINK_JOIN_FLAG+cellValue
	 * 注：（LINK_JOIN_FLAG+cellValue）可不写，默认cellValue为“详情”
	 */
	public static final String LINK_JOIN_FLAG = "@#@";
	// 同Excel中超链的颜色
	private static final XSSFColor FONT_COLOR_HYPERLINK = new XSSFColor(new byte[] { 5, 99, (byte) 193 });
	/**
	 * 导入模板文件的列头
	 */
	// --------------------------------------------------------------导入列头Start--------------------------------------------------------------------------
	// 设备
	public static final String[] IMPORT_ITCOMP = new String[] { "管理地址", "设备类型", "站点（区域）", "位置", "业务名", "主机名", "是否可网管", "共同体（读）", "端口", "连接超时（ms）","是否可Ping","所属项目","所属设备组"};

	// --------------","-----------------------------------------------导入列头End--------------------------------------------------------------------------
	// --------------------------------------------------------------导出列头Start--------------------------------------------------------------------------
	// 设备
	public static final String[] Export_ITCOMP = new String[] { "管理地址", "设备类型", "站点（区域）", "位置",  "是否可网管", "是否可Ping", "重要程度","业务名", "主机名","用途","描述","凭证信息@#@" };
	//凭证
	public static final String[] Export_CREDENCE = new String[] {"IP地址","凭证类型", "共同体(只读)", "共同体(写)", "端口",  "用户名", "密码", "连接超时(毫秒)","状态", "可用性","实例名", "认证方式", "认证密码", "加密方式", "加密密码"};
	
	
	public static final String[] Export_LOGGINGEVENT = new String[]{"时间 ","日志级别","操作人","日志内容","结果"};
	// --------------","-----------------------------------------------导出列头End--------------------------------------------------------------------------

	/**
	 * 检查导入的文件列头是否和模板吻合
	 * 
	 * @param importFile
	 * @param heads
	 * @return
	 */
	public static boolean checkImportFileHead(MultipartFile importFile, String[] heads) {
		Workbook wb = null;
		try {
			wb = new XSSFWorkbook(importFile.getInputStream());
			Row headRow = wb.getSheetAt(0).getRow(0);
			if(headRow == null){
				return false;
			}

			for (int i = 0; i < heads.length; i++) {
				if(!heads[i].equals(headRow.getCell(i).getStringCellValue())){
					return false;
				}
			}
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (wb != null){
                    wb.close();
                }
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * 读取importFile指定列数的全部行（首行表头不计） 注：只读取第一个sheet，0位记录行号
	 * 
	 * @param importFile
	 * @param colNum
	 *            读取列数（从左开始计算）
	 * @return
	 */
	public static List<String[]> readImportFile(MultipartFile importFile, int colNum) {
		Workbook wb = null;
		List<String[]> dataList = null;
		try {
			wb = new XSSFWorkbook(importFile.getInputStream());
			Sheet sheet = wb.getSheetAt(0);
			if (sheet.getLastRowNum() == 0) {
				return dataList;
			}
			dataList = new ArrayList<String[]>();
			Row row = null;
			String[] data = null;
			int emptyIndex = 0;
			// 跳过列头
			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				row = sheet.getRow(i);
				if (row == null){
                    continue;
                }
				data = new String[colNum + 1];
				data[0] = String.valueOf(i + 1);// 0格记录行号
				emptyIndex = 0;// 记录空Cell数
				for (int j = 0; j < colNum; j++) {
					data[j + 1] = getCellValue(row.getCell(j));
					if (StringUtils.isBlank(data[j + 1])){
                        emptyIndex++;
                    }
				}
				if (emptyIndex < colNum){
                    dataList.add(data);
                }
			}
			return dataList;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (wb != null){
                    wb.close();
                }
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return dataList;
	}

	public static Workbook exportWorkBookByList(String[] titles, List<String[]> dataList) {
		Workbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet();
		if (titles != null && titles.length > 0) {
			for (int i = 0; i < titles.length; i++) {
				sheet.setColumnWidth(i, 2500);// 默认宽度
			}
		}

		makeTitle(wb, sheet, titles);// 写入标题

		if (CollectionUtils.isEmpty(dataList)) {
			sheet.createRow(1).createCell(0).setCellValue("无符合条件的记录！");// 无数据
			return wb;
		}

		CellStyle contentStyle = getContentStyle(wb);

		String[] data = null;
		Row row = null;
		Cell cell = null;
		for (int i = 0; i < dataList.size(); i++) {
			data = dataList.get(i);
			row = sheet.createRow(i + 1);
			for (int j = 0; j < titles.length; j++) {
				cell = row.createCell(j);
				cell.setCellStyle(contentStyle);
				cell.setCellValue(data[j]);
			}
		}
		return wb;
	}

	/**
	 * 多Sheet数据写入
	 * 
	 * @param dtos
	 * @return
	 * @author XHP
	 * @date 2016年12月6日 下午4:46:32
	 */
	public static Workbook exportWorkBookForMultyByPoiDtos(PoiDto... dtos) {
		XSSFWorkbook wb = new XSSFWorkbook();
		if (dtos == null || dtos.length == 0){
            return wb;
        }
		String[] titles = null;
		List<String[]> dataList = null;
		Sheet sheet = null;
		CellStyle contentStyle = getContentStyle(wb);
		CellStyle linkStyle = getHyperlinkStyle(wb);
		String[] linkVaules = null;
		for (PoiDto dto : dtos) {
			if (dto == null){
                continue;
            }

			if (StringUtils.isNotBlank(dto.getSheetName())){
                sheet = wb.createSheet(dto.getSheetName());
            }else{
                sheet = wb.createSheet();
            }
			titles = dto.getTitles();
			dataList = dto.getDataList();

			if (titles != null && titles.length > 0) {
				for (int i = 0; i < titles.length; i++) {
					sheet.setColumnWidth(i, 2500);// 默认宽度
				}
			}

			makeTitle(wb, sheet, titles);// 写入标题

			if (CollectionUtils.isEmpty(dataList)) {
				sheet.createRow(1).createCell(0).setCellValue("无符合条件的记录！");// 无数据
				continue;
			}
			String[] data = null;
			Row row = null;
			Cell cell = null;
			for (int i = 0; i < dataList.size(); i++) {
				data = dataList.get(i);
				row = sheet.createRow(i + 1);
				for (int j = 0; j < titles.length; j++) {
					cell = row.createCell(j);
					if (titles[j].endsWith(LINK_FLAG) && (StringUtils.isNotBlank(data[j]))) {
						linkVaules = data[j].split(LINK_JOIN_FLAG);
						CreationHelper creationHelper = wb.getCreationHelper();
						Hyperlink hl = creationHelper.createHyperlink(XSSFHyperlink.LINK_DOCUMENT);
						hl.setAddress(linkVaules[0]);
						cell.setHyperlink(hl);
						cell.setCellValue(linkVaules.length > 1 ? linkVaules[1] : "详情");
						cell.setCellStyle(linkStyle);
					} else {
						cell.setCellStyle(contentStyle);
						cell.setCellValue(data[j]);
					}
				}
			}
		}
		return wb;
	}
	private static void makeTitle(Workbook wb, Sheet sheet, String[] titles) {
		if (titles == null || titles.length == 0){
            return;
        }
		Row titleRow = sheet.createRow(0);
		titleRow.setHeightInPoints(20);
		Cell cell = null;
		Font font = wb.createFont();
		font.setFontName("微软雅黑");
		font.setFontHeightInPoints((short) 9);
		// font.setBoldweight(Font.BOLDWEIGHT_BOLD);// 加粗
		CellStyle titleStyle = wb.createCellStyle();
		// titleStyle.setBorderBottom(CellStyle.BORDER_THIN); // 下边框
		// titleStyle.setBorderLeft(CellStyle.BORDER_THIN);// 左边框
		// titleStyle.setBorderTop(CellStyle.BORDER_THIN);// 上边框
		// titleStyle.setBorderRight(CellStyle.BORDER_THIN);// 右边框
		titleStyle.setFont(font);
		titleStyle.setAlignment(CellStyle.ALIGN_CENTER);
		titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		for (int i = 0; i < titles.length; i++) {
			cell = titleRow.createCell(i);
			cell.setCellStyle(titleStyle);
			cell.setCellValue(titles[i]);
		}
	}

	private static CellStyle getContentStyle(Workbook wb) {
		Font font = wb.createFont();
		font.setFontName("微软雅黑");
		font.setFontHeightInPoints((short) 9);
		DataFormat format = wb.createDataFormat();
		CellStyle cellStyle = wb.createCellStyle();
		cellStyle.setFont(font);
		cellStyle.setDataFormat(format.getFormat("@"));
		// cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
		// cellStyle.setBorderBottom(CellStyle.BORDER_THIN); // 下边框
		// cellStyle.setBorderLeft(CellStyle.BORDER_THIN);// 左边框
		// cellStyle.setBorderTop(CellStyle.BORDER_THIN);// 上边框
		// cellStyle.setBorderRight(CellStyle.BORDER_THIN);// 右边框
		return cellStyle;
	}

	private static String getCellValue(Cell cell) {
		if (cell == null){
            return null;
        }
		String value = null;
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
				value = DATEFORMAT_TIME.format(cell.getDateCellValue());
			} else {
				value = NumberToTextConverter.toText(cell.getNumericCellValue());
			}
			break;
		case Cell.CELL_TYPE_STRING:
			value = cell.getStringCellValue();
			break;
		case Cell.CELL_TYPE_FORMULA:
			value = String.valueOf(cell.getCellFormula());
			break;
		case Cell.CELL_TYPE_BLANK:
			value = "";
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			value = String.valueOf(cell.getBooleanCellValue());
			break;
		case Cell.CELL_TYPE_ERROR:
			value = null;
			break;
		default:
			value = null;
		}

		return value == null ? null : value.trim();
	}

	/**
	 * 转换String2Integer
	 * 
	 * @param data
	 * @return
	 */
	public static Integer convertStringToInteger(String data) {
		Integer dataInt = null;
		if (StringUtils.isBlank(data))
			return null;
		try {
			dataInt = Integer.parseInt(data);
			return dataInt;
		} catch (NumberFormatException e) {

		}
		return null;
	}

	/**
	 * 获得超链接的CellStyle
	 * 
	 * @param wb
	 * @return CellStyle
	 * @author XHP
	 * @date 2016年11月29日 上午11:07:07
	 */
	private static CellStyle getHyperlinkStyle(XSSFWorkbook wb) {
		CellStyle linkStyle = wb.createCellStyle();
		XSSFFont font = wb.createFont();
		font.setUnderline((byte) 1);
		font.setColor(FONT_COLOR_HYPERLINK);
		font.setFontName("微软雅黑");
		font.setFontHeightInPoints((short) 10);
		linkStyle.setFont(font);
		linkStyle.setAlignment(CellStyle.ALIGN_CENTER);
		linkStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		linkStyle.setBorderBottom(CellStyle.BORDER_THIN); // 下边框
		linkStyle.setBorderLeft(CellStyle.BORDER_THIN);// 左边框
		linkStyle.setBorderTop(CellStyle.BORDER_THIN);// 上边框
		linkStyle.setBorderRight(CellStyle.BORDER_THIN);// 右边框
		return linkStyle;
	}
	
	/**
	 * 转换String2Double
	 * 
	 * @param data
	 * @return
	 */
	public static Double convertStringToDouble(String data) {
		Double dataDouble = null;
		if (StringUtils.isBlank(data)){
            return null;
        }
		try {
			dataDouble = Double.parseDouble(data);
			return dataDouble;
		} catch (NumberFormatException e) {
		}
		return null;
	}

	/**
	 * 文件是否Excel2003
	 * 
	 * @param fileName
	 * @return
	 */
	public static boolean isExcel2003(String fileName) {
		return fileName.matches("^.+\\.(?i)(xls)$");
	}

	/**
	 * 文件是否Excel2007+
	 * 
	 * @param filePath
	 * @return
	 */
	public static boolean isExcel2007(String filePath) {
		return filePath.matches("^.+\\.(?i)(xlsx)$");
	}

	public static void main(String[] args) {
//		System.out.println("288.168.11.11".matches(REGEX_IP));
	}
}
