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
	
	private boolean isImage(File file){///�g���q��K���ɔ���
		String full = file.getAbsolutePath();
		System.out.println("Dropped "+full);
		if(full.lastIndexOf(".png")!=-1||full.lastIndexOf(".jpg")!=-1||full.lastIndexOf(".gif")!=-1)
			return true;
		return false;
	}
	
	public static BufferedImage convert(File file,JLabel target) throws Exception{
		return convert(file,target,220,300);
	}
	
	public static BufferedImage convert(File file,JLabel target,int width,int height) throws Exception{///�摜��width*height�ɍ��킹��
		BufferedImage source = ImageIO.read(file);
		int fwidth = source.getWidth();
		int fheight = source.getHeight();
		int tarWidth,tarHeight;///�ڕW�Ƃ���T�C�Y
		double resizePer;///�k��(�g��)��
		if(fheight > fwidth){///�c���̉摜
			tarHeight = height;
			resizePer = (double)tarHeight/fheight*1.0;
			tarWidth = (int) (fwidth*resizePer);
		}else{
			tarWidth = width;
			resizePer = (double)tarWidth/fwidth*1.0;
			tarHeight = (int) (fheight*resizePer);
		}
		if(tarWidth>width+2){///�݂͂���������Ă�
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
		// �󂯎���Ă������̂��m�F����
		if (!canImport(support)) {
	        return false;
	    }

		// �h���b�v����
		Transferable t = support.getTransferable();
		try {
			// �t�@�C�����󂯎��
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
			// �h���b�v����łȂ��ꍇ�͎󂯎��Ȃ�
	        return false;
	    }

		if (!support.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
			// �h���b�v���ꂽ�̂��t�@�C���łȂ��ꍇ�͎󂯎��Ȃ�
	        return false;
	    }

		return true;
	}
	
	

}
