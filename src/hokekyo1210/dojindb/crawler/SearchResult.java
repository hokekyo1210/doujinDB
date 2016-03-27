package hokekyo1210.dojindb.crawler;

import java.util.ArrayList;
import java.util.List;

public class SearchResult{
	
	public int id;
	public String title,circle;
	public String year,month,day;
	public String artist;
	public List<String> tags;///とりあえずキャラクター
	public String imageURL;
	
	public SearchResult(int id,String title,String circle){
		this.id = id;
		this.title = title;
		this.circle = circle;
	}
	
	public void secondSearch() throws Exception{///確定したらページを読み込む
		String url = "http://www.doujinshi.org/book/"+(id)+"/?agree=force";
		String ret = HtmlUtil.access(url).toString();
		
		String date = ret.split("発行日:</B></td><td>")[1].split("</td>")[0];
		String[] datesp = date.split("-");
		year = datesp[0];
		month = datesp[1];
		day = datesp[2];
		
		artist = getArtist(ret);///アーティストとタグは存在しない可能性があるので丁寧に
		tags = getTags(ret);
		
		imageURL = "http://img.doujinshi.org/big/"+ret.split("img.doujinshi.org/big/")[1].split("\" alt")[0];
		System.out.println(imageURL);
		System.out.println(artist);
		System.out.println(date);
		for(String c:tags){
			System.out.println(c);
		}
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
		String[] split = ret.split("\">");
		int i;
		for(i = 0;i < split.length;i++){
			String tar = split[i].split("<")[0];
			if(tar.equals("著者:"))break;
		}
		i++;
		if(i == split.length)return "";
		String artret = "";
		while(true){
			String tar = split[i].split("<")[0];
			if(!tar.equals(" "))break;
			if(artret.length() != 0)artret += ",";
			artret += split[i+1].split("<")[0];
			i+=3;
		}
		return artret;
	}

}
