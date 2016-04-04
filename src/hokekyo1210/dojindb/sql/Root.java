package hokekyo1210.dojindb.sql;

import javax.swing.tree.DefaultMutableTreeNode;

public class Root extends MyNode{
	
	private String name;
	
	public Root(String name){
		super(name);
		this.name = name;
	}
	
	public DefaultMutableTreeNode getTreeNode(){
		return this;
	}
	
	/*public Collection<Circle> getCircles(){
		return circles.values();
	}*/
	
	public void addCircle(Circle circle){
		boolean done = false;
		for(int i = 0;i<this.getChildCount();i++){
			if(circle.compare((Circle)this.getChildAt(i)) <= 0){
				this.insert(circle, i);
				done = true;
				break;
			}
		}
		if(!done)this.add(circle);///ÅŒã‚É“ü‚ê‚é
	}
	
	public void removeCircle(Circle circle){
		for(int i = 0;i<this.getChildCount();i++){
			if(this.getChildAt(i).equals(circle)){
				this.remove(i);
				break;
			}
		}
	}
	
	public Circle getCircle(String key){
		for(int i = 0;i<this.getChildCount();i++){
			Circle c = (Circle) this.getChildAt(i);
			if(c.getCircleName().equals(key)){
				return c;
			}
		}
		return null;
	}
	
	public void clearCirlces(){
		while(this.getChildCount() != 0){
			this.remove(0);
		}
	}
	
	public String getName(){
		return name;
	}

}
