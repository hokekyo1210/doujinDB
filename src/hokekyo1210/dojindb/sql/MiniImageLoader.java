package hokekyo1210.dojindb.sql;

import java.util.List;

import hokekyo1210.dojindb.ui.DBPanel;

public class MiniImageLoader implements Runnable{
	
	private List<Node> target;
	
	public MiniImageLoader(List<Node> target){
		this.target = target;
	}

	@Override
	public void run() {
		for(Node node:target)node.loadImage();
		DBPanel.treeRefresh();
	}

}
