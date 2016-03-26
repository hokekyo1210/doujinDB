package hokekyo1210.dojindb.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import hokekyo1210.dojindb.sql.Circle;
import hokekyo1210.dojindb.sql.Node;
import hokekyo1210.dojindb.sql.Root;
import hokekyo1210.dojindb.sql.SQLManager;
import hokekyo1210.dojindb.ui.util.IconUtil;

public class DBPanel extends JPanel implements MouseListener{
	
	private static final int width = 190,height = 555;
	private static final Color backGroundColor = new Color(212,230,247);
	private static final Color otherColor = new Color(246,255,247);
	
	private MainFrame source;
	
	private static JTree jTree;///アクセスを容易にしたいのでstaticに取る
	private JScrollPane scroll;
	
	private static DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("ROOT");
	private static DefaultTreeModel model;
	
	public DBPanel(int x,int y,MainFrame source){
		this.source = source;
		this.setBounds(x, y, DBPanel.width,DBPanel.height);
		this.setBackground(backGroundColor);
		initPanel();
		initComponent();
		initTree();
	}
	
	private void initTree(){///木構造を完璧にペースト
		List<Root> roots = SQLManager.getTables();
		for(Root r:roots){
			DefaultMutableTreeNode tableNode = r.getTreeNode();
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
	
	public static void addNewTable(Root table){
		rootNode.add(table);
		treeRefresh();
	}
	
	private void removeNode(List<DefaultMutableTreeNode> selection){
		List<Node> nodes = new ArrayList<Node>();///処理の順番を間違えるとDBがめんどくさいので一度選別
		List<Root> roots = new ArrayList<Root>();
		
		DefaultMutableTreeNode last = null;
		for(int i = 0;i < selection.size();i++){
			DefaultMutableTreeNode node = selection.get(i);
			DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();
			if(node instanceof Node){///サークルも消すか判定しなければいけないので頑張る
				Node n = (Node) node;
				Circle c = (Circle) parent;
				c.removeNode(n);
				if(c.getNodeCount() == 0)selection.add(parent);///もし空っぽになったならサークルも消す
				nodes.add(n);
			}else if(node instanceof Circle && parent != null){///サークルを削除するのでRootから消す
				Circle c = (Circle)node;
				Root r = (Root)parent;
				r.removeCircle(c);
			}else if(node instanceof Root){
				roots.add((Root) node);
			}
			if(parent != null){
				parent.remove(node);
			}
			last = parent;
		}
		for(Node node : nodes){
			SQLManager.removeNode(node);///あとは投げる
		}
		for(Root r : roots){
			SQLManager.removeTable(r);///投げる
		}
		
		if(selection.size() == 1){///1個なら更新を最小限に
			treeRefresh(last);
		}else{
			treeRefresh();
		}
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
		jTree.addMouseListener(this);
		model = (DefaultTreeModel)jTree.getModel();
		scroll = new JScrollPane(jTree);
		scroll.setBounds(0, 0, width, height);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroll.getVerticalScrollBar().setUnitIncrement(10);
		this.add(scroll);
	}
	
	private void dfs(DefaultMutableTreeNode now,List<DefaultMutableTreeNode> set){///再帰的に削除すべきノードを探す
		if(!set.contains(now)){
			set.add(now);
		}
		for(int i = 0;i < now.getChildCount();i++){
			DefaultMutableTreeNode next = (DefaultMutableTreeNode) now.getChildAt(i);
			dfs(next,set);
		}
	}
	
	private void showPopup(int x,int y){///ポップアップを表示する
		JPopupMenu menu = new JPopupMenu();
		JMenuItem item = new JMenuItem("削除");
		menu.add(item);
		menu.show(jTree, x, y);
		item.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(jTree.getSelectionPaths() == null)return;
				
				List<DefaultMutableTreeNode> tar = new ArrayList<DefaultMutableTreeNode>();
				for(TreePath path : jTree.getSelectionPaths()){///スタートノードは全部試す、あとは再帰
					dfs((DefaultMutableTreeNode)path.getLastPathComponent(),tar);
				}
				
				if(tar.size() == 0)return;
				int ret = JOptionPane.showConfirmDialog(source, (tar.size())+"個の項目を削除しますがよろしいですか？","",JOptionPane.OK_CANCEL_OPTION);
				if(ret == 0)
					removeNode(tar);
			}
		});

	}
	
	@Override
	public void mousePressed(MouseEvent event) {///右クリックした時にポップアップメニューを表示
		if(!SwingUtilities.isRightMouseButton(event))return;
		int tarRow = jTree.getRowForLocation(event.getX(), event.getY());
		if(tarRow == -1)return;
		if(jTree.getSelectionPaths() == null||jTree.getSelectionPaths().length == 1){
			jTree.setSelectionRow(tarRow);///右クリックした場所を選択させる
		}
		showPopup(event.getX(),event.getY());
	}

	private void initPanel() {
		this.setLayout(null);
		///this.setBorder(new EtchedBorder(EtchedBorder.LOWERED, Color.white, Color.black));
	}
	


	@Override
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}
	
}
