package com.sr178.game.tool;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.sr178.game.tool.mysql.ConnectionManager;
import com.sr178.game.tool.parse.ExcelCellToSql;
import com.sr178.game.tool.parse.ExcelParse;
import com.sr178.game.tool.util.ConfigParseExcel;
import com.sr178.game.tool.util.ExcelUtil;
import com.sr178.game.tool.util.GeneratorType;


/**
 * excel导入mysql
 * @author magical
 *
 */
public class ExcelToMysql {
	public static void main(String[] args) throws SQLException, IOException {
		File file = new File(Config.init().EXCEL_DERECTORY);
		System.out.println(file.getCanonicalPath());
		if(file.isDirectory()){
			File[] f = file.listFiles(); 
			for(int i=0;i<f.length;i++){
				Workbook workBook = ExcelUtil.getWorkBookFromFile(f[i]);
				for(int m=0;m<workBook.getNumberOfSheets();m++){
					Sheet tempSheet = 	workBook.getSheetAt(m);
					ExcelParse excelParse = new ExcelParse(f[i],tempSheet);
					if(excelParse.isPassed()){
						continue;
					}
				
				String tableName = excelParse.getTableName();
                int dateColumNum = excelParse.getDateColumn();
				
			    int date_start_row = excelParse.getDateStartRow();
			    System.out.println(tempSheet.getSheetName()+"，开始行数"+date_start_row);
			    int date_end_row = excelParse.getDateEndRow();
			    System.out.println(tempSheet.getSheetName()+"，结束行数"+date_end_row);
			    
			    Cell[] headCell = excelParse.getCell(date_start_row-1);
			    ConfigParseExcel config = new ConfigParseExcel(excelParse,GeneratorType.SERVER);
				    if(config.getPassSize()==dateColumNum){
				    	System.out.println(f[i].getName()+"服务端不需要该sheet，跳过该shell的生成"+tempSheet.getSheetName());
				    	continue;
				    }
				ExcelCellToSql cellToSql = new ExcelCellToSql(config.getIgoList(), headCell,dateColumNum, tableName);
				System.out.println("fileName="+f[i].getName()+"sheetName="+tempSheet.getSheetName()+"rowNum="+excelParse.rowNum());
				//清理數據線
				int type = excelParse.getConnectionType();
				ConnectionManager.getInstance().executeUpdate(type,"delete from "+tableName);
				for(int j=date_start_row;j<date_end_row;j++){
					String sqlTemp = cellToSql.cellToSql(excelParse.getCell(j));
					System.out.println(sqlTemp);
					ConnectionManager.getInstance().executeUpdate(type,sqlTemp);
				}
			}
		  }
		}else{
			throw new RuntimeException("Excel目录获取失败！！！"+Config.init().EXCEL_DERECTORY);
		}
    }
}