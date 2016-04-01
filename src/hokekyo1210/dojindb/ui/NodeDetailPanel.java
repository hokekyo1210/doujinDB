package hokekyo1210.dojindb.ui;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.LineBorder;

import hokekyo1210.dojindb.main.Main;
import hokekyo1210.dojindb.sql.Node;
import hokekyo1210.dojindb.ui.util.FontManager;

public class NodeDetailPanel extends JPanel implements MouseListener , MouseMotionListener{
	
	private static final Color backGround = new Color(212,230,247);
	private static final Color backGround2 = new Color(212,220,247);
	
	private int panelWidth,panelHeight,row;
	
	private Node node;
	private BrowsePanel source;
	
	private JLabel imageLabel;
	private JLabel titleLabel,circleLabel,artistLabel,dateLabel,tagLabel;
	
	public NodeDetailPanel(int row,int width,int height,Node node,BrowsePanel source){
		this.panelWidth = width;
		this.panelHeight = height;
		this.row = row;
		this.node = node;
		this.source = source;
		
		///this.setBorder(new LineBorder(Color.BLACK, 1));
		if(row % 2 == 1){
			this.setBackground(backGround);
		}else{
			this.setBackground(backGround2);
		}
		initComponent();
	}
	
	private void initComponent() {
		this.setLayout(null);
		
		if(!node.image.equals("None")){
			node.loadImage();///�񓯊��ł̃��[�h�Ȃ̂ŉ摜��null�̉\��������A���̏ꍇ���胍�[�h������
			imageLabel = node.thumbnailLabel;
			int thWidth = node.thumbnail.getIconWidth();
			int thHeight = node.thumbnail.getIconHeight();
			int calc = (panelHeight-thHeight)/2;
			int calc2 = ((int)Main.DIAMETER*92-thWidth)/2;
			imageLabel.setBounds(calc2, calc, thWidth+2, thHeight+2);
			imageLabel.setBorder(new LineBorder(Color.black,1));
			imageLabel.addMouseListener(this);///�|�b�v�A�b�v�p�̃��X�i�[
			imageLabel.addMouseMotionListener(this);
			this.add(imageLabel);
		}
		
		JSeparator separator = new JSeparator(JSeparator.VERTICAL);
		separator.setBounds((int)Main.DIAMETER*92, (int)Main.DIAMETER*6, (int)Main.DIAMETER*4, (int)Main.DIAMETER*108);
		this.add(separator);
		
		JLabel label1 = new JLabel("�^�C�g��:");
		titleLabel = new JLabel("<html><u>"+node.title+"</u></html>");
		label1.setFont(FontManager.getDefaultFont(11));
		titleLabel.setFont(FontManager.getDefaultFontU(14));
		int titleWidth = titleLabel.getPreferredSize().width;
		panelWidth = Math.max(panelWidth, (int)Main.DIAMETER*145 + titleWidth + (int)Main.DIAMETER*10);///�^�C�g�����͂ݏo���ꍇ�̂��߂ɏ�Ƀ`�F�b�N
		label1.setBounds((int)Main.DIAMETER*96, (int)Main.DIAMETER*3, (int)Main.DIAMETER*50, (int)Main.DIAMETER*20);
		titleLabel.setBounds((int)Main.DIAMETER*145, (int)Main.DIAMETER*4, titleWidth, (int)Main.DIAMETER*20);
		this.add(label1);
		this.add(titleLabel);
		
		JLabel label2 = new JLabel("�T�[�N��:");
		circleLabel = new JLabel(node.circle);
		label2.setFont(FontManager.getDefaultFont(11));
		circleLabel.setFont(FontManager.getDefaultFont(13));
		label2.setBounds((int)Main.DIAMETER*96, (int)Main.DIAMETER*(23+3), (int)Main.DIAMETER*50, (int)Main.DIAMETER*20);
		circleLabel.setBounds((int)Main.DIAMETER*145, (int)Main.DIAMETER*(24+3), circleLabel.getPreferredSize().width, (int)Main.DIAMETER*20);
		this.add(label2);
		this.add(circleLabel);
		
		JLabel label3 = new JLabel("��Җ�:");
		artistLabel = new JLabel(node.artist);
		label3.setFont(FontManager.getDefaultFont(11));
		artistLabel.setFont(FontManager.getDefaultFont(13));
		label3.setBounds((int)Main.DIAMETER*107, (int)Main.DIAMETER*(43+6), (int)Main.DIAMETER*50, (int)Main.DIAMETER*20);
		artistLabel.setBounds((int)Main.DIAMETER*145, (int)Main.DIAMETER*(44+6), artistLabel.getPreferredSize().width, (int)Main.DIAMETER*20);
		this.add(label3);
		this.add(artistLabel);
		
		String tag = "";
		for(String l : node.tags){
			if(!tag.equals(""))tag += ",";
			tag += l;
		}
		JLabel label5 = new JLabel("�^�O:");
		tagLabel = new JLabel(tag);
		label5.setFont(FontManager.getDefaultFont(11));
		tagLabel.setFont(FontManager.getDefaultFont(13));
		panelWidth = Math.max(panelWidth, (int)Main.DIAMETER*145 + tagLabel.getPreferredSize().width + (int)Main.DIAMETER*10);///�^�O���͂ݏo���ꍇ�̂��߂ɏ�Ƀ`�F�b�N
		label5.setBounds((int)Main.DIAMETER*118, (int)Main.DIAMETER*(63+9), (int)Main.DIAMETER*40, (int)Main.DIAMETER*20);
		tagLabel.setBounds((int)Main.DIAMETER*145, (int)Main.DIAMETER*(64+9), tagLabel.getPreferredSize().width, (int)Main.DIAMETER*20);
		this.add(label5);
		this.add(tagLabel);
		
		JLabel label4 = new JLabel("���s��:");
		dateLabel = new JLabel(node.date);
		label4.setFont(FontManager.getDefaultFont(11));
		dateLabel.setFont(FontManager.getDefaultFont(13));
		label4.setBounds((int)Main.DIAMETER*107, (int)Main.DIAMETER*(83+12), (int)Main.DIAMETER*50, (int)Main.DIAMETER*20);
		dateLabel.setBounds((int)Main.DIAMETER*145, (int)Main.DIAMETER*(84+12), dateLabel.getPreferredSize().width, (int)Main.DIAMETER*20);
		this.add(label4);
		this.add(dateLabel);
		
		if(!node.comment.equals("None")){
			JLabel label6 = new JLabel("�R�����g�\��");
			label6.setFont(FontManager.getDefaultFont(12));
			label6.setBounds((int)Main.DIAMETER*250, (int)Main.DIAMETER*(83+12), (int)Main.DIAMETER*78, (int)Main.DIAMETER*20);
			label6.setBorder(new LineBorder(Color.BLACK,1,true));
			this.add(label6);
		}

		
	}

	public void reloadBounds(int x,int y){
		this.setBounds(x, y, 1000, panelHeight);
	}
	
	public int getPanelWidth(){
		return panelWidth;
	}
	
	private boolean isEntering = false;
	private ImagePopup popup = null;
	
	@Override
	public void mouseEntered(MouseEvent event) {
		isEntering = true;
		popup = new ImagePopup(node);
		try{
			popup.show(this, event.getXOnScreen()+5, event.getYOnScreen()+5);
		}catch(Exception e){
		}
	}

	@Override
	public void mouseExited(MouseEvent event) {
		isEntering = false;
		popup.setVisible(false);
		popup = null;
	}
	
	@Override
	public void mouseMoved(MouseEvent event) {///�����Ď�
		if(popup != null){
			popup.setLocation(event.getXOnScreen()+5, event.getYOnScreen()+5);
		}		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
