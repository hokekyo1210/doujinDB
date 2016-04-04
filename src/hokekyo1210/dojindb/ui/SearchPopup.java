package hokekyo1210.dojindb.ui;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import hokekyo1210.dojindb.crawler.SearchResult;
import hokekyo1210.dojindb.ui.util.FontManager;

public class SearchPopup extends JPopupMenu implements MouseListener{
	
	private SubmitPanel source;
	private List<SearchResult> results;
	private List<String> converted = new ArrayList<String>();
	private HashMap<String,SearchResult> idmemo = new HashMap<String,SearchResult>();
	
	private JList list;
	
	
	public SearchPopup(List<SearchResult> results,SubmitPanel source){
		this.results = results;
		this.source = source;
		for(SearchResult sr : results){
			String key = sr.title+"/"+sr.circle;
			converted.add(key);
			idmemo.put(key,sr);
		}
		initPopup();
	}
	@Override
	public void mousePressed(MouseEvent event) {
		if(event.getClickCount() != 2)return;
		if(!SwingUtilities.isLeftMouseButton(event))return;
		String tar = (String)list.getSelectedValue();
		SearchResult main = idmemo.get(tar);
		try{
			main.secondSearch();///ダブルクリックされたやつを本サーチ
			source.setResult(main);
		}catch(Exception e){
			///失敗
			e.printStackTrace();
		}
		this.setVisible(false);
	}

	private void initPopup() {
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		list = new JList(converted.toArray());
		list.addMouseListener(this);
		JScrollPane scroll = new JScrollPane(list);
		scroll.setBorder(null);
		list.setFocusable( false );
		list.setFont(FontManager.getDefaultFont(12));
        scroll.getVerticalScrollBar().setFocusable( false ); 
        scroll.getHorizontalScrollBar().setFocusable( false ); 
		this.add(scroll);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
