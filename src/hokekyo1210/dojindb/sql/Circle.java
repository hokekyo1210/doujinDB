package hokekyo1210.dojindb.sql;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

public class Circle extends MyNode{
	
	private String circleName;
	///private List<Node> nodes;
	
	///private DefaultMutableTreeNode treeNode;
	
	public Circle(String circleName){
		super(circleName);
		this.circleName = circleName;
		///nodes = new ArrayList<Node>();
		///treeNode = new DefaultMutableTreeNode(circleName);
	}
	
	public DefaultMutableTreeNode getTreeNode(){
		return this;
	}
	
	/*public List<Node> getNodes(){
		return nodes;
	}*/
	
	public void addNode(Node node){
		boolean done = false;
		for(int i = 0;i<this.getChildCount();i++){
			if(node.compare((Node)this.getChildAt(i)) <= 0){
				this.insert(node, i);
				done = true;
				break;
			}
		}
		if(!done)this.add(node);///ÅŒã‚É“ü‚ê‚é
		this.setUserObject(circleName+"("+(getNodeCount())+")");
	}
	public void removeNode(Node node){///ŽÀ‘•‚µ‚½‚Å
		for(int i = 0;i < getNodeCount();i++){
			if(this.getChildAt(i).equals(node)){
				this.remove(i);
				break;
			}
		}
		this.setUserObject(circleName+"("+(getNodeCount())+")");
	}
	public int getNodeCount(){
		return this.getChildCount();
	}
	
	public String getCircleName(){
		return circleName;
	}

	public int compare(Circle c1){
		return this.getCircleName().compareTo(c1.getCircleName());
	}


}
