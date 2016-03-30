package hokekyo1210.dojindb.ui;

import java.util.List;

import javax.swing.JPanel;

import hokekyo1210.dojindb.sql.Node;

public class BrowsePanel extends JPanel{
	
	private RightPanel source;
	private List<Node> views;
	
	public BrowsePanel(RightPanel source,List<Node> views){
		this.source = source;
		this.views = views;
	}

}
