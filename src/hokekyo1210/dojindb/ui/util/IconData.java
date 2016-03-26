package hokekyo1210.dojindb.ui.util;


import javax.swing.ImageIcon;

public class IconData {
	
	private String name;
	private ImageIcon icon;
	
	public IconData(String name,ImageIcon icon){
		this.name = name;
		this.icon = icon;
	}
	
	public String getName(){
		return name;
	}
	public ImageIcon getIcon(){
		return icon;
	}

}
