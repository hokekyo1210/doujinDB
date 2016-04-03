package hokekyo1210.dojindb.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import hokekyo1210.dojindb.crawler.HtmlUtil;
import hokekyo1210.dojindb.sql.Circle;
import hokekyo1210.dojindb.sql.Root;
import hokekyo1210.dojindb.sql.SQLManager;
import hokekyo1210.dojindb.ui.DBPanel;

public class ReitaisaiLoader implements Runnable{///例大祭13用

	private static final String url = "http://s.reitaisai.com/rts13/name-circle/";
	private static final double distance = 0.99;
	
	public static HashMap<Circle,String> circleSpace = new HashMap<Circle,String>();
	
	private List<String> spaces;
	private List<String> circles;
	private List<String> artists;
	
	public ReitaisaiLoader(){
		
	}
	
	public void runThread(){
		Thread th = new Thread(this);
		th.start();
	}
	
	@Override
	public void run(){
		spaces = new ArrayList<String>();
		circles = new ArrayList<String>();
		artists = new ArrayList<String>();
		String ret;
		try {
			ret = HtmlUtil.access(url, "").toString();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		String[] split = ret.split("<td>");
		int at = 1;
		for(;at < split.length;at+=3){
			String space = split[at].split("</td>")[0];
			String circle = split[at+1].split("</td>")[0];
			String artist = split[at+2].split("</td>")[0];
			spaces.add(space);
			circles.add(circle);
			artists.add(artist);
		}
		
		List<Root> tables = SQLManager.getTables();///全サークルをいったん読み込む
		for(Root r:tables){
			int a = r.getChildCount();
			for(int i = 0;i < a;i++){
				Circle tar = (Circle)r.getChildAt(i);
				String circleName = tar.getCircleName();
				String artistName = tar.getLastArtist();
				double maxi = 0.0;
				String tmp = "";
				String tmp2 = "";
				for(int j = 0;j < circles.size();j++){
					String space = spaces.get(j);
					String l = circles.get(j);
					String artist = artists.get(j);
					double score1,score2;
					try {
						score1 = LevenshteinDistance.edit(circleName, l);
						score2 = LevenshteinDistance.edit(artistName, artist);
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
						continue;
					}
					
					if(score2 == 1.0){
						maxi = 1.0;
						tmp = l;
						tmp2 = space;
					}
					if(maxi < score1){
						maxi = score1;
						tmp = l;
						tmp2 = space;
					}
				}
//				System.out.println(circleName + " " + tmp + " " + maxi);
				if(distance <= maxi)
					tar.setCircleSpace(tmp2);
			}
		}
		DBPanel.treeRefresh();///変更を伝える
	}

}
