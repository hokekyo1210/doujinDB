package hokekyo1210.dojindb.ui;

import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import hokekyo1210.dojindb.sql.Node;

public class RightPanel extends JPanel{
	
	private static final int width = 430,height = 590;
	
	private JComponent currentView = null;
	private MainFrame source;
	
	public RightPanel(int x,int y,MainFrame source){
		this.source = source;
		this.setLayout(null);
		this.setBounds(x, y, RightPanel.width,RightPanel.height);
		initPanel();
		setSubmitPanel();///最初はサブミットパネル出す
	}
	
	public void removeCurrentView(){
		if(currentView == null)return;
		this.remove(currentView);
		if(currentView instanceof BrowsePanel){
			((BrowsePanel)currentView).end();
		}
		currentView = null;
	}
	
	public void setBrowsePanel(List<Node> views){
		removeCurrentView();
		
		BrowsePanel browsePanel = new BrowsePanel(RightPanel.width,RightPanel.height,this,views);
		currentView = browsePanel;
		source.setTitle(source.title + "("+views.size()+")");
		this.add(currentView);
		this.repaint();
	}
	
	public void setSubmitPanel(){
		removeCurrentView();
		
		SubmitPanel submitPanel = new SubmitPanel(RightPanel.width,RightPanel.height,this);
		currentView = submitPanel;
		this.add(currentView);
		this.repaint();
	}
	
	public void setModificationPanel(Node node){///修正用
		removeCurrentView();
		
		SubmitPanel submitPanel = new SubmitPanel(RightPanel.width,RightPanel.height,this,node);
		currentView = submitPanel;
		this.add(currentView);
		this.repaint();
	}
	
	private void initPanel(){
		this.setBorder(new BevelBorder(BevelBorder.LOWERED));
	}

}
