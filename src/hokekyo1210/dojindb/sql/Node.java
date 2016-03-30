package hokekyo1210.dojindb.sql;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.tree.DefaultMutableTreeNode;

import hokekyo1210.dojindb.main.Main;
import hokekyo1210.dojindb.ui.util.MyDropFileHandler;

public class Node extends DefaultMutableTreeNode{
	
	public String title;
	public String circle;
	public String artist;
	public String date;
	public List<String> tags;
	public String comment;
	public String image;
	public String thumb;
	public ImageIcon miniImage;
	
	public String table;
	
	public Date exDate = null;
	
	public Node(String title,String circle,String artist,String date,List<String> tags,
				String comment,String image,String thumb,String table,boolean imageLoad){
		super(title);
		this.title = title;
		this.circle = circle;
		this.artist = artist;
		this.date = date;
		this.tags = tags;
		this.comment = comment;
		this.image = image;
		this.thumb = thumb;
		this.table = table;
		
		///treeNode = new DefaultMutableTreeNode(title);
		if(!date.equals("None")){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			try {
				exDate = format.parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if(imageLoad)
			loadMiniImage();
	}
	
	public void loadMiniImage(){
		if(!image.equals("None")){
			try {
				this.miniImage = new ImageIcon(MyDropFileHandler.convert(new File(image), null, Main.TreeRowHeight, Main.TreeRowHeight));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public DefaultMutableTreeNode getTreeNode(){
		return this;
	}
	
	private static final Date none = new Date(0L);
	public int compare(Node n1) {
		int ret = this.circle.compareTo(n1.circle);///まずサークルで比較
		if(ret == 0){///サークルが同じ場合は日付で比較
			Date d0 = this.exDate;
			Date d1 = n1.exDate;
			if(d0 == null)d0 = none;
			if(d1 == null)d1 = none;
			ret = d1.compareTo(d0);
			if(ret == 0){///日付も同じ場合はタイトル
				ret = this.title.compareTo(n1.title);
			}
		}
		return ret;
	}

}
