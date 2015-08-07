package com.sr178.game.tool.parse;

import java.io.File;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.sr178.game.tool.util.ExcelUtil;



public class ExcelParse {
	private Sheet sheet;
	private File file;
	public static final String START_TAG = "#START_TAG#";
	public static final String END_TAG = "#END_TAG#";
	public static final String PASSED_TAG = "#PASS_TAG#";
	public ExcelParse(File file,Sheet sheet){
			this.sheet = sheet; 
			this.file = file;
	}
	/**
	 * 是否跳过该sheet
	 * @return
	 */
	public boolean isPassed(){
		Row row = this.getRow(0);
		if(row==null){
			System.out.println(file.getName()+"跳过sheet"+sheet.getSheetName());
			return true;
		}
		String tableName = ExcelUtil.getContent(row.getCell(0));
		if(tableName==null||tableName.equals("")){
			throw new RuntimeException(file.getName()+"---["+sheet.getSheetName()+"]无表名，请在第一行第一列填入表名！或填入跳过标识"+PASSED_TAG);
		}
		if(PASSED_TAG.equals(tableName)){
			System.out.println(file.getName()+"跳过sheet"+sheet.getSheetName());
			return true;
		}
		return false;
	}
	
	/**
	 * 获取表名
	 * @return
	 */
	public String getTableName(){
		Row row = this.getRow(0);
		if(row==null){
			return null;
		}
		Cell cell = row.getCell(0);
		if(cell==null){
			return null;
		}
		String tableName = ExcelUtil.getContent(cell);
		if(tableName==null||tableName.equals("")){
			throw new RuntimeException(file.getName()+"---["+sheet.getSheetName()+"]无表名，请在第一行第一列填入表名！");
		}
		return tableName;
	}
	/**
	 * 如果该表需要在客户端拆分 则获取拆分KEY所在的列
	 * @return
	 */
	public int getDepartMentKeyClumn(){
		Row row = this.getRow(0);
		if(row==null){
			return 0;
		}
		Cell cell = row.getCell(2);
		if(cell==null){
			return 0;
		}
		String departmentKeyName = ExcelUtil.getContent(cell);
		if(departmentKeyName==null){
			return 0;
		}
		int keyClumn = 0;
		try {
			keyClumn = Integer.valueOf(departmentKeyName);
		} catch (Exception e) {
			throw new RuntimeException(file.getName()+"---["+sheet.getSheetName()+"]列数标识为非整形,现在的值为："+departmentKeyName+"，请在第一行第三列填入分表所在列数！");
		}
		return keyClumn;
		
	}
	
	/**
	 * 拆分文件个数
	 * @return
	 */
	public int getDepartMentNum(){
		Row row = this.getRow(0);
		if(row==null){
			return 0;
		}
		Cell cell = row.getCell(3);
		if(cell==null){
			return 0;
		}
		String departMentNumStr = ExcelUtil.getContent(cell);
		if(departMentNumStr==null){
			return 0;
		}
		int departMentNum = 0;
		try {
			departMentNum = Integer.valueOf(departMentNumStr);
		} catch (Exception e) {
			throw new RuntimeException(file.getName()+"---["+sheet.getSheetName()+"]列数标识为非整形,现在的值为："+departMentNumStr+"，请在第一行第四列填入数据列数！");
		}
		return departMentNum;
	}
	
	
	public int getConnectionType(){
		Row row = this.getRow(0);
		if(row==null){
			return 0;
		}
		Cell cell = row.getCell(4);
		if(cell==null){
			return 0;
		}
		String typeString = ExcelUtil.getContent(cell);
		if(typeString==null||typeString.equals("")){
			return 0;
		}
		int type = 0;
		try {
			type = Integer.valueOf(typeString);
		} catch (Exception e) {
			throw new RuntimeException(file.getName()+"---["+sheet.getSheetName()+"]连接类型："+typeString+"，请在第一行第五列填入连接类型  1 config or  2 common！");
		}
		return type;
	}
	/**
	 * 获取数据列数
	 * @return
	 */
	public int getDateColumn(){
		Row row =  sheet.getRow(0);
		String endColumnStr = ExcelUtil.getContent(row.getCell(1));
		if(endColumnStr==null||endColumnStr.equals("")){
				throw new RuntimeException(file.getName()+"---["+sheet.getSheetName()+"]无列数标识，请在第一行第二列填入数据列数！");
		}
		int endColumn = 0;
		try {
			endColumn = Integer.valueOf(endColumnStr);
		} catch (Exception e) {
			throw new RuntimeException(file.getName()+"---["+sheet.getSheetName()+"]列数标识为非整形,现在的值为："+endColumnStr+"，请在第一行第二列填入数据列数！");
		}
		return endColumn;
	}
	/**
	 * 获取数据开始行 （包括字段名称行） 第一行返回 1  第二行 返回2
	 * @return
	 */
	public int getDateStartRow(){
		int rowNum = rowNum();
		for(int i=0;i<rowNum;i++){
			Row row =  sheet.getRow(i);
			if(row!=null){
				Cell cell = row.getCell(0);
				if(cell!=null){
				String content = ExcelUtil.getContent(cell);
				if(START_TAG.equals(content)){
					return i+2;
				}
				}
			}
		}
		throw new RuntimeException(file.getName()+"---["+sheet.getSheetName()+"]数据没有定义开始行，请在数据开始前（数据行包括列名那一行） 第一行，第一列写入"+START_TAG);
	}
	/**
	 * 获取数据结束行  第一行返回 1  第二行 返回2
	 * @return
	 */
	public int getDateEndRow(){
		int rowNum = rowNum();
		for(int i=0;i<rowNum;i++){
			Row row =  sheet.getRow(i);
			if(row!=null){
				String content = ExcelUtil.getContent(row.getCell(0));
				if(END_TAG.equals(content)){
					return i-1+1;
				}
			}
		}
		throw new RuntimeException(file.getName()+"---["+sheet.getSheetName()+"]数据没有定义结束行，请在数据结束行之后 第一行第一列写入"+END_TAG);
	}
	/**
	 * 获取行数
	 * @return
	 */
	public int rowNum(){
		return sheet.getLastRowNum()+1; 
	}
	/**
	 * 获取列数
	 * @return
	 */
	public int culumNum(){
		return getDateColumn(); 
	}
	/**
	 * 获取一列的所有列元素
	 * @param column
	 * @return
	 */
	public Cell[] getColum(int column){
//        System.out.println("开始产生一列数据");
//		long start = System.currentTimeMillis();
		int rowNum = rowNum();
        if (rowNum <=1) {
            return null;
        }
        Cell[] result = new Cell[rowNum];
        for (int r = 0; r < rowNum; r++) {
            Row row = sheet.getRow(r);
            if(row==null){
            	result[r] = null;
            }else{
                result[r] = row.getCell(column);
            }
        }
//        long end = System.currentTimeMillis();
//        System.out.println("产生一列数据所花时间"+(end-start));
		return result;
	}
	/**
	 * 获取一行的所有列元素
	 * @param column
	 * @return
	 */
	public Cell[] getCell(int rowIndex){
		Row row =  sheet.getRow(rowIndex);
		if (row == null) {
            return null;
        }
        int cells = getDateColumn();
		Cell[] result = new Cell[cells];
        for (int c = 0; c < cells; c++) {
            result[c] = row.getCell(c);
        }
        return result;
	}
	
	public Row getRow(int rowIndex){
		Row row =  sheet.getRow(rowIndex);
		if (row == null) {
            return null;
        }
		return row;
	}
	public Sheet getSheet() {
		return sheet;
	}
	public File getFile() {
		return file;
	}
	
}
