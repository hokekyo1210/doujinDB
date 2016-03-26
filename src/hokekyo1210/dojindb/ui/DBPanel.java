package hokekyo1210.dojindb.ui;

import java.awt.Color;
import java.awt.Font;
import java.util.Collection;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

import hokekyo1210.dojindb.sql.Circle;
import hokekyo1210.dojindb.sql.Node;
import hokekyo1210.dojindb.sql.Root;
import hokekyo1210.dojindb.sql.SQLManager;
import hokekyo1210.dojindb.ui.util.IconUtil;

public class DBPanel extends JPanel{
	
	private static final int width = 190,height = 555;
	private static final Color backGroundColor = new Color(212,230,247);
	private static final Color otherColor = new Color(246,255,247);
	
	private MainFrame source;
	
	private static JTree jTree;
	private JScrollPane scroll;
	
	private DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("ROOT");
	private static DefaultTreeModel model;
	
	private void initTree(){///木構造を完璧にペースト
		List<Root> roots = SQLManager.getTables();
		for(Root r:roots){
			DefaultMutableTreeNode tableNode = r.getTreeNode();
			/*Collection<Circle> circles = r.getCircles();
			for(Circle c : circles){
				DefaultMutableTreeNode circleNode = c.getTreeNode();
				List<Node> nodes = c.getNodes();
				for(Node node : nodes){
					circleNode.add(node.getTreeNode());
				}
				tableNode.add(circleNode);
			}*/
			rootNode.add(tableNode);
		}
		model.reload();
		this.repaint();
	}
	
	public static void treeRefresh(){
		model.reload();
	}
	public static void treeRefresh(TreeNode node){
		model.nodeStructureChanged(node);
	}
	
	public DBPanel(int x,int y,MainFrame source){
		this.source = source;
		this.setBounds(x, y, DBPanel.width,DBPanel.height);
		this.setBackground(backGroundColor);
		initPanel();
		initComponent();
		initTree();
	}	

	private void initComponent() {
		DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
		renderer.setBackgroundNonSelectionColor(backGroundColor);
		renderer.setOpenIcon(IconUtil.getIcon("folderopen.png"));
		renderer.setClosedIcon(IconUtil.getIcon("folder.png"));
		renderer.setLeafIcon(IconUtil.getIcon("file.png"));
		renderer.setFont(new Font("メイリオ", Font.PLAIN, 12));
		jTree = new JTree(rootNode);
		jTree.setCellRenderer(renderer);
		jTree.setBackground(backGroundColor);
		jTree.setRootVisible(false);
		jTree.setShowsRootHandles(true);
		model = (DefaultTreeModel)jTree.getModel();
		scroll = new JScrollPane(jTree);
		scroll.setBounds(0, 0, width, height);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroll.getVerticalScrollBar().setUnitIncrement(10);
		this.add(scroll);
	}

	private void initPanel() {
		this.setLayout(null);
		///this.setBorder(new EtchedBorder(EtchedBorder.LOWERED, Color.white, Color.black));
	}
	
}
