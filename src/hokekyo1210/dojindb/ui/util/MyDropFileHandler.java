package hokekyo1210.dojindb.ui.util;

import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.TransferHandler;

import hokekyo1210.dojindb.main.Main;
import hokekyo1210.dojindb.ui.MyImageLabel;

public class MyDropFileHandler extends TransferHandler{
	
	private MyImageLabel target;

	public MyDropFileHandler(MyImageLabel imageArea) {
		this.target = imageArea;
	}
	
	private boolean isImage(File file){///拡張子を適当に判定
		String full = file.getAbsolutePath();
		System.out.println("Dropped "+full);
		if(full.lastIndexOf(".png")!=-1||full.lastIndexOf(".jpg")!=-1||full.lastIndexOf(".gif")!=-1)
			return true;
		return false;
	}
	
	public static BufferedImage convert(File file,MyImageLabel target) throws Exception{///画像を220*300に合わせる
		BufferedImage source = ImageIO.read(file);
		int fwidth = source.getWidth();
		int fheight = source.getHeight();
		int tarWidth,tarHeight;///目標とするサイズ
		double resizePer;///縮小(拡大)率
		if(fheight > fwidth){///縦長の画像
			tarHeight = (int)Main.DIAMETER*300;
			resizePer = (double)tarHeight/fheight*1.0;
			tarWidth = (int) (fwidth*resizePer);
			
			target.setHorizontalAlignment(JLabel.CENTER);
			target.setVerticalAlignment(JLabel.TOP);
		}else{
			tarWidth = (int)Main.DIAMETER*220;
			resizePer = (double)tarWidth/fwidth*1.0;
			tarHeight = (int) (fheight*resizePer);
			target.setHorizontalAlignment(JLabel.CENTER);
			target.setVerticalAlignment(JLabel.CENTER);
		}
		if(tarWidth>(int)Main.DIAMETER*222){///はみだしちゃってる
			tarWidth = (int)Main.DIAMETER*220;
			resizePer = (double)tarWidth/fwidth*1.0;
			tarHeight = (int) (fheight*resizePer);
			target.setHorizontalAlignment(JLabel.CENTER);
			target.setVerticalAlignment(JLabel.CENTER);
		}
		BufferedImage ret = new BufferedImage(tarWidth, tarHeight, source.getType());
		ret.getGraphics()
		.drawImage(source.getScaledInstance(tarWidth, tarHeight, Image.SCALE_AREA_AVERAGING), 0, 0, tarWidth, tarHeight, null);
		
		return ret;
	}
	
	@Override
	public boolean importData(TransferSupport support) {
		// 受け取っていいものか確認する
		if (!canImport(support)) {
	        return false;
	    }

		// ドロップ処理
		Transferable t = support.getTransferable();
		try {
			// ファイルを受け取る
			List<File> files = (List<File>) t.getTransferData(DataFlavor.javaFileListFlavor);
			File get = files.get(0);
			if(!isImage(get))return false;
			target.setImageIcon(convert(get,target));
			target.repaint();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	
	@Override
	public boolean canImport(TransferSupport support) {
		if (!support.isDrop()) {
			// ドロップ操作でない場合は受け取らない
	        return false;
	    }

		if (!support.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
			// ドロップされたのがファイルでない場合は受け取らない
	        return false;
	    }

		return true;
	}
	
	

}
