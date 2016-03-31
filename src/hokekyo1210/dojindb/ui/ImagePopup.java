package hokekyo1210.dojindb.ui;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.border.LineBorder;

import hokekyo1210.dojindb.sql.Node;

public class ImagePopup extends JPopupMenu{
	
	public ImagePopup(Node node){
		this.setBorder(new LineBorder(Color.BLACK,1));
		if(node.bigImage == null)node.loadBigImage();
		JLabel label = new JLabel(node.bigImage);
		this.add(label);
	}

}
