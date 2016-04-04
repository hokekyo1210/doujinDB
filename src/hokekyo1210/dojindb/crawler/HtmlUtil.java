package hokekyo1210.dojindb.crawler;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class HtmlUtil {
	
	public static StringBuffer access(String urls,String cookie) throws Exception {
		StringBuffer sb = new StringBuffer();
		URL url = new URL(urls);
		
		HttpURLConnection uc = (HttpURLConnection) url.openConnection();
		uc.setRequestProperty("Cookie", cookie);
		
		BufferedInputStream bis = new BufferedInputStream(uc.getInputStream());
		BufferedReader br = new BufferedReader(new InputStreamReader(bis, "UTF-8"));
		String line;
		while ((line = br.readLine()) != null) {
			sb.append(line.replaceAll("&amp;", "&") + "\n");
		}
		return sb;
	}
	
	static String retrieveCookie(URL url) throws Exception 
	{ 
	     String cookieValue = null;

	     CookieHandler handler = CookieHandler.getDefault();
	     if (handler != null)    {
	          Map<String, List<String>> headers = handler.get(url.toURI(), new HashMap<String, List<String>>() );
	          List<String> values = headers.get("Cookie");
	          for (Iterator<String> iter=values.iterator(); iter.hasNext();) {
	               String v = iter.next(); 

	               if (cookieValue == null) 
	                    cookieValue = v; 
	               else
	                    cookieValue = cookieValue + ";" + v; 
	          } 
	     } 
	     return cookieValue; 
	}
	
	public static String download(File to,String urlStr) throws Exception{
		System.out.println(to.getAbsolutePath()+" "+urlStr);
		URL url = new URL(urlStr); // ダウンロードする URL
		URLConnection conn = url.openConnection();
		InputStream in = conn.getInputStream();
		FileOutputStream out = new FileOutputStream(to, false);
		byte[] bytes = new byte[1024];
		int len;
		while((len=in.read(bytes))!=-1){
            out.write(bytes,0,len);
        }
		out.close();
		in.close();
		return to.getAbsolutePath();
	}
	
}
