package hokekyo1210.dojindb.sql;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

public class Circle extends MyNode{
	
	private String circleName;
	private String lastArtist;
	private String circleSpace = "";///イベント用
	
	public Circle(String circleName){
		super(circleName);
		this.circleName = circleName;
		///nodes = new ArrayList<Node>();
		///treeNode = new DefaultMutableTreeNode(circleName);
	}
	
	public DefaultMutableTreeNode getTreeNode(){
		return this;
	}
	
	public void reloadNodeName(){///ツリーに作品数を表示する
		if(circleSpace.equals("")){
			this.setUserObject(circleName+"("+(getNodeCount())+")");
		}else{
			this.setUserObject("["+circleSpace+"]"+circleName+"("+(getNodeCount())+")");
		}
	}
	
	public void addNode(Node node){
		boolean done = false;
		for(int i = 0;i<this.getChildCount();i++){
			if(node.compare((Node)this.getChildAt(i)) <= 0){
				this.insert(node, i);
				done = true;
				break;
			}
		}
		if(!done)this.add(node);///最後に入れる
		this.lastArtist = node.artist;
		reloadNodeName();
	}
	public void removeNode(Node node){///実装したで
		for(int i = 0;i < getNodeCount();i++){
			if(this.getChildAt(i).equals(node)){
				this.remove(i);
				break;
			}
		}
		reloadNodeName();
	}
	public int getNodeCount(){
		return this.getChildCount();
	}
	
	public String getCircleName(){
		return circleName;
	}
	
	public String getLastArtist(){
		return lastArtist;
	}
	public void setCircleSpace(String circleSpace){
		this.circleSpace = circleSpace;
		reloadNodeName();
	}

	public int compare(Circle c1){
		return this.getCircleName().compareToIgnoreCase(c1.getCircleName());
	}


}
