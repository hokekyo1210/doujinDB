package hokekyo1210.dojindb.crawler;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import hokekyo1210.dojindb.ui.SubmitPanel;

public class Crawler implements Runnable{
	
	private SubmitPanel source;
	
	private Thread myThread;
	private boolean isFinished = false;
	private String searchWord;
	private List<String> result = new ArrayList<String>();
	private List<SearchResult> results = new ArrayList<SearchResult>();
	
	public Crawler(String searchWord,SubmitPanel source){
		this.searchWord = searchWord;
		this.source = source;
	}
	
	public void start(){
		myThread = new Thread(this);
		myThread.start();
	}
	
	public boolean isFinished(){
		return isFinished;
	}
	
	private void fin(){
		results.clear();
		if(result.size() == 0)return;
//		System.out.println(result);
		for(String res : result){
			String[] split = res.split("\"onsale\">");
			int hit = split.length - 1;
			System.out.println("hit "+hit);
			///System.out.println(res);
			for(int i = 1 ; i < split.length ; i++){
				String tar = split[i];
				String id = tar.split("/product/detail/")[1].split("\">")[0];
				String[] spl2 = tar.split("\">");
				
				String title = spl2[1].split("</a>")[0];
				if(title.lastIndexOf("> ") != -1){
					//「<<東方>> ~~~~~」みたいな感じでジャンルが含まれちゃってる場合は除く
					title = title.split("> ")[1];
				}else{
					//ジャンルが無い場合は謎の空白が1文字目にあるので消す
					title = title.substring(1);
				}
				if(title.lastIndexOf(" / ") != -1){
					title = title.split(" / ")[0];
				}
				
				String circle = spl2[2].split("</span>")[0];
				if(circle.indexOf("  [") != -1) {
					//サークルの前に空白がいっぱい入るようになったので除く
					circle = circle.split("  \\[")[1];
				}
				if(circle.indexOf("]&#12288;") != -1) {
					circle = circle.split("\\]&#12288;")[0];
				}
				
				if(circle.equalsIgnoreCase(""))continue;///サークル部分が空なら省く
				
		//			System.out.println(title+" "+circle+" "+id);
				results.add(new SearchResult(id,title,circle));
			}
		}
		source.showPopup(results);
	}

	@Override
	public void run() {
		System.out.println("Crawler is running...");
		long start = System.currentTimeMillis();
		String url,url2;
		try {
			url = URLEncoder.encode(searchWord, "UTF-8");///urlエンコード
			url2 = URLEncoder.encode(searchWord, "UTF-8");///urlエンコード
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
			return;
		}
		url = "https://www.suruga-ya.jp/search?category=11&search_word="+url+"&grid=f&inStock=On&adult_s=1";
		url2 = "https://www.suruga-ya.jp/search?category=11&search_word="+url2+"&grid=f&inStock=On&adult_s=1&page=2";
//		System.out.println("-----"+url+"-----");
//		System.out.println("-----"+url2+"-----");
		try {
			StringBuffer response = HtmlUtil.access(url,"");///htmlを取得する
			result.add(response.toString());
			response = HtmlUtil.access(url2,"");///htmlを取得する
			result.add(response.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		long time = System.currentTimeMillis() - start;
		
		System.out.println("Crawler finished!["+time+"ms]");
		fin();
		isFinished = true;
	}
	


}
