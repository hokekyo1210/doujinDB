package hokekyo1210.dojindb.ui;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

import hokekyo1210.dojindb.sql.Node;

public class RightPanel extends JPanel{
	
	private static final int width = 430,height = 588;
	
	private JPanel currentView = null;
	
	public RightPanel(int x,int y){
		this.setLayout(null);
		this.setBounds(x, y, RightPanel.width,RightPanel.height);
		initPanel();
		setSubmitPanel();///最初はサブミットパネル出す
	}
	
	public void setSubmitPanel(){
		if(currentView!=null)
			this.remove(currentView);///とりあえず今表示してるパネル消す
		
		SubmitPanel submitPanel = new SubmitPanel(RightPanel.width,RightPanel.height,this);
		currentView = submitPanel;
		this.add(submitPanel);
		this.repaint();
	}
	
	public void setModificationPanel(Node node){///修正用
		if(currentView!=null)
			this.remove(currentView);///とりあえず今表示してるパネル消す
		
		SubmitPanel submitPanel = new SubmitPanel(RightPanel.width,RightPanel.height,this,node);
		currentView = submitPanel;
		this.add(submitPanel);
		this.repaint();
	}
	
	private void initPanel(){
		this.setBorder(new BevelBorder(BevelBorder.LOWERED));
	}

}
