package hokekyo1210.dojindb.crawler;

import java.util.ArrayList;
import java.util.List;

public class SearchResult{
	
	public String id;
	public String title,circle;
	public String year,month,day;
	public String artist;
	public List<String> tags;///とりあえずキャラクター
	public String imageURL;
	
	public SearchResult(String id,String title,String circle){
		this.id = id;
		this.title = title;
		this.circle = circle;
	}
	
	public void secondSearch() throws Exception{///確定したらページを読み込む
		String url = "https://www.suruga-ya.jp/product/detail/"+id;
		String ret = HtmlUtil.access(url,"adult=1").toString();
//		System.out.println(ret);
		
		System.out.println("----Load Dojin----");
		System.out.println("title: " + title);
		System.out.println("circle: " + circle);
		
		String date = ret.split("発売日: ")[1].split("<br>")[0];
		if(!date.equalsIgnoreCase("")){
			String regex = "[^0-9/]";
			date = date.replaceAll(regex, "");//日付に必要のない余計な文字を消す
			String[] datesp = date.split("/");
			year = datesp[0];
			month = datesp[1];
			day = datesp[2];
		}
		
		artist = getArtist(ret);
		tags = new ArrayList<String>();
		
//		System.out.println(ret);
		
		String[] tmp = ret.split("zoom-photo-url=\"");
		if(tmp.length != 1){
			imageURL = tmp[1].split("\" style=\"")[0];
		}
		
		System.out.println("artist: " + artist);
		System.out.println("imageURL: " + imageURL);
		System.out.println("date: " + date);
		/*for(String c:tags){
			System.out.println(c);
		}*/
	}

	private List<String> getTags(String ret) {
		List<String> tagret = new ArrayList<String>();
		if(ret.lastIndexOf("\"list\">キャラクター:") == -1)return tagret;
		String[] split = ret.split("\"list\">キャラクター:")[1].split("/'>");
		System.out.println("len"+split.length);
		for(int i = 1;i<split.length;i+=2){
			tagret.add(split[i].split("</A>")[0]);
		}
		return tagret;
	}

	private String getArtist(String ret) {
		String artret = "";
		String[] split = ret.split("画:<a");
		if(split.length == 1)return artret;
		artret = split[1].split("\">")[1].split("</a>")[0];
		return artret;
	}

}
