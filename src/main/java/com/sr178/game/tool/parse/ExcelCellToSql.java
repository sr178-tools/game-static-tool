package com.sr178.game.tool.parse;

import org.apache.poi.ss.usermodel.Cell;

import com.sr178.game.tool.util.ExcelUtil;

/**
 * excel行转sql工具类
 * @author Administrator
 *
 */
public class ExcelCellToSql {
	private int[] passCellIndex;
	private String headStr;
	private int headLength;
	private String tableName;
	/**
	 * 
	 * @param passCellIndex  不插入数据库的列索引
	 * @param headCell       标题行
	 * @param tableName      表名称
	 */
	public ExcelCellToSql(int[] passCellIndex,Cell[] headCell,int dataColumLength,String tableName){
		this.passCellIndex = passCellIndex;
		this.headLength = dataColumLength;
		this.headStr = generatorHead(headCell);
		this.tableName = tableName;
	}
    public String cellToSql(Cell[] valueCell){
    	StringBuffer sql = new StringBuffer("insert into "+tableName+"("+headStr+") VALUES("+generatorCommomCell(valueCell)+");");
    	return sql.toString();
    }
    /**
     * 生成头列字符串
     * @param headCell
     * @return
     */
    public String generatorHead(Cell[] headCell){
    	String result = "";
    	for(int i=0;i<headLength;i++){
    	  if(!isPass(i)){
    		if("".equals(result)){
    			result="`"+ExcelUtil.getContent(headCell[i])+"`";
    		}else{
	    		result = result+","+"`"+ExcelUtil.getContent(headCell[i])+"`";
	    	}
    	  }
    	}
    	return result;
    }
    /**
     * 生成普通数据字符串
     * @param headCell
     * @return
     */
    public String generatorCommomCell(Cell[] valueCell){
    	String result = "";
    	for(int i=0;i<headLength;i++){
    	  if(!isPass(i)){
    	   if(i<valueCell.length){
	    		if("".equals(result)){
	    			result=""+getValue(valueCell[i]);
	    		}else{
		    		result = result+","+getValue(valueCell[i]);
		    	}
    		  }else{
        		  result = result+",NULL";
       	    }
    	   }
    	  }
    	return result;
    }
    /**
     * 是否跳过
     * @param n
     * @return
     */
    private boolean isPass(int n){
    	for(int i=0;i<passCellIndex.length;i++){
    		if(n==passCellIndex[i]){
    			return true;
    		}
    	}
    	return false;
    }
    private String getValue(Cell cell){
        String result = ExcelUtil.getContent(cell);
        if(result==null||result.equals("")){
        	return "NULL";
        }
        try {
			 Double.valueOf(result);
		} catch (Exception e) {
			 result = result.replace("\'", "‘");//sql替换单引号
			 return "'"+result+"'";
		}
       return result;
    }
    
}
