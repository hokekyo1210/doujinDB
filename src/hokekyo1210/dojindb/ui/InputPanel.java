package hokekyo1210.dojindb.ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;

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
		inputField.setBounds(1, 1, 162, 32);
		inputField.setFont(new Font("メイリオ", Font.PLAIN, 14));
		this.add(inputField);
		
		JButton addBtn = new JButton("+");
		addBtn.setFont(new Font("メイリオ", Font.PLAIN, 18));
		addBtn.setMargin(new Insets(0,0,0,0));
		addBtn.setBounds(162, 1, 24, 31);
		addBtn.addActionListener(this);
		this.add(addBtn);
		
		JLabel subBtn = new JLabel(IconUtil.getIcon("edit32.png"));
		subBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		subBtn.addMouseListener(this);
		subBtn.setBounds(189, 2, 30,30);
		subBtn.setBorder(new LineBorder(Color.black, 1, false));
		this.add(subBtn);
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {///検索するよ！！
		String tableName = inputField.getText();
		if(tableName.equalsIgnoreCase(""))return;
		
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
