package hokekyo1210.dojindb.ui.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.io.File;
import java.nio.charset.Charset;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;

import hokekyo1210.dojindb.main.Main;
import hokekyo1210.dojindb.sql.Node;
import hokekyo1210.dojindb.sql.Root;

public class IconTreeCellRenderer implements TreeCellRenderer{
	
	private static final int width = 8;
	
	private DefaultTreeCellRenderer superRenderer;
//	private JLabel dammy = new JLabel("");
	
	private Color backGroundColor = null;
	private ImageIcon openIcon = null;
	private ImageIcon closedIcon = null;
	private ImageIcon defaultIcon = null;
	private Font font = null;
	private Font bigFont = null;
	
	public IconTreeCellRenderer(){
		superRenderer = new DefaultTreeCellRenderer();
		superRenderer.setBackgroundNonSelectionColor(backGroundColor);
		superRenderer.setOpenIcon(null);
		superRenderer.setClosedIcon(null);
		superRenderer.setLeafIcon(IconUtil.getIcon("file.png"));
	}

	@Override
	public Component getTreeCellRendererComponent(
			JTree tree , Object value , boolean selected ,
			boolean expanded , boolean leaf , int row , boolean hasFocus
	) {
		JLabel label = (JLabel)superRenderer.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
		JLabel dammy = new JLabel("");///幅を測るためのダミーコンポーネント
		dammy.setText((String) ((DefaultMutableTreeNode)value).getUserObject());
		if(value instanceof Root){
			label.setFont(bigFont);
			dammy.setFont(bigFont);
		}else{
			label.setFont(font);
			dammy.setFont(font);
		}

		if(leaf && value instanceof Node){
			Node node = (Node)value;
			if(node.miniImage != null){
				label.setIcon(node.miniImage);
				dammy.setIcon(node.miniImage);
			}
		}
		int twidth = dammy.getPreferredSize().width;
		if(value instanceof Node){
			label.setPreferredSize(new Dimension(twidth,Main.TreeRowHeight));
		}else{
			label.setPreferredSize(new Dimension(twidth,Main.TreeTxtHeight));
		}
		return label;
	}
	
	public void setFont(Font font){
		this.font = font;
	}
	
	public void setBigFont(Font font){
		this.bigFont = font;
	}

}
