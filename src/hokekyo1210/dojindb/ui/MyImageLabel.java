package hokekyo1210.dojindb.ui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import hokekyo1210.dojindb.crawler.HtmlUtil;
import hokekyo1210.dojindb.ui.util.MyDropFileHandler;

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
	public void setImageIcon(File file) throws Exception{
		setImageIcon(ImageIO.read(file));
	}
	public void setImageIcon(String url){
		if(!(new File(DIR).exists())){
			new File(DIR).mkdirs();
		}
		File to = new File(DIR+"/temp.png");
		if(to.exists())to.delete();
		try {
			HtmlUtil.download(to, url);
			this.source = MyDropFileHandler.convert(to, this);///大きさを変換する
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		ImageIcon icon = new ImageIcon(source);
		this.setIcon(icon);
	}

}
