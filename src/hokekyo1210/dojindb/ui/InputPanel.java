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
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;

import hokekyo1210.dojindb.main.Main;
import hokekyo1210.dojindb.sql.SQLManager;
import hokekyo1210.dojindb.ui.util.IconUtil;

public class InputPanel extends JPanel implements ActionListener, MouseListener{
	
	private static final int width = 191,height = 32;
	
	private MainFrame source;
	
	private JTextField inputField;
	
	public InputPanel(int x,int y,MainFrame source){
		this.source = source;
		this.setBounds(x, y, (int)Main.DIAMETER*InputPanel.width, (int)Main.DIAMETER*InputPanel.height);
		initPanel();
		initComponents();
	}

	private void initComponents() {
		inputField = new JTextField();
		inputField.setBounds(1, 1, (int)Main.DIAMETER*132, (int)Main.DIAMETER*32);
		inputField.setFont(new Font("メイリオ", Font.PLAIN, (int)Main.DIAMETER*14));
		this.add(inputField);
		
		JButton addBtn = new JButton("+");
		addBtn.setFont(new Font("メイリオ", Font.PLAIN, (int)Main.DIAMETER*18));
		addBtn.setMargin(new Insets(0,0,0,0));
		addBtn.setBounds((int)Main.DIAMETER*132, 1, (int)Main.DIAMETER*24, (int)Main.DIAMETER*31);
		addBtn.addActionListener(this);
		this.add(addBtn);
		
		JLabel subBtn = new JLabel(IconUtil.getIcon("edit32.png"));
		subBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		subBtn.addMouseListener(this);
		subBtn.setBounds((int)Main.DIAMETER*159, 2, (int)Main.DIAMETER*30,(int)Main.DIAMETER*30);
		subBtn.setBorder(new LineBorder(Color.black, 1, false));
		this.add(subBtn);
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {///新しいテーブルを追加するよ！
		String tableName = inputField.getText();
		if(tableName.equalsIgnoreCase(""))return;
		SQLManager.addNewTable(tableName);
		inputField.setText("");
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
