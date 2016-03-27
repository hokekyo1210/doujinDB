package hokekyo1210.dojindb.sql;

import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

public class Circle extends MyNode{
	
	private String circleName;
	private List<Node> nodes;
	
	///private DefaultMutableTreeNode treeNode;
	
	public Circle(String circleName){
		super(circleName);
		this.circleName = circleName;
		nodes = new ArrayList<Node>();
		///treeNode = new DefaultMutableTreeNode(circleName);
	}
	
	public DefaultMutableTreeNode getTreeNode(){
		return this;
	}
	
	public List<Node> getNodes(){
		return nodes;
	}
	
	public void addNode(Node node){
		nodes.add(node);
		this.add(node.getTreeNode());
		this.setUserObject(circleName+"("+(nodes.size())+")");
	}
	public void removeNode(Node node){///����������
		nodes.remove(node);
		this.setUserObject(circleName+"("+(nodes.size())+")");
	}
	public int getNodeCount(){
		return nodes.size();
	}
	
	public String getCircleName(){
		return circleName;
	}

}