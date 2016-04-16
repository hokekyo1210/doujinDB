package hokekyo1210.dojindb.ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;

import hokekyo1210.dojindb.sql.Node;
import hokekyo1210.dojindb.sql.SQLManager;
import hokekyo1210.dojindb.ui.util.IconUtil;

public class InputPanel extends JPanel implements ActionListener, MouseListener{
	
	private static final int width = 220,height = 32;
	
	private MainFrame source;
	
	private JTextField inputField;
	
	public InputPanel(int x,int y,MainFrame source){
		this.source = source;
		this.setBounds(x, y, InputPanel.width, InputPanel.height);
		initPanel();
		initComponents();
	}

	private void initComponents() {
		inputField = new JTextField();
		inputField.setBounds(1, 1, 156, 32);
		inputField.setFont(new Font("メイリオ", Font.PLAIN, 14));
		inputField.addActionListener(this);
		this.add(inputField);
		
		JButton addBtn = new JButton(IconUtil.getIcon("zoom.png"));
		addBtn.setFont(new Font("メイリオ", Font.PLAIN, 18));
		addBtn.setMargin(new Insets(0,0,0,0));
		addBtn.setBounds(156, 1, 30, 31);
		addBtn.addActionListener(this);
		this.add(addBtn);
		
		JLabel subBtn = new JLabel(IconUtil.getIcon("edit32.png"));
		subBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		subBtn.addMouseListener(this);
		subBtn.setBounds(189, 2, 30,30);
		subBtn.setBorder(new LineBorder(Color.black, 1, false));
		this.add(subBtn);
	}
	
	private void searchDB(String word){
		List<Node> target = SQLManager.getAllNodes();
		List<Node> views = new ArrayList<Node>();
		for(Node node : target){
			if(!node.hasWord(word))continue;
			views.add(node);
		}
		Collections.sort(views,new Node());
		source.getRightPanel().setBrowsePanel(views);
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {///検索するよ！！
		String word = inputField.getText();
		if(word.equalsIgnoreCase(""))return;
		searchDB(word);
	}
	
	@Override
	public void mousePressed(MouseEvent event) {
		if(!SwingUtilities.isLeftMouseButton(event))return;
		source.getRightPanel().setSubmitPanel();
	}

	private void initPanel() {
		this.setLayout(null);
	}
	
	@Override
	public void mouseClicked(MouseEvent event) {

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
