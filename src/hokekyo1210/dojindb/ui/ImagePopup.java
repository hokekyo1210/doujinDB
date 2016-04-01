package hokekyo1210.dojindb.ui;

import java.awt.Color;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.border.LineBorder;

import hokekyo1210.dojindb.sql.Node;

public class ImagePopup extends JPopupMenu{
	
	private static HashMap<Node,ImagePopup> memo = new HashMap<Node,ImagePopup>();
	
	private ImagePopup(Node node){
		this.setBorder(new LineBorder(Color.BLACK,1));
		if(node.bigImage == null)node.loadBigImage();
		JLabel label = new JLabel(node.bigImage);
		this.add(label);
	}
	
	public static ImagePopup getImagePopup(Node node){
		ImagePopup ret = memo.get(node);
		if(ret == null){
			ret = new ImagePopup(node);
			memo.put(node, ret);
		}
		return ret;
	}

}
