package com.sr178.game.tool;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.Map;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.sr178.game.tool.parse.ExcelCellToJsonTool;
import com.sr178.game.tool.parse.ExcelParse;
import com.sr178.game.tool.util.ConfigParseExcel;
import com.sr178.game.tool.util.ExcelUtil;
import com.sr178.game.tool.util.GeneratorType;


public class ExcelToClientJson {
	//起始行
//	private static final int start_colum=2;
	
	public static void main(String[] args) throws SQLException, UnsupportedEncodingException, IOException {
		File file = new File(Config.init().EXCEL_DERECTORY);
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
				int departMentClumn = excelParse.getDepartMentKeyClumn();
				int departmentNum = excelParse.getDepartMentNum();
				departmentNum = (departmentNum==0?1:departmentNum);
//				for(int n=0;n<departmentNum;n++){
				String fileName = excelParse.getTableName();
				ExcelCellToJsonTool cellToJsonTool = new ExcelCellToJsonTool(fileName,departmentNum);
				System.out.println("fileName="+f[i].getName()+"sheetName="+tempSheet.getSheetName()+"rowNum="+excelParse.rowNum());
			    int date_column = excelParse.getDateColumn();
			    System.out.println(tempSheet.getSheetName()+"数据一共有"+date_column+"列");
			    ConfigParseExcel config = new ConfigParseExcel(excelParse,GeneratorType.CLIENT);
				cellToJsonTool.start();
			    int passCount = config.getPassSize();
			    int haveCount = date_column-passCount;
			    if(haveCount<=0){
			    	System.out.println(f[i].getName()+"客户端不需要该sheet，跳过该shell的生成"+tempSheet.getSheetName());
			    	continue;
			    }
			    int insertCount = 0;
			    int date_start_row = excelParse.getDateStartRow();
			    System.out.println(tempSheet.getSheetName()+"开始行数"+date_start_row);
			    int date_end_row = excelParse.getDateEndRow();
			    System.out.println(tempSheet.getSheetName()+"结束行数"+date_end_row);
			    long time1 = System.currentTimeMillis();
			    System.out.println("start内存组合文件的时间"+time1);
				for(int j=0;j<date_column;j++){
					if(!config.isPass(j)){
						Map<Integer,String> oneEntry = cellToJsonTool.excelColumsToJson(excelParse.getColum(j),(departMentClumn==0?null:excelParse.getColum(departMentClumn-1)),date_start_row,date_end_row);
						if(insertCount==haveCount-1){
							cellToJsonTool.insertOneEntry(oneEntry);
						}else{
							for(Integer mk:oneEntry.keySet()){
								oneEntry.put(mk, oneEntry.get(mk)+",");
							}
							cellToJsonTool.insertOneEntry(oneEntry);
						}
						insertCount++;
					}
				}
				cellToJsonTool.end();
			    long time1_end = System.currentTimeMillis();
			    System.out.println("start内存组合文件的时间"+time1_end+",花时="+(time1_end-time1));
			    
			    long time2 = System.currentTimeMillis();
			    System.out.println("start文件生成时间"+time2);
				cellToJsonTool.writeFile();
				long time2_end = System.currentTimeMillis();
				System.out.println("start内存组合文件的时间"+time2_end+",花时="+(time2_end-time2));
				}
			   }
//			  }
		}else{
			throw new RuntimeException("Excel目录获取失败！！！"+Config.init().EXCEL_DERECTORY);
		}
    }
}
