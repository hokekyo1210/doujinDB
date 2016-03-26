package hokekyo1210.dojindb.ui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class MyImageLabel extends JLabel{
	
	private static final String DIR = System.getProperty("user.home")+"/temp/pic";///画像を保存するディレクトリ
	private static Random r = new Random();
	
	private BufferedImage source = null;
	
	public BufferedImage getSource(){
		return source;
	}
	
	public String save(){///画像を保存する
		if(!(new File(DIR).exists())){
			new File(DIR).mkdirs();
		}
		String ret = DIR+"/"+(r.nextLong())+".png";
		try {
			ImageIO.write(source, "png", new File(ret));
		} catch (IOException e) {
			e.printStackTrace();
			return "None";
		}
		return ret;
	}
	
	public void setImageIcon(BufferedImage source){
		this.source = source;
		ImageIcon icon = new ImageIcon(source);
		this.setIcon(icon);
	}

}
