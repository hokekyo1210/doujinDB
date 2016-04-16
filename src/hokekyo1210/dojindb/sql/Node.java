package hokekyo1210.dojindb.sql;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.tree.DefaultMutableTreeNode;

import hokekyo1210.dojindb.main.Main;
import hokekyo1210.dojindb.ui.util.MyDropFileHandler;

public class Node extends DefaultMutableTreeNode implements Comparator<Node>{
	
	public String title;
	public String circle;
	public String artist;
	public String date;
	public List<String> tags;
	public String comment;
	public String image;
	public String thumb;
	public ImageIcon bigImage;
	public ImageIcon miniImage;
	public ImageIcon thumbnail;
	public JLabel thumbnailLabel;
	
	public String table;
	
	public Date exDate = null;
	
	public Node(){}///ダミー
	
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
			loadImage();
	}
	
	public synchronized void loadImage() {
		if(image.equals("None"))return;
		try {
			if(miniImage == null){
				this.miniImage = new ImageIcon(MyDropFileHandler.convert(new File(image), null, Main.TreeRowHeight, Main.TreeRowHeight));
			}
			if(thumbnailLabel == null){
				thumbnailLabel = new JLabel();
				this.thumbnail = new ImageIcon(MyDropFileHandler.convert(new File(image), thumbnailLabel, Main.thumbnailWidth, Main.thumbnailHeight));
				thumbnailLabel.setIcon(thumbnail);
			}
		} catch (Exception e) {
			///e.printStackTrace();
			System.out.println("image load error! "+title+" ("+image+")");
		}
	}
	
	public void loadBigImage(){
		if(image.equals("None"))return;
		if(bigImage != null)return;
		try{
			bigImage = new ImageIcon(ImageIO.read(new File(image)));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public DefaultMutableTreeNode getTreeNode(){
		return this;
	}
	
	private static final Date none = new Date(0L);
	public int compare(Node n1) {
		int ret = this.circle.compareToIgnoreCase(n1.circle);///まずサークルで比較
		if(ret == 0){///サークルが同じ場合は日付で比較
			Date d0 = this.exDate;
			Date d1 = n1.exDate;
			if(d0 == null)d0 = none;
			if(d1 == null)d1 = none;
			ret = d1.compareTo(d0);
			if(ret == 0){///日付も同じ場合はタイトル
				ret = this.title.compareToIgnoreCase(n1.title);
			}
		}
		return ret;
	}
	
	public boolean hasWord(String word){
		boolean ret = false;
		ret =   title.indexOf(word)!=-1||
				circle.indexOf(word)!=-1||
				artist.indexOf(word)!=-1||
				date.indexOf(word)!=-1;
		if(ret)return ret;
		for(String tag : tags){
			ret |= tag.indexOf(word)!=-1;
			if(ret)break;
		}
		return ret;
	}

	@Override
	public int compare(Node n1, Node n2) {
		int ret = 0;
		Date d1 = n1.exDate;
		Date d2 = n2.exDate;
		if(d1 == null)d1 = none;
		if(d2 == null)d2 = none;
		ret = d2.compareTo(d1);
		if(ret == 0){///サークルで比較
			ret = n1.circle.compareToIgnoreCase(n2.circle);
		}
		if(ret == 0){///タイトルで比較
			ret = n1.title.compareToIgnoreCase(n2.title);
		}
		return ret;
	}

}
