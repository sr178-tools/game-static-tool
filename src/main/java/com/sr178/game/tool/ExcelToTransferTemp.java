package com.sr178.game.tool;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.sr178.game.tool.parse.ExcelParse;
import com.sr178.game.tool.util.ExcelUtil;

/**
 * 转成翻译模板
 * @author Administrator
 *
 */
public class ExcelToTransferTemp {

	public static void main(String[] args) {
		File file = new File(Config.init().EXCEL_DERECTORY);
		Map<String,String> haveAreadyTransfer =  getHaveAreadyTransferKeyValue();
		if(file.isDirectory()){
			File[] f = file.listFiles(); 
			for (int i = 0; i < f.length; i++) {
				if(f[i].isDirectory()){
					continue;
				}
				Workbook workBook = ExcelUtil.getWorkBookFromFile(f[i]);
				Workbook transferTempBook = ExcelUtil.createExcelFile();
				boolean isNeedCreatWorkBook = false;
				for (int m = 0; m < workBook.getNumberOfSheets(); m++) {
					Sheet tempSheet = workBook.getSheetAt(m);
					ExcelParse excelParse = new ExcelParse(f[i], tempSheet);
					if (excelParse.isPassed()) {
						continue;
					}
					Sheet transferTempSheet = null;
					int date_start_row = excelParse.getDateStartRow();
					System.out.println(tempSheet.getSheetName() + "开始行数"
							+ date_start_row);
					int date_end_row = excelParse.getDateEndRow();
					System.out.println(tempSheet.getSheetName() + "结束行数"
							+ date_end_row);
					int dateColumNum = excelParse.getDateColumn();
//					ConfigParseExcel config = new ConfigParseExcel(excelParse,
//							GeneratorType.NONE);
                    
                    Map<Integer,List<String>> mapTemp = new HashMap<Integer,List<String>>();
					for (int j = date_start_row; j < date_end_row; j++) {
						Cell[] tempRowCells = excelParse
								.getCell(j);
						if(tempRowCells==null){
							continue;
						}
                        for(int k=0;k<dateColumNum;k++){
                        	String tempValue = ExcelUtil.getContent(tempRowCells[k]);
                        	if(isChineseChar(tempValue)){
                        		if(tempValue.equals("LabelChineseStr.ActivityUIPanel_2 = \"級開放天界商店\"")){
                        			System.out.println("");
                        		}
                        		//已翻译的不需要倒出了
                        		if(haveAreadyTransfer.containsKey(tempValue)){
                        			ExcelUtil.addCellValue(tempSheet, j, k, haveAreadyTransfer.get(tempValue));
                        			continue;
                        		}
                        		if(transferTempSheet == null){
                        			isNeedCreatWorkBook = true;
                        			transferTempSheet = ExcelUtil.addSheet(transferTempBook, tempSheet.getSheetName());
                        		}
                        		if(mapTemp.containsKey(k)){
                        			mapTemp.get(k).add(tempValue);
                        		}else{
                        			List<String> tempList = new ArrayList<String>();
                        			tempList.add(tempValue);
                        			mapTemp.put(k, tempList);
                        		}
                        	}
                        }
					}
					int allRowNum=0;
					Map<String,String> haveAdd = new HashMap<String,String>();//过滤掉重复添加的用户
					for(Entry<Integer,List<String>> entry:mapTemp.entrySet()){
						for(String str:entry.getValue()){
							if(!haveAdd.containsKey(str)){
								ExcelUtil.addCellValue(transferTempSheet, allRowNum, 0, str);
								haveAdd.put(str, null);
								allRowNum++;
							}
						}
					}
					System.out.println("fileName=" + f[i].getName()
							+ "sheetName=" + tempSheet.getSheetName()
							+ "--rowNum=" + excelParse.rowNum());
				}
				if(isNeedCreatWorkBook){
				ExcelUtil.outExcelFile(Config.init().needTransferTempDirectory,f[i].getName(), transferTempBook);
				}
				//输出翻译后的文件
				ExcelUtil.outExcelFile(Config.init().transferTargetDirectory,f[i].getName(), workBook);
			}
			System.out.println("ok!~~~生成完毕！");
//			  }
		}else{
			throw new RuntimeException("Excel目录获取失败！！！"+Config.init().EXCEL_DERECTORY);
		}
    }
	
	public static Map<String,String> getHaveAreadyTransferKeyValue(){
		File file = new File(Config.init().transferTempDirectory);
		List<File> fileList = new ArrayList<File>();
		Map<String,String> map = new HashMap<String,String>();
		getAllExcelFile(file,fileList);
		for(File fileTemp:fileList){
			Workbook workBook = ExcelUtil.getWorkBookFromFile(fileTemp);
			for (int m = 0; m < workBook.getNumberOfSheets(); m++) {
				Sheet tempSheet = workBook.getSheetAt(m);
				int k=0;
				while(true){
					Row row = tempSheet.getRow(k);
					if(row!=null){
						Cell cell = row.getCell(0);
						if(cell!=null){
							String key = ExcelUtil.getContent(cell);
							Cell cellValue = row.getCell(1);
							String value = "";
							if(cellValue!=null){
								value = ExcelUtil.getContent(cellValue);
							}
							map.put(key, value);
						}
						k++;
					}else{
						break;
					}
				}
			}
		}
		return map;
	}
	
	public static void getAllExcelFile(File file,List<File> fileList){
		if(file.isDirectory()){
			File[] f = file.listFiles();
			for(int i = 0; i < f.length; i++){
				if(f[i].isDirectory()){
					getAllExcelFile(f[i],fileList);
				}else{
					fileList.add(f[i]);
				}
			}
		}else{
			fileList.add(file);
		}
	}
	
	public static boolean isChineseChar(String str){
		if(str==null||str.equals("")) return false;
        boolean temp = false;
        Pattern p=Pattern.compile("[\u4e00-\u9fa5]"); 
        Matcher m=p.matcher(str); 
        if(m.find()){ 
            temp =  true;
        }
        return temp;
    }
}
