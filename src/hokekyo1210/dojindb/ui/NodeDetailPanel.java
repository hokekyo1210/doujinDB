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
			node.loadImage();///非同期でのロードなので画像がnullの可能性がある、その場合先回りロードをする
			imageLabel = node.thumbnailLabel;
			int thWidth = node.thumbnail.getIconWidth();
			int thHeight = node.thumbnail.getIconHeight();
			int calc = (panelHeight-thHeight)/2;
			int calc2 = (92-thWidth)/2;
			imageLabel.setBounds(calc2, calc, thWidth+2, thHeight+2);
			imageLabel.setBorder(new LineBorder(Color.black,1));
			imageLabel.addMouseListener(this);///ポップアップ用のリスナー
			imageLabel.addMouseMotionListener(this);
			this.add(imageLabel);
		}
		
		JSeparator separator = new JSeparator(JSeparator.VERTICAL);
		separator.setBounds(92, 6, 4, 108);
		this.add(separator);
		
		JLabel label1 = new JLabel("タイトル:");
		titleLabel = new JLabel("<html><u>"+node.title+"</u></html>");
		label1.setFont(FontManager.getDefaultFont(11));
		titleLabel.setFont(FontManager.getDefaultFontU(14));
		int titleWidth = titleLabel.getPreferredSize().width;
		panelWidth = Math.max(panelWidth, 145 + titleWidth + 10);///タイトルがはみ出す場合のために常にチェック
		label1.setBounds(96, 3, 50, 20);
		titleLabel.setBounds(145, 4, titleWidth, 20);
		this.add(label1);
		this.add(titleLabel);
		
		JLabel label2 = new JLabel("サークル:");
		circleLabel = new JLabel(node.circle);
		label2.setFont(FontManager.getDefaultFont(11));
		circleLabel.setFont(FontManager.getDefaultFont(13));
		label2.setBounds(96, 23+3, 50, 20);
		circleLabel.setBounds(145, 24+3, circleLabel.getPreferredSize().width, 20);
		this.add(label2);
		this.add(circleLabel);
		
		JLabel label3 = new JLabel("作者名:");
		artistLabel = new JLabel(node.artist);
		label3.setFont(FontManager.getDefaultFont(11));
		artistLabel.setFont(FontManager.getDefaultFont(13));
		label3.setBounds(107, 43+6, 50, 20);
		artistLabel.setBounds(145, 44+6, artistLabel.getPreferredSize().width, 20);
		this.add(label3);
		this.add(artistLabel);
		
		String tag = "";
		for(String l : node.tags){
			if(!tag.equals(""))tag += ",";
			tag += l;
		}
		JLabel label5 = new JLabel("タグ:");
		tagLabel = new JLabel(tag);
		label5.setFont(FontManager.getDefaultFont(11));
		tagLabel.setFont(FontManager.getDefaultFont(13));
		panelWidth = Math.max(panelWidth, 145 + tagLabel.getPreferredSize().width + 10);///タグがはみ出す場合のために常にチェック
		label5.setBounds(118, 63+9, 40, 20);
		tagLabel.setBounds(145, 64+9, tagLabel.getPreferredSize().width, 20);
		this.add(label5);
		this.add(tagLabel);
		
		JLabel label4 = new JLabel("発行日:");
		dateLabel = new JLabel(node.date);
		label4.setFont(FontManager.getDefaultFont(11));
		dateLabel.setFont(FontManager.getDefaultFont(13));
		label4.setBounds(107, 83+12, 50, 20);
		dateLabel.setBounds(145, 84+12, dateLabel.getPreferredSize().width, 20);
		this.add(label4);
		this.add(dateLabel);
		
		if(!node.comment.equals("None")){
			JLabel label6 = new JLabel("コメント表示");
			label6.setFont(FontManager.getDefaultFont(12));
			label6.setBounds(250, 83+12, 78, 20);
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
		popup = ImagePopup.getImagePopup(node);
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
	public void mouseMoved(MouseEvent event) {///動き監視
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
