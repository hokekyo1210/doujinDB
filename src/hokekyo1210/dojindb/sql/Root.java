package hokekyo1210.dojindb.sql;

import java.util.Collection;
import java.util.HashMap;

import javax.swing.tree.DefaultMutableTreeNode;

public class Root extends DefaultMutableTreeNode{
	
	private String name;
	private HashMap<String,Circle> circles;///エッジが多くなることが予想されるので
										   ///ArrayListは使わない
//	private DefaultMutableTreeNode treeNode;
	
	public Root(String name){
		super(name);
		this.name = name;
		circles = new HashMap<String,Circle>();
//		treeNode = new DefaultMutableTreeNode(name);
	}
	
	public DefaultMutableTreeNode getTreeNode(){
		return this;
	}
	
	public Collection<Circle> getCircles(){
		return circles.values();
	}
	
	public void addCircle(Circle circle){
		circles.put(circle.getCircleName(), circle);
		this.add(circle.getTreeNode());
	}
	
	public void removeCircle(Circle circle){
		circles.put(circle.getCircleName(), null);
	}
	
	public Circle getCircle(String key){
		return circles.get(key);
	}
	
	public void clearCirlces(){
		circles.clear();
	}
	
	public String getName(){
		return name;
	}

}
