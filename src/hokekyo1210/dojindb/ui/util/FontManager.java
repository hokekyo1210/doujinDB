package hokekyo1210.dojindb.ui.util;

import java.awt.Font;

import hokekyo1210.dojindb.main.Main;

public class FontManager {
	
	public static Font getDefaultFontU(int size){
		return new Font("���C���I", Font.ITALIC, (int)Main.DIAMETER*size);
	}
	public static Font getDefaultFont(int size){
		return new Font("���C���I", Font.PLAIN, (int)Main.DIAMETER*size);
	}

}
