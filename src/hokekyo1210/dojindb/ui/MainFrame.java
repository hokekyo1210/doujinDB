package hokekyo1210.dojindb.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MainFrame extends JFrame implements ActionListener{
	
	private static final int width = 630,
							height = 638;
	
	private String title;
	
	private InputPanel inputPanel;
	private DBPanel dbPanel;
	private RightPanel rightPanel;
	
	public MainFrame(String title){
		this.title = title;
		initFrame();
		initComponents();
		
		this.setVisible(true);///ここでUIを可視化
	}
	
	private void initFrame(){
		this.setTitle(title);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(MainFrame.width, MainFrame.height);
		this.setResizable(false);
		this.setLocationRelativeTo(null);///画面の中央に移動
	}
	
	private void initComponents(){
		this.setLayout(null);///レイアウトをnullにする！
		/////JMenuBar追加
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("File");
		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.addActionListener(this);
		menu.add(exitItem);
		menuBar.add(menu);
		this.setJMenuBar(menuBar);
		
		inputPanel = new InputPanel(0,0,this);
		dbPanel = new DBPanel(1,34,this);
		rightPanel = new RightPanel(192,0);
		this.add(inputPanel);
		this.add(dbPanel);
		this.add(rightPanel);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		JMenuItem source = (JMenuItem) event.getSource();
		if(source.getText().equals("Exit")){
			System.exit(0);
		}
	}
}
