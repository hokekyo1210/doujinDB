package hokekyo1210.dojindb.plugins;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.tree.TreeNode;

import hokekyo1210.dojindb.sql.Node;
import hokekyo1210.dojindb.sql.Root;
import hokekyo1210.dojindb.sql.SQLManager;
import hokekyo1210.dojindb.ui.util.FontManager;

public class BarGraph extends JPopupMenu{
	
	private static final Date none = new Date(0L);
	private static final int width = 600;
	private static final int height = 300;
	
	private List<Node> targets;
	private List<Date> sortedKey = new ArrayList<Date>();
	private HashMap<Date,List<Node>> memo = new HashMap<Date,List<Node>>();
	private int month;
	private int[] amount;
	private int maxiValue = 0;
	private Date mini;
	
	public BarGraph(List<Node> targets){
		this.targets = targets;
		init();
		initComponent();
	}
	
	public BarGraph(){
		this.targets = new ArrayList<Node>();
		this.targets = SQLManager.getAllNodes();
		init();
		initComponent();
	}
	
	private void initComponent(){
		JPanel bigLabel = new JPanel();
		bigLabel.setLayout(null);
		
		int pHeight = BarGraph.height/maxiValue;
		int pWidth = BarGraph.width/amount.length;
		int height = pHeight * maxiValue;
		
		for(int i = 0;i < amount.length;i++){
			int value = amount[i];
			JLabel bar = new JLabel();
			bar.setBackground(Color.GREEN);
			bar.setBounds(pWidth * i + 10, height - (pHeight * value) - 10, pWidth, pHeight * value);
			bar.setOpaque(true);
			bar.setToolTipText(String.valueOf(value));
			bigLabel.add(bar);
			
			Date copy = new Date(mini.getTime() + (i * 2592000000L));
			if(copy.getMonth() == 0){///1ŒŽ‚¾‚¯Žæ“¾
				String year = copy.toString().split(" ")[5].substring(2);
				System.out.println(year);
				JLabel yLabel = new JLabel(year);
				yLabel.setFont(FontManager.getDefaultFont(10));
				yLabel.setHorizontalAlignment(JLabel.LEFT);
				yLabel.setBounds(pWidth * i,height - 10, 30, 10);
				bigLabel.add(yLabel);
			}
		}
		
		bigLabel.setPreferredSize(new Dimension(pWidth * amount.length + 20,height));
		
		this.add(bigLabel);
	}
	
	private void init(){
		for(Node n : targets){
			Date now = n.exDate;
			if(now.compareTo(none) == 0L)continue;///ƒ_ƒ~[
			if(!memo.containsKey(now)){
				memo.put(now, new ArrayList<Node>());
				sortedKey.add(now);
			}
			memo.get(now).add(n);
		}
		Collections.sort(sortedKey);
		Date maxi = sortedKey.get(sortedKey.size() - 1);
		mini = sortedKey.get(0);
		month = (int) ((maxi.getTime()-mini.getTime())/2592000000L);
		System.out.println(mini.toString());
		System.out.println(maxi.toString());
		System.out.println(month);
		amount = new int[month + 2];
		mini = (Date) sortedKey.get(0).clone();
		mini.setDate(1);
		for(Date key : sortedKey){
			int at = (int) ((key.getTime() - mini.getTime())/2592000000L);
			amount[at] += memo.get(key).size();
		}
		for(int i = 0;i < month + 2;i++){
			maxiValue = Math.max(maxiValue, amount[i]);
			System.out.println(i + " "+ amount[i]);
		}
	}
	


}
