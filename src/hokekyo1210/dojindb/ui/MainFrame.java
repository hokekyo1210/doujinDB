package hokekyo1210.dojindb.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import hokekyo1210.dojindb.plugins.BarGraph;
import hokekyo1210.dojindb.plugins.ReitaisaiLoader;


public class MainFrame extends JFrame implements ActionListener{
	
	private static final int width = 660,///630
							height = 636;
	
	private MainFrame own;
	public String title;
	
	private InputPanel inputPanel;
	private DBPanel dbPanel;
	private RightPanel rightPanel;
	
	public MainFrame(String title){
		this.own = this;
		this.title = title;
		initFrame();
		initComponents();
		
		this.setVisible(true);///������UI������
	}
	
	private void initFrame(){
		this.setTitle(title);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(MainFrame.width, MainFrame.height);
		this.setResizable(false);
		this.setLocationRelativeTo(null);///��ʂ̒����Ɉړ�
		
		
	}
	
	private void initComponents(){
		this.setLayout(null);///���C�A�E�g��null�ɂ���I
		/////JMenuBar�ǉ�
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("File");
		JMenuItem exitItem = new JMenuItem("Exit");
		JMenu menu2 = new JMenu("Plugin");
		JMenuItem exItem = new JMenuItem("����13");
		JMenuItem graphItem = new JMenuItem("�O���t�\��");
		exitItem.addActionListener(this);
		exItem.addActionListener(this);
		graphItem.addActionListener(this);
		menu.add(exitItem);
		menu2.add(exItem);
		menu2.add(graphItem);
		menuBar.add(menu);
		menuBar.add(menu2);
		this.setJMenuBar(menuBar);
		
		inputPanel = new InputPanel(0,0,this);
		dbPanel = new DBPanel(1,34,this);
		rightPanel = new RightPanel(222,0,this);
		this.add(inputPanel);
		this.add(dbPanel);
		this.add(rightPanel);
	}
	
	public RightPanel getRightPanel(){
		return rightPanel;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		JMenuItem source = (JMenuItem) event.getSource();
		if(source.getText().equals("Exit")){
			System.exit(0);
		}else if(source.getText().equals("����13")){
			ReitaisaiLoader sp = new ReitaisaiLoader();///���Ղ̃f�[�^�ǂݍ���
			sp.runThread();
		}else if(source.getText().equals("�O���t�\��")){
			BarGraph graph = new BarGraph();
			graph.show(this, 0, 0);
		}
	}
}
