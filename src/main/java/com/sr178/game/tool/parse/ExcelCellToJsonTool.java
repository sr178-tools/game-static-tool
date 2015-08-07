package com.sr178.game.tool.parse;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;

import com.sr178.game.tool.Config;
import com.sr178.game.tool.util.ExcelUtil;
import com.sr178.game.tool.util.FileCreatUtil;


public class ExcelCellToJsonTool {
  public static final String COLUM_TEM = "@@COLUM_TEM@@";
  public static final String VALUE_TEMP = "@@VALUE_TEMP@@";
  private static StringBuffer cellTemp = new StringBuffer();
  private String fileName;
  private int departMentNum;
  private Map<Integer,StringBuffer> map = new HashMap<Integer,StringBuffer>();
//  private StringBuffer fileStr = new StringBuffer();
  public ExcelCellToJsonTool(String fileName,int departMentNum){
	  this.fileName = fileName;
	  this.departMentNum = departMentNum;
  }
  static{
	  cellTemp.append('"');
	  cellTemp.append(COLUM_TEM);
	  cellTemp.append('"');
	  cellTemp.append(":[");
	  cellTemp.append(VALUE_TEMP);
	  cellTemp.append("]");
  }
  public void start(){
	  for(int i=0;i<departMentNum;i++){
		  map.put(i, new StringBuffer().append("{"));
	  }
//	  fileStr.append("{");
  }
  public void insertOneEntry(Map<Integer,String> entryMap){
//	  map.get(currentDepartMentNum).append(string);
//	  fileStr.append(string);
	  for(Integer i:entryMap.keySet()){
		  map.get(i).append(entryMap.get(i));
	  }
  }
  public void end(){
	  for(int i=0;i<departMentNum;i++){
		  map.get(i).append("}");
	  }
//	  fileStr.append("}");
  }
  
  public void writeFile() throws UnsupportedEncodingException, IOException{
	  if(departMentNum<=1){
		  FileCreatUtil.outFile(Config.init().JSON_DERECTORY, fileName2Bean(fileName+".json"), map.get(0).toString());
	  }else{
		  for(int i=0;i<departMentNum;i++){
			  String departMentTableNum = "_"+i;
			  FileCreatUtil.outFile(Config.init().JSON_DERECTORY, fileName2Bean(fileName+departMentTableNum+".json"), map.get(i).toString());
		  }
	  }
  }
  public Map<Integer,String> excelColumsToJson(Cell[] colums,Cell[] keyColums,int startRow,int endCellRow){
	  Map<Integer,String> result = new HashMap<Integer,String>();
	  Map<Integer,String> mapCell = generatorCell(colums,keyColums,startRow,endCellRow);
	  for(int i=0;i<departMentNum;i++){
		  String str = clumn2Field(ExcelUtil.getContent(colums[startRow-1]));
		  String resultstr = cellTemp.toString().replace(COLUM_TEM, str==null?"":str);
		  result.put(i, resultstr.replace(VALUE_TEMP,mapCell.get(i)));
	  }
	  return result;
  }
  
  private  String clumn2Field(String clumn){
	   String[] array = clumn.split("_");
	   if(array.length==1){
		   return clumn;
	   }
	   String result = "";
	   for(int i=0;i<array.length;i++){
		   if(i==0){
			   result = array[i];
		   }else{
			   result = result+""+array[i].substring(0, 1).toUpperCase()+array[i].substring(1);
		   }
	   }
	   return result;
  }
  
  private String fileName2Bean(String fileName){
	  String[] array = fileName.split("_");
	  if(array.length==1){
		   return fileName.substring(0,1).toUpperCase()+fileName.substring(1);
	  }
	  String result = "";
	   for(int i=0;i<array.length;i++){
		   if(i==0){
			   result = array[i].substring(0, 1).toUpperCase()+array[i].substring(1);
		   }else{
			   result = result+""+array[i].substring(0, 1).toUpperCase()+array[i].substring(1);
		   }
	   }
	   return result;
  }
  //@TODO 该方法耗时比较多
  private Map<Integer,String> generatorCell(Cell[] colums,Cell[] keys,int startRow,int endRow){
	  Map<Integer,String> resultMap = new HashMap<Integer,String>();
	  for(int i=0;i<departMentNum;i++){
		  resultMap.put(i, "");
	  }
	  for(int i=startRow;i<endRow;i++){
		  String value = "";
		  if(i<colums.length){
			  value = getValue(colums[i]);
		  }
		  int mapKey=0;
		  if(keys!=null){
			  Integer key = Integer.valueOf(getValue(keys[i]));
			  mapKey = key%departMentNum;
		  }
		  String result = resultMap.get(mapKey);
		  if(result.equals("")){
			  result = value+"";
		  }else{
			  result = result+","+value;
		  }
		  resultMap.put(mapKey, result);
	  }
	  return resultMap;
  }
  private String getValue(Cell cell){
      String result = ExcelUtil.getContent(cell);
      if(result==null){
    	  return null;
      }
      try {
			 Double.valueOf(result);
		} catch (Exception e) {
			 result = trandferStr(result);//替换冒号  双引号  单引号
			 return '"'+result+'"';
		}
     return result;
  }
  
  public String trandferStr(String result){
	     result = result.replace("'", "‘");
		 result = result.replace("\"", "”");
		 return result;
  }
//  public static void main(String[] args) {
//	String culum1 = "cchero";
//	String culum2 = "cc_hero";
//	String culum3 = "cc_hero_id";
//	System.out.println(fileName2Bean(culum1));
//	System.out.println(fileName2Bean(culum2));
//	System.out.println(fileName2Bean(culum3));
//}
//  public static void main(String[] args) {
//	String s = "我的家乡:在你的\"家里么\",,'''";
//	System.out.println(s);
//	System.out.println(trandferStr(s));
//  }
}
