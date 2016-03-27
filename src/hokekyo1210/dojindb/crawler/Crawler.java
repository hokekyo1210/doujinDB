package hokekyo1210.dojindb.crawler;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import hokekyo1210.dojindb.ui.SubmitPanel;

public class Crawler implements Runnable{
	
	private SubmitPanel source;
	
	private Thread myThread;
	private boolean isDead = false;
	private String searchWord;
	private String result;
	private List<SearchResult> results = new ArrayList<SearchResult>();
	
	public Crawler(String searchWord,SubmitPanel source){
		this.searchWord = searchWord;
		this.source = source;
	}
	
	public void start(){
		myThread = new Thread(this);
		myThread.start();
	}
	public void stop(){
		isDead = true;
	}
	public boolean isDead(){
		return isDead();
	}
	
	private void fin(){
		results.clear();
		if(result.equalsIgnoreCase(""))return;
//		System.out.println(result);
		String[] split = result.split("\"onsale\">");
		int hit = split.length - 1;
		System.out.println("hit "+hit);
		for(int i = 1 ; i < split.length ; i++){
			String tar = split[i];
			String id = tar.split("/product/detail/")[1].split("\">")[0];
			String[] spl2 = split[i].split("\">");
			String title = spl2[1].split("</a>")[0];
			String circle = spl2[2].split("</span>")[0];
			if(circle.equalsIgnoreCase(""))continue;///サークル部分が空なら省く
			
			circle = circle.substring(1);
			circle = circle.split("]")[0];
			if(title.lastIndexOf("&gt;") != -1){///ジャンルが含まれちゃってる
				title = title.split("&gt; ")[1];
			}else{
				title = title.substring(1);
			}
			if(title.lastIndexOf(" / ") != -1){
				title = title.split(" / ")[0];
			}
//			System.out.println(title+" "+circle+" "+id);
			results.add(new SearchResult(id,title,circle));
		}
		if(results.size() != 0)
			source.showPopup(results);
	}

	@Override
	public void run() {
		System.out.println("Crawler is running...");
		long start = System.currentTimeMillis();
		String[] split = searchWord.split(" ");
		String url;
		try {
			url = URLEncoder.encode(searchWord, "UTF-8");///urlエンコード
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
			return;
		}
		url = "http://www.suruga-ya.jp/search?category=11&search_word="+url+"&grid=f&inStock=On&adult_s=1";
		System.out.println("-----"+url+"-----");
		result = "";
		try {
			StringBuffer response = HtmlUtil.access(url);///htmlを取得する
			result = response.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		long time = System.currentTimeMillis() - start;
		
		System.out.println("Crawler finished!["+time+"ms]");
		fin();
		stop();
	}
	


}
