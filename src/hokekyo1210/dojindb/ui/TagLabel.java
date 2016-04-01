package hokekyo1210.dojindb.ui;

import java.awt.Color;
import java.awt.Cursor;
import java.nio.charset.Charset;

import javax.swing.JLabel;
import javax.swing.border.LineBorder;

import com.sun.xml.internal.ws.util.StringUtils;

import hokekyo1210.dojindb.main.Main;
import hokekyo1210.dojindb.ui.util.FontManager;

public class TagLabel extends JLabel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int height = 24
			;
	private static final Color backGround = Color.WHITE;
	private static final Color otherColor = new Color(246,255,247);
	
	private String tag;
	private int pwidth;
	
	public TagLabel(String tag){
		this.tag = tag;
		
		this.setBorder(new LineBorder(Color.black, 1, true));
		this.setBackground(otherColor);
		this.setOpaque(true);
		this.setHorizontalAlignment(JLabel.CENTER);
		this.setVerticalAlignment(JLabel.CENTER);
		this.setText(tag);
		this.setCursor(new Cursor(Cursor.HAND_CURSOR));
		this.setFont(FontManager.getDefaultFont(11));
		this.pwidth = this.getPreferredSize().width + 6;
	}
	
	public int getWidth(){
		return pwidth;
	}
	
	public String getTag(){
		return tag;
	}
	
	public void reloadBounds(int x,int y){
		this.setBounds(x, y, (int)Main.DIAMETER*pwidth, (int)Main.DIAMETER*height);
	}

}
