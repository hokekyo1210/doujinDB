package hokekyo1210.dojindb.sql;

import javax.swing.tree.DefaultMutableTreeNode;

public class MyNode extends DefaultMutableTreeNode{
	
	private boolean isDead = false;
	
	public MyNode(String name){
		super(name);
	}
	
	public boolean isDead(){
		return isDead;
	}
	
	public void setDead(boolean isDead){
		this.isDead = isDead;
	}

}
