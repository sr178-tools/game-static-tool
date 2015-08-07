package com.sr178.game.tool.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import com.sr178.game.tool.Config;


public class ConnectionManager {
   private static ConnectionManager cm = new ConnectionManager();
   private Map<Integer,Connection> connMap;
   
   public static final int CONFIG = 0;
   public static final int COMMON = 1;
   private ConnectionManager(){
	   try {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		System.out.println("Success loading Mysql Driver!\n"); // jdbc成功
	} catch (InstantiationException e) {
		e.printStackTrace();
	} catch (IllegalAccessException e) {
		e.printStackTrace();
	} catch (ClassNotFoundException e) {
		e.printStackTrace();
	}
   }
   public static ConnectionManager getInstance(){
	   return cm;
   }
   
   public Connection getConnection(int type){
	   if(connMap==null){
		   connMap = new HashMap<Integer,Connection>();
		  try {
			  connMap.put(CONFIG,  DriverManager.getConnection(Config.init().MYSQL_URL_CONFIG));
			  connMap.put(COMMON,  DriverManager.getConnection(Config.init().MYSQL_URL_COMMON));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	   }
       return connMap.get(type);
   }
   
   public Statement getStatement(int type){
	  try {
		return getConnection(type).createStatement();
	} catch (SQLException e) {
		e.printStackTrace();
	}
	return null;
   }
   
   public ResultSet findSql(int type,String sql) throws SQLException{
		return getStatement(type).executeQuery(sql);
   }
   
   public int executeUpdate(int type,String sql) throws SQLException{
		return getStatement(type).executeUpdate(sql);
   }
}
