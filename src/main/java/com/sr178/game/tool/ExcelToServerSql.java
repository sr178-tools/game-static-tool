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
import com.sr178.game.tool.util.FileCreatUtil;
import com.sr178.game.tool.util.GeneratorType;


/**
 * excel导出sql语句
 * @author magical
 *
 */
public class ExcelToServerSql {
	public static void main(String[] args) throws SQLException, IOException {
		String configSqlFileName = "staticupdate_config.sql";
		String commonSqlFileName = "staticupdate_common.sql";
		File file = new File(Config.init().EXCEL_DERECTORY);
		StringBuffer fileConfigContent = new StringBuffer(); 
		StringBuffer fileCommonContent = new StringBuffer();
		StringBuffer temp = null;
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
			    int date_start_row = excelParse.getDateStartRow();
			    System.out.println(tempSheet.getSheetName()+"开始行数"+date_start_row);
			    int date_end_row = excelParse.getDateEndRow();
			    System.out.println(tempSheet.getSheetName()+"结束行数"+date_end_row);
				
				Cell[] headCell = excelParse.getCell(date_start_row-1);
				
				String tableName = excelParse.getTableName();
                int dateColumNum = excelParse.getDateColumn();
                ConfigParseExcel config = new ConfigParseExcel(excelParse,GeneratorType.SERVER);
			    if(config.getPassSize()==dateColumNum){
			    	System.out.println(f[i].getName()+"服务端不需要该sheet，跳过该shell的生成"+tempSheet.getSheetName());
			    	continue;
			    }
				ExcelCellToSql cellToSql = new ExcelCellToSql(config.getIgoList(), headCell,dateColumNum,tableName);
				int type = excelParse.getConnectionType();
				if(type==ConnectionManager.CONFIG){
					temp = fileConfigContent;
				}else{
					temp = fileCommonContent;
				}
				//特殊处理系统内部邮件的sql 不导入限时抽奖的邮件
				if(tableName.equals("system_mail_internal")){
					temp.append("delete from "+tableName+" where internal_mail_id!=2001 and internal_mail_id!=2002 and internal_mail_id!=2003 and internal_mail_id!=2004 and internal_mail_id!=2005;");
				}else{
					temp.append("delete from "+tableName+";");
				}
				temp.append("\n");
				for(int j=date_start_row;j<date_end_row;j++){
					String sqlTemp = cellToSql.cellToSql(excelParse.getCell(j));
					temp.append(sqlTemp);
					temp.append("\n");
				}
				System.out.println("fileName="+f[i].getName()+"sheetName="+tempSheet.getSheetName()+"--rowNum="+excelParse.rowNum());
			 }
			}
			FileCreatUtil.outFile(Config.init().SQL_File, configSqlFileName, fileConfigContent.toString());
			System.out.println("----写入sql文件成功--文件存在--"+Config.init().SQL_File+"\\"+configSqlFileName);
			FileCreatUtil.outFile(Config.init().SQL_File, commonSqlFileName, fileCommonContent.toString());
			System.out.println("----写入sql文件成功--文件存在--"+Config.init().SQL_File+"\\"+commonSqlFileName);
			
		}else{
			throw new RuntimeException("Excel目录获取失败！！！"+Config.init().EXCEL_DERECTORY);
		}

    }
}