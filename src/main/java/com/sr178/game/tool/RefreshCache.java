package com.sr178.game.tool;

import com.sr178.game.tool.http.UrlRequestUtils;


public class RefreshCache {
public static void main(String[] args) {
	String jsonStr = UrlRequestUtils.execute(Config.init().HTTP_REFRESH_STATIC_DATA, null, UrlRequestUtils.Mode.GET);
	System.out.println(jsonStr);
	if(jsonStr.indexOf("1000")!=-1){
    	System.out.println("刷新成功~~~");
    }else{
    	System.out.println("刷新失败~~~");
    }
	
}
}
