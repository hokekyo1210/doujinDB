package hokekyo1210.dojindb.main;

import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import hokekyo1210.dojindb.sql.SQLManager;
import hokekyo1210.dojindb.ui.MainFrame;
import hokekyo1210.dojindb.ui.util.IconUtil;

public class Main {
	
	public static final String title = "doujinDB";

	private MainFrame frame;
	public Main(){
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
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
		new Main();
	}

}
