package hokekyo1210.dojindb.ui;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

public class RightPanel extends JPanel{
	
	private static final int width = 430,height = 588;
	
	private JPanel currentView = null;
	
	public RightPanel(int x,int y){
		this.setLayout(null);
		this.setBounds(x, y, RightPanel.width,RightPanel.height);
		initPanel();
		setSubmitPanel();///�ŏ��̓T�u�~�b�g�p�l���o��
	}
	
	public void setSubmitPanel(){
		if(currentView!=null)
			this.remove(currentView);///�Ƃ肠�������\�����Ă�p�l������
		
		SubmitPanel submitPanel = new SubmitPanel(RightPanel.width,RightPanel.height,this);
		currentView = submitPanel;
		this.add(submitPanel);
		this.repaint();
	}
	
	private void initPanel(){
		this.setBorder(new BevelBorder(BevelBorder.LOWERED));
	}

}