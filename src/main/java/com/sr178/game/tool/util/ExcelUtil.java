package com.sr178.game.tool.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class ExcelUtil {
	   /**
	    * 获取所有sheet
	    * @param file
	    * @return
	    */
	   public static Workbook getWorkBookFromFile(File file){
		   System.out.println("开始解析文件"+file.getName());
		   InputStream inp=null;
				try {
					inp = new FileInputStream(file);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} 
				Workbook wb = null;
				try {
					wb = WorkbookFactory.create(inp);
				} catch (InvalidFormatException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} 
		        return wb; 
	   }
	 /**
	  * 创建excel
	  * @return
	  */
	 public static Workbook createExcelFile() {
		        Workbook workbook = null;
		        try {
		            // XSSFWork used for .xslx (>= 2007), HSSWorkbook for 03 .xsl
		            workbook = new XSSFWorkbook();//HSSFWorkbook();//WorkbookFactory.create(inputStream);
		        }catch(Exception e) {
		            System.out.println("It cause Error on CREATING excel workbook: ");
		            e.printStackTrace();
		        }
		        return workbook;
	  }
	 /**
	  * 输出excel文件
	  * @param path
	  * @param workbook
	  */
	 public static void outExcelFile(String path,String name,Workbook workbook){
		 try {
			 
			    String sDirF = System.getProperty("file.separator");
				// 检测指定路径
				if (path.substring(path.length() - 1).equals(sDirF)) {
					path = path.substring(0, path.length() - 1);
				}

				File pathDir = null;
				File file = null;
					// 创建目录
					pathDir = new File(path + sDirF );
					if (!(pathDir.isDirectory())) {
						if (!(pathDir.mkdirs())) {
							System.out.println("制定输出目录错误"+pathDir);
						}
					}
					file = new File(path + sDirF +  name);
					System.out.println("开始创建文件："+file);
					if (file.exists()) {
						file.delete();
					}
					if (pathDir != null) {
						pathDir = null;
					}
					if (file != null) {
						file = null;
					}
             FileOutputStream outputStream = new FileOutputStream(path + sDirF +  name);
             workbook.write(outputStream);
             outputStream.flush();
             outputStream.close();
         } catch (Exception e) {
             System.out.println("It cause Error on WRITTING excel workbook: ");
             e.printStackTrace();
         }
	 }
     /**
      * 
      * @param workBook
      * @param sheetName
      * @return
      */
	public static Sheet addSheet(Workbook workBook, String sheetName) {
		if (workBook == null) {
			return null;
		}
		Sheet sheet = workBook.createSheet(sheetName);
//		Row row0 = sheet.createRow(0);
//		for (int i = 0; i < 11; i++) {
//			Cell cell_1 = row0.createCell(i, Cell.CELL_TYPE_STRING);
//			cell_1.setCellValue("HELLO" + i + "Column");
//			sheet.autoSizeColumn(i);
//		}
//		for (int rowNum = 1; rowNum < 200; rowNum++) {
//			Row row = sheet.createRow(rowNum);
//			for (int i = 0; i < 11; i++) {
//				Cell cell = row.createCell(i, Cell.CELL_TYPE_STRING);
//				cell.setCellValue("cell" + String.valueOf(rowNum + 1)
//						+ String.valueOf(i + 1));
//			}
//		}
		return sheet;
	}
	 /**
	  * 添加一个格子内容
	  * @param sheet
	  * @param rowIndex   0开始计算
	  * @param cellIndex  0开始计算
	  * @param value
	  */
	 public static void addCellValue(Sheet sheet,int rowIndex,int cellIndex,String value){
		 Row row = sheet.getRow(rowIndex);
		 if(row==null){
			 row = sheet.createRow(rowIndex);
		 }
		 Cell cell = row.getCell(cellIndex);
		 if(cell==null){
			 cell = row.createCell(cellIndex, Cell.CELL_TYPE_STRING);
		 }
		 cell.setCellValue(value);
	 }
	 
	 public static void main(String[] args) {
		 Workbook wb  = createExcelFile();
		 Sheet sheet = addSheet(wb,"测试sheet");
		 addCellValue(sheet,0,0,"value");
		 outExcelFile("D:\\数据目录\\","bbc.xlsx", wb);
	}  
	   public static Workbook getWorkBookFromFile(InputStream inp){
				Workbook wb = null;
				try {
					wb = WorkbookFactory.create(inp);
				} catch (InvalidFormatException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} 
		        return wb; 
	   }
	   /**
	    * 获取表格中cell的内容 统一转成string
	    * @param cell
	    * @return
	    */
	   public static String getContent(Cell cell){
			 String value = null;
			 if(cell==null){
				 return null;
			 }
	         switch (cell.getCellType()) {
	         case HSSFCell.CELL_TYPE_FORMULA:
	             value = cell.getCellFormula();
	             break;
	         case HSSFCell.CELL_TYPE_NUMERIC:
	             value = cell.getNumericCellValue() + "";
	             if (value.endsWith(".0")) {
	                 value = value.substring(0, value.length() - 2);
	             }
	             break;
	         case HSSFCell.CELL_TYPE_STRING:
	             value = cell.getStringCellValue();
	             break;
	         default:
	        	 value = cell.getStringCellValue();
	         }
	         return value;
		}
	    public Map<String, Object> readExcel(File excel) {
	        Map<String, Object> result = new HashMap<String, Object>();
	        result.put("success", false);
	        FileInputStream fis = null;
	        Workbook wb = null;
	        int saveCount = 0;
	        try {
	            try {
	                fis = new FileInputStream(excel);
	                wb = WorkbookFactory.create(fis);
	                for (int s = 0; s < wb.getNumberOfSheets(); s++) {
	                    Sheet sheet = wb.getSheetAt(s);
	                    int rowNum = sheet.getPhysicalNumberOfRows();
	                    if (rowNum < 1) {
	                        result.put("msg", "导入文件中没有数据");
	                        return result;
	                    }
	                    for (int r = 0; r < rowNum; r++) {
	                        Row row = sheet.getRow(r);
	                        if (row == null) {
	                            continue;
	                        }
	                        int cells = row.getPhysicalNumberOfCells();
	                        for (int c = 0; c < cells; c++) {
	                            Cell cell = row.getCell(c);
	                            String value = null;
	                            switch (cell.getCellType()) {
	                            case HSSFCell.CELL_TYPE_FORMULA:
	                                value = cell.getCellFormula();
	                                break;
	                            case HSSFCell.CELL_TYPE_NUMERIC:
	                                value = cell.getNumericCellValue() + "";
	                                if (value.endsWith(".0")) {
	                                    value = value.substring(0, value.length() - 2);
	                                }
	                                break;
	                            case HSSFCell.CELL_TYPE_STRING:
	                                value = cell.getStringCellValue();
	                                break;
	                            default:
	                            }
	                            System.out.println(value);
	                        }
	                    }
	                }
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        } finally {
	            try {
	                fis.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	        result.put("msg", "导入[" + saveCount + "]条数据");
	        result.put("success", true);
	        return result;
	    }
}
