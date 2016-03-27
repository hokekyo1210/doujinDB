package hokekyo1210.dojindb.crawler;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class HtmlUtil {
	
	public static StringBuffer access(String urls) throws Exception {
		StringBuffer sb = new StringBuffer();
		URL url = new URL(urls);
		HttpURLConnection uc = (HttpURLConnection) url.openConnection();
		
		BufferedInputStream bis = new BufferedInputStream(uc.getInputStream());
		BufferedReader br = new BufferedReader(new InputStreamReader(bis, "UTF-8"));
		String line;
		while ((line = br.readLine()) != null) {
			sb.append(line.replaceAll("&amp;", "&") + "\n");
		}
		return sb;
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
