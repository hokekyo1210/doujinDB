package hokekyo1210.dojindb.ui.util;

import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.TransferHandler;

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
	
	public static BufferedImage convert(File file,JLabel target) throws Exception{
		return convert(file,target,220,300);
	}
	
	public static BufferedImage convert(File file,JLabel target,int width,int height) throws Exception{///画像をwidth*heightに合わせる
		BufferedImage source = ImageIO.read(file);
		int fwidth = source.getWidth();
		int fheight = source.getHeight();
		int tarWidth,tarHeight;///目標とするサイズ
		double resizePer;///縮小(拡大)率
		if(fheight > fwidth){///縦長の画像
			tarHeight = height;
			resizePer = (double)tarHeight/fheight*1.0;
			tarWidth = (int) (fwidth*resizePer);
		}else{
			tarWidth = width;
			resizePer = (double)tarWidth/fwidth*1.0;
			tarHeight = (int) (fheight*resizePer);
		}
		if(tarWidth>width+2){///はみだしちゃってる
			tarWidth = width;
			resizePer = (double)tarWidth/fwidth*1.0;
			tarHeight = (int) (fheight*resizePer);
		}
		BufferedImage ret = new BufferedImage(tarWidth, tarHeight, source.getType());
		ret.getGraphics()
		.drawImage(source.getScaledInstance(tarWidth, tarHeight, Image.SCALE_AREA_AVERAGING), 0, 0, tarWidth, tarHeight, null);
		if(target != null){
			target.setHorizontalAlignment(JLabel.CENTER);
			target.setVerticalAlignment(JLabel.CENTER);
		}
		return ret;
	}
	
	@SuppressWarnings("unchecked")
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
