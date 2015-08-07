package com.sr178.game.tool;


import java.io.UnsupportedEncodingException;
import java.util.ResourceBundle;

public class Config {
   //public static String EXCEL_DERECTORY = "F:/workspace/game-tool/src/main/resources/excelfile";
   // public static String JSON_DERECTORY = "F:/workspace/game-tool/src/main/resources/jsonfile/";
	//public static String MYSQL_URL = "jdbc:mysql://localhost:3306/ddz_game?user=root&password=root&useUnicode=true&characterEncoding=UTF8";

   private static Config config;
   public  String EXCEL_DERECTORY ="D:\\数据目录\\excel\\";
   public  String JSON_DERECTORY = "D:\\数据目录\\jsonsfile\\";
   public  String SQL_File ="D:\\数据目录\\sqlfile\\update.sql";
   
   public  String MYSQL_URL_CONFIG = "jdbc:mysql://localhost:3306/hw_game_config?user=root&password=123456&useUnicode=true&characterEncoding=UTF8";
   public String MYSQL_URL_COMMON = "jdbc:mysql://localhost:3306/hw_game_common?user=root&password=123456&useUnicode=true&characterEncoding=UTF8";
   
   public String HTTP_REFRESH_STATIC_DATA = "http://192.168.1.100:8080/server/reload.do?className=ALL";
   
   public String transferTempDirectory;
   
   public String transferTargetDirectory;
   
   public String needTransferTempDirectory;
   private Config(String excelDerectory,String jsonDerectory,String sqlFile,String mysqlConfig,String mysqlCommon,String httpUrl,String transferTemp,String transferTarget,String needTransferTempDirectory){
	   this.EXCEL_DERECTORY = excelDerectory;
	   this.JSON_DERECTORY = jsonDerectory;
	   this.SQL_File = sqlFile;
	   this.MYSQL_URL_CONFIG = mysqlConfig;
	   this.MYSQL_URL_COMMON = mysqlCommon;
	   this.HTTP_REFRESH_STATIC_DATA = httpUrl;
	   this.transferTempDirectory=transferTemp;
	   this.transferTargetDirectory = transferTarget;
	   this.needTransferTempDirectory = needTransferTempDirectory;
   }

   public static Config init(){
	   if(config==null){
		   Config.class.getClassLoader();
		   ResourceBundle rs = ResourceBundle.getBundle("config");
		   String excel = rs.getString("excel_directory");
		   String json = rs.getString("client_json_directory");
		   String sql = rs.getString("server_sql_directory");
		   String mysql_config = rs.getString("jdbc.url.config");
		   String mysql_common = rs.getString("jdbc.url.common");
		   String http = rs.getString("http.url");
		   String transferTemp = rs.getString("transfer_temp_directory");
		   String transferTarget = rs.getString("transfer_target_directory");
		   String needTransferTempDirectory = rs.getString("need_transfer_temp_directory");
		   try {
				excel =new String(excel.getBytes("iso8859-1"),"utf-8");
				json = new String(json.getBytes("iso8859-1"),"utf-8");
				sql = new String(sql.getBytes("iso8859-1"),"utf-8");
				mysql_config = new String(mysql_config.getBytes("iso8859-1"),"utf-8");
				mysql_common = new String(mysql_common.getBytes("iso8859-1"),"utf-8");
				http =  new String(http.getBytes("iso8859-1"),"utf-8");
				transferTemp =  new String(transferTemp.getBytes("iso8859-1"),"utf-8");
				transferTarget =  new String(transferTarget.getBytes("iso8859-1"),"utf-8");
				needTransferTempDirectory =  new String(needTransferTempDirectory.getBytes("iso8859-1"),"utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		   config = new Config(excel,json,sql,mysql_config,mysql_common,http,transferTemp,transferTarget,needTransferTempDirectory);
		   System.out.println("加载配置文件信息");
		   System.out.println("excel目录:"+excel);
		   System.out.println("json目录:"+json);
		   System.out.println("sql目录:"+json);
		   System.out.println("mysql配置信息:"+mysql_config);
		   System.out.println("mysql.common信息:"+mysql_common);
		   System.out.println("http信息:"+http);
		   System.out.println("已翻译过了的模板位置:"+transferTemp);
		   System.out.println("翻译目标文件位置:"+transferTarget);
		   System.out.println("需要翻译的文件位置:"+needTransferTempDirectory);
	   }
	   return config;
   } 
}
