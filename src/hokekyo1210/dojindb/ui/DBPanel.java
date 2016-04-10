package hokekyo1210.dojindb.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
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

import hokekyo1210.dojindb.main.Main;
import hokekyo1210.dojindb.sql.Circle;
import hokekyo1210.dojindb.sql.MyNode;
import hokekyo1210.dojindb.sql.Node;
import hokekyo1210.dojindb.sql.Root;
import hokekyo1210.dojindb.sql.SQLManager;
import hokekyo1210.dojindb.ui.util.FontManager;
import hokekyo1210.dojindb.ui.util.IconTreeCellRenderer;
import hokekyo1210.dojindb.ui.util.IconUtil;
import hokekyo1210.dojindb.ui.util.MyDropFileHandler;

public class DBPanel extends JPanel implements MouseListener , ActionListener{
	
	private static final int width = 220,height = 555;
	private static final Color backGroundColor = new Color(212,230,247);
	private static final Color otherColor = new Color(246,255,247);
	
	private MainFrame source;
	
	private static JTree jTree;///�A�N�Z�X��e�Ղɂ������̂�static�Ɏ��
	private JScrollPane scroll;
	
	private static DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("ROOT");
	private static DefaultTreeModel model;
	
	public DBPanel(int x,int y,MainFrame source){
		this.source = source;
		this.setBounds(x, y, (int)Main.DIAMETER*DBPanel.width,(int)Main.DIAMETER*DBPanel.height);
		this.setBackground(backGroundColor);
		initPanel();
		initComponent();
		initTree();
	}
	
	private void initTree(){///�؍\���������Ƀy�[�X�g
		List<Root> roots = SQLManager.getTables();
		for(Root r:roots){
			DefaultMutableTreeNode tableNode = r.getTreeNode();
			rootNode.add(tableNode);
		}
		treeRefresh();
		this.repaint();
	}
	
	public static void treeRefresh(){///JTree�����t���b�V�����邯�ǂ���܂łɊJ���Ă��p�X���ێ�����
		long start = System.currentTimeMillis();
		List<DefaultMutableTreeNode> open = new ArrayList<DefaultMutableTreeNode>();///�J������Ԃ��ێ�����
		for(int r = 0;r < jTree.getRowCount();r++){
			TreePath path = jTree.getPathForRow(r);
			DefaultMutableTreeNode n = (DefaultMutableTreeNode) path.getLastPathComponent();
			if(jTree.isExpanded(path) && n instanceof MyNode){
				MyNode mn = (MyNode) n;
				if(mn.isDead())continue;
				open.add(n);
			}
		}
		for(int i = 0;i < rootNode.getChildCount();i++){///��ʂ̂Ƃ���ɍ�i�����o��
			Root r = (Root) rootNode.getChildAt(i);
			int num = 0;
			for(int j = 0;j < r.getChildCount();j++){
				num += ((Circle)r.getChildAt(j)).getNodeCount();
			}
			r.setUserObject(r.getName()+"("+num+")");
		}
		model.reload();
		for(int r = 0;r < jTree.getRowCount();r++){
			TreePath path = jTree.getPathForRow(r);
			DefaultMutableTreeNode n = (DefaultMutableTreeNode) path.getLastPathComponent();
			if(open.contains(n)){
				jTree.expandPath(path);
			}
		}
		jTree.repaint();
		System.out.println("Refresh completed. ["+(System.currentTimeMillis() - start)+" ms]");
	}
	/*public static void treeRefresh(TreeNode node){
		model.nodeStructureChanged(node);
	}*/
	
	public static void addNewTable(Root table){
		rootNode.add(table);
		treeRefresh();
	}
	
	public static void removeNode(List<DefaultMutableTreeNode> selection,boolean refresh){
		List<Node> nodes = new ArrayList<Node>();///�����̏��Ԃ��ԈႦ���DB���߂�ǂ������̂ň�x�I��
		List<Root> roots = new ArrayList<Root>();
		
		for(int i = 0;i < selection.size();i++){
			DefaultMutableTreeNode node = selection.get(i);
			DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();
			if(node instanceof Node){///�T�[�N�������������肵�Ȃ���΂����Ȃ��̂Ŋ撣��
				Node n = (Node) node;
				Circle c = (Circle) parent;
				c.removeNode(n);
				if(c.getNodeCount() == 0)selection.add(parent);///��������ۂɂȂ����Ȃ�T�[�N��������
				nodes.add(n);
			}else if(node instanceof Circle && parent != null){///�T�[�N�����폜����̂�Root�������
				Circle c = (Circle)node;
				Root r = (Root)parent;
				r.removeCircle(c);
				c.setDead(true);
			}else if(node instanceof Root){
				Root r = (Root)node;
				roots.add(r);
				r.setDead(true);
				rootNode.remove(r);
			}
		}
		for(Node node : nodes){
			SQLManager.removeNode(node);///���Ƃ͓�����
		}
		for(Root r : roots){
			SQLManager.removeTable(r);///������
		}
		if(refresh)
			treeRefresh();
	}
	
	public static void removeNode(Node node,boolean refresh){
		List<DefaultMutableTreeNode> tmp = new ArrayList<DefaultMutableTreeNode>();
		tmp.add(node);
		removeNode(tmp,refresh);
	}
	


	private void initComponent() {
		IconTreeCellRenderer renderer = new IconTreeCellRenderer();
		renderer.setFont(new Font("���C���I", Font.PLAIN, (int)Main.DIAMETER*12));
		renderer.setBigFont(new Font("���C���I", Font.BOLD, (int)Main.DIAMETER*12));
		jTree = new JTree(rootNode);
//		jTree.setRowHeight(Main.TreeRowHeight);
		jTree.setRowHeight(0);
		jTree.setCellRenderer(renderer);
		jTree.setBackground(backGroundColor);
		jTree.setRootVisible(false);
		jTree.setShowsRootHandles(true);
		jTree.addMouseListener(this);
		model = (DefaultTreeModel)jTree.getModel();
		scroll = new JScrollPane(jTree);
		scroll.setBounds(0, 0, (int)Main.DIAMETER*width, (int)Main.DIAMETER*height);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroll.getVerticalScrollBar().setUnitIncrement(10);
		this.add(scroll);
	}
	
	private void showPopup(int x,int y,boolean folder){///�|�b�v�A�b�v��\������
		JPopupMenu menu = new JPopupMenu();
		JMenuItem item5 = new JMenuItem("�\��");
		JMenuItem item2 = new JMenuItem("�J��");
		JMenuItem item3 = new JMenuItem("����");
		JMenuItem item4 = new JMenuItem("�C��");
		JMenuItem item = new JMenuItem("�폜");
		item.setFont(FontManager.getDefaultFont(12));
		item2.setFont(FontManager.getDefaultFont(12));
		item3.setFont(FontManager.getDefaultFont(12));
		item4.setFont(FontManager.getDefaultFont(12));
		item5.setFont(FontManager.getDefaultFont(12));
		menu.add(item5);
		if(folder){
			menu.add(item2);
			menu.add(item3);
		}
		if(!folder){///�m�[�h�Ȃ�C����\��
			menu.add(item4);
		}
		menu.add(item);
		menu.show(jTree, x, y);
		item5.addActionListener(this);
		item.addActionListener(this);
		item2.addActionListener(this);
		item3.addActionListener(this);
		item4.addActionListener(this);
	}
	
	private void dfs(DefaultMutableTreeNode now,List<DefaultMutableTreeNode> set){///�ċA�I�ɍ폜���ׂ��m�[�h��T��
		if(!set.contains(now)){
			set.add(now);
		}
		for(int i = 0;i < now.getChildCount();i++){
			DefaultMutableTreeNode next = (DefaultMutableTreeNode) now.getChildAt(i);
			dfs(next,set);
		}
	}
	
	private void dfsNode(DefaultMutableTreeNode now,List<Node> set){///�ċA�I�ɕ\�����ׂ��m�[�h��T��
		if(now instanceof Node && !set.contains(now)){
			set.add((Node)now);
		}
		for(int i = 0;i < now.getChildCount();i++){
			DefaultMutableTreeNode next = (DefaultMutableTreeNode) now.getChildAt(i);
			dfsNode(next,set);
		}
	}
	

	
	@Override
	public void actionPerformed(ActionEvent event) {
		if(jTree.getSelectionPaths() == null)return;
		
		String key = ((JMenuItem)event.getSource()).getText();
		if(key.equals("�폜")){
			
			List<DefaultMutableTreeNode> tar = new ArrayList<DefaultMutableTreeNode>();
			for(TreePath path : jTree.getSelectionPaths()){///�X�^�[�g�m�[�h�͑S�������A���Ƃ͍ċA
				dfs((DefaultMutableTreeNode)path.getLastPathComponent(),tar);
			}
			
			if(tar.size() == 0)return;
			int ret = JOptionPane.showConfirmDialog(source, (tar.size())+"�̍��ڂ��폜���܂�����낵���ł����H","",JOptionPane.OK_CANCEL_OPTION);
			if(ret == 0)
				removeNode(tar,true);
			
		}else if(key.equals("�J��") || key.equals("����")){
			
			List<DefaultMutableTreeNode> tar = new ArrayList<DefaultMutableTreeNode>();
			for(TreePath path : jTree.getSelectionPaths()){
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
				if(node instanceof MyNode){
					tar.add(node);
				}
			}
			if(tar.size() == 0)return;
			System.out.println("open or close :"+tar.size());
			for(int i = 0;i < jTree.getRowCount();i++){
				if(!tar.contains(jTree.getPathForRow(i).getLastPathComponent()))continue;
				if(jTree.getPathForRow(i).getLastPathComponent() instanceof Circle){
					if(key.equals("�J��"))
						jTree.expandRow(i);
					if(key.equals("����"))
						jTree.collapseRow(i);
				}
			}
			for(int i = 0;i < jTree.getRowCount();i++){
				if(!tar.contains(jTree.getPathForRow(i).getLastPathComponent()))continue;
				if(jTree.getPathForRow(i).getLastPathComponent() instanceof Root){
					if(key.equals("�J��"))
						jTree.expandRow(i);
					if(key.equals("����"))
						jTree.collapseRow(i);
				}
			}
			
		}else if(key.equals("�C��")){
			
			TreePath path = jTree.getSelectionPath();
			if(!(path.getLastPathComponent() instanceof Node))return;
			source.getRightPanel().setModificationPanel((Node)path.getLastPathComponent());
			
		}else if(key.equals("�\��")){
			
			List<Node> views = new ArrayList<Node>();///�\��������m�[�h
			TreePath[] paths = jTree.getSelectionPaths();
			for(TreePath path:paths){
				dfsNode((DefaultMutableTreeNode) path.getLastPathComponent(),views);///�m�[�h���ċA�I�ɒT������
			}
			if(views.size() == 0)return;
			Collections.sort(views, new Node());
			source.getRightPanel().setBrowsePanel(views);
			System.out.println("views "+views.size());
			
		}
	}
	
	@Override
	public void mousePressed(MouseEvent event) {///�E�N���b�N�������Ƀ|�b�v�A�b�v���j���[��\��
		if(!SwingUtilities.isRightMouseButton(event))return;
		int tarRow = jTree.getRowForLocation(event.getX(), event.getY());
		if(tarRow == -1)return;
		boolean folder = true;
		if(jTree.getSelectionPaths() == null||jTree.getSelectionPaths().length == 1){
			jTree.setSelectionRow(tarRow);///�E�N���b�N�����ꏊ��I��������
			if(jTree.getSelectionPath().getLastPathComponent() instanceof Node)
				folder = false;
		}
		showPopup(event.getX(),event.getY(),folder);
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
