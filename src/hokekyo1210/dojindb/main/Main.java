package hokekyo1210.dojindb.main;

import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import hokekyo1210.dojindb.sql.SQLManager;
import hokekyo1210.dojindb.ui.MainFrame;
import hokekyo1210.dojindb.ui.util.IconUtil;

public class Main {
	
	public static final String title = "doujinDB";
	
	public static final int TreeRowHeight = 30;///ツリーのサムネに使う大きさ
	public static final int TreeTxtHeight = 18;
	public static final int thumbnailWidth = 80;///ブラウジングに使うサムネの大きさ
	public static final int thumbnailHeight = 110;

	private MainFrame frame;
	public Main(){
		try {
			///UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try{
			SQLManager.launchSQLManager();
		}catch(Exception e){
			e.printStackTrace();
			System.exit(0);
		}
		new IconUtil("resources");
		frame = new MainFrame(Main.title);
		///setToolTip();
	}
	
	private void setToolTip(){
		ToolTipManager tp = ToolTipManager.sharedInstance();
		tp.setInitialDelay(1000);
	}

	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		new Main();
		long time = System.currentTimeMillis() - start;
		System.out.println("Launch completed!["+time+"ms]");
	}

}
