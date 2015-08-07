package com.sr178.game.tool.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;

import com.sr178.game.tool.parse.ExcelParse;


public class ConfigParseExcel {
//   private final static String ALL = "ALL";
   private final static String SERVER="SERVER";
   private final static String CLIENT="CLIENT";
   //字段是否生成配置行 在第二行
   private final static int CONFIG_CELL_INDEX =1;
   private ExcelParse excelParse;
   private int[] igoList;
   
   public ConfigParseExcel(ExcelParse excelParse,GeneratorType type){
	   this.excelParse = excelParse;
	   if(type==GeneratorType.SERVER){
		   igoList =  getServerIgornIndex();
	   }else if(type==GeneratorType.CLIENT){
		   igoList = getClientIgornIndex();
	   }else{
		   igoList = new int[0];
	   }
   }
   /**
    * 获取生成客户端文件需要忽略的字段index
    * @param excelParse
    * @return
    */
   private  int[] getClientIgornIndex(){
	   Cell[] configCell = excelParse.getCell(CONFIG_CELL_INDEX);
	   if(configCell==null){
		   throw new RuntimeException(excelParse.getFile().getName()+"--"+excelParse.getSheet().getSheetName()+"请在第二行加入配置项，如  ALL ALL SERVER CLIENT");
	   }
	   List<Integer> igorList = new ArrayList<Integer>();
	   for(int i=0;i<configCell.length;i++){
		   if(configCell[i].getStringCellValue().equals(SERVER)){
			   igorList.add(i);
		   }
	   }
	   int[] reslut = new int[igorList.size()];
	   for(int m=0;m<igorList.size();m++){
		   reslut[m] = igorList.get(m);
	   }
	   return reslut;
   }
   
   /**
    * 获取生成服务端文件需要忽略的字段index
    * @param excelParse
    * @return
    */
   private  int[] getServerIgornIndex(){
	   Cell[] configCell = excelParse.getCell(CONFIG_CELL_INDEX);
	   if(configCell==null){
		   throw new RuntimeException(excelParse.getFile().getName()+"--"+excelParse.getSheet().getSheetName()+"请在第二行加入配置项，如  ALL ALL SERVER CLIENT");
	   }
	   List<Integer> igorList = new ArrayList<Integer>();
	   for(int i=0;i<configCell.length;i++){
		   if(configCell[i].getStringCellValue().equals(CLIENT)){
			   igorList.add(i);
		   }
	   }
	   int[] reslut = new int[igorList.size()];
	   for(int m=0;m<igorList.size();m++){
		   reslut[m] = igorList.get(m);
	   }
	   return reslut;
   }
   /**
    * 是否pass掉该字段
    * @param index
    * @return
    */
   public boolean isPass(int index){
	   if(igoList!=null&&igoList.length>0){
		   for(int i=0;i<igoList.length;i++){
			   if(igoList[i]==index){
				   return true;
			   }
		   }
	   }
	   return false;
   }
   
   public int[] getIgoList() {
	return igoList;
   }
/**
    * 获取需要过滤的字段数量
    * @return
    */
   public int getPassSize(){
	    return igoList.length;
   }
}
