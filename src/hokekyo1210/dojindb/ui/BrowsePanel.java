package hokekyo1210.dojindb.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import hokekyo1210.dojindb.main.Main;
import hokekyo1210.dojindb.sql.Node;

public class BrowsePanel extends JPanel{
	
	private static final Color backGround = new Color(212,210,247);
	public static final int detailPanelHeight = (int)Main.DIAMETER*120;
	
	private int panelWidth,panelHeight;
	private RightPanel source;
	
	private JScrollPane scroll;
	private JPanel ground;///これに色々貼ってく
	private int scrollBarWidth = (int)Main.DIAMETER*24;///スクロールバーの幅
	private int scrollBarInc = (int)Main.DIAMETER*20;///スクロール速度
	
	private List<NodeDetailPanel> panels = new ArrayList<NodeDetailPanel>();
	private int panelNum = 0;
	
	public BrowsePanel(int width,int height,RightPanel source,List<Node> views){
		this.panelWidth = width;
		this.panelHeight = height;
		this.source = source;
		initPanel();
		addNode(views);
		resizeGround();
		System.out.println(scrollBarWidth);
	}
	
	public void addNode(Node node){
		NodeDetailPanel detail = new NodeDetailPanel(panelNum,panelWidth-scrollBarWidth,detailPanelHeight,node,this);
		detail.reloadBounds(0, panelNum * detailPanelHeight);
		panels.add(detail);
		ground.add(detail);
		panelNum++;
	}
	
	public void addNode(List<Node> nodes){
		for(Node node:nodes)addNode(node);
	}
	
	public void resizeGround(){
		int maxi = 0;
		for(NodeDetailPanel p : panels){
			maxi = Math.max(maxi, p.getPanelWidth());
		}
		ground.setPreferredSize(new Dimension(maxi,panelNum * detailPanelHeight));
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				scroll.revalidate();
			}
		});
	}


	private void initPanel() {
		this.setLayout(null);
		this.setBounds(2, 2, panelWidth-3, panelHeight-3);
		ground = new JPanel();
		ground.setLayout(null);
		ground.setBackground(backGround);
		scroll = new JScrollPane(ground);
		scroll.setBounds(0, 0, panelWidth-3, panelHeight-3);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.getVerticalScrollBar().setUnitIncrement(scrollBarInc);
		this.add(scroll);
	}

}
