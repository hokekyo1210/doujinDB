package hokekyo1210.dojindb.crawler;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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
	
}
