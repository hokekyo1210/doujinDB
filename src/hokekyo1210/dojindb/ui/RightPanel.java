package hokekyo1210.dojindb.ui;

import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

import hokekyo1210.dojindb.sql.Node;

import hokekyo1210.dojindb.main.Main;

public class RightPanel extends JPanel{
	
	private static final int width = 430,height = 590;
	
	private JComponent currentView = null;
	
	public RightPanel(int x,int y){
		this.setLayout(null);
		this.setBounds(x, y, (int)Main.DIAMETER*RightPanel.width,(int)Main.DIAMETER*RightPanel.height);
		initPanel();
		setSubmitPanel();///�ŏ��̓T�u�~�b�g�p�l���o��
	}
	
	public void setBrowsePanel(List<Node> views){
		if(currentView!=null)
			this.remove(currentView);///�Ƃ肠�������\�����Ă�p�l������
		
		BrowsePanel browsePanel = new BrowsePanel((int)Main.DIAMETER*RightPanel.width,(int)Main.DIAMETER*RightPanel.height,this,views);
		currentView = browsePanel;
		this.add(currentView);
		this.repaint();
	}
	
	public void setSubmitPanel(){
		if(currentView!=null)
			this.remove(currentView);///�Ƃ肠�������\�����Ă�p�l������
		
		SubmitPanel submitPanel = new SubmitPanel((int)Main.DIAMETER*RightPanel.width,(int)Main.DIAMETER*RightPanel.height,this);
		currentView = submitPanel;
		this.add(currentView);
		this.repaint();
	}
	
	public void setModificationPanel(Node node){///�C���p
		if(currentView!=null)
			this.remove(currentView);///�Ƃ肠�������\�����Ă�p�l������
		
		SubmitPanel submitPanel = new SubmitPanel(RightPanel.width,RightPanel.height,this,node);
		currentView = submitPanel;
		this.add(currentView);
		this.repaint();
	}
	
	private void initPanel(){
		this.setBorder(new BevelBorder(BevelBorder.LOWERED));
	}

}
