package hokekyo1210.dojindb.sql;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

public class Node {
	
	public String title;
	public String circle;
	public String artist;
	public String date;
	public List<String> tags;
	public String comment;
	public String image;
	public String thumb;
	
	public Date exDate = null;
	
	private DefaultMutableTreeNode treeNode;
	
	public Node(String title,String circle,String artist,String date,List<String> tags,
				String comment,String image,String thumb){
		this.title = title;
		this.circle = circle;
		this.artist = artist;
		this.date = date;
		this.tags = tags;
		this.comment = comment;
		this.image = image;
		this.thumb = thumb;
		
		treeNode = new DefaultMutableTreeNode(title);
		if(!date.equals("None")){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			try {
				exDate = format.parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public DefaultMutableTreeNode getTreeNode(){
		return treeNode;
	}

}
