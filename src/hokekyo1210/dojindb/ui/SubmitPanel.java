package hokekyo1210.dojindb.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;

import hokekyo1210.dojindb.crawler.Crawler;
import hokekyo1210.dojindb.crawler.SearchResult;
import hokekyo1210.dojindb.sql.Node;
import hokekyo1210.dojindb.sql.Root;
import hokekyo1210.dojindb.sql.SQLManager;
import hokekyo1210.dojindb.ui.util.MyDropFileHandler;

public class SubmitPanel extends JPanel implements ActionListener, MouseListener,Runnable, ItemListener{
	
	private static final Color backGroundColor = new Color(212,230,247);
	///private static final Color otherColor = new Color(232,255,247);
	private static final Color otherColor = new Color(246,255,247);
	
	private SubmitPanel own;
	private RightPanel source;
	private int panelWidth;
	private boolean isModification;///修正かどうか
	private Node beforeNode = null;
	
	private MyImageLabel imageArea;
	private JTextField titleField,circleField,artistField;
	private JFormattedTextField year,month,day;
	///JFormattedTextField textBox = new JFormattedTextField(new DecimalFormat("###.##"));
	private JComboBox<Object> kindBox;
	private Object[] kinds;
	private JTextField tagField;
	private JButton addButton;
	private List<TagLabel> tags = new ArrayList<TagLabel>();
	
	private JTextArea commentArea;
	private JButton clearButton,submitButton;
	
	private static Crawler workingThread = null;
	
	public SubmitPanel(int width,int height,RightPanel source){
		this.own = this;
		this.panelWidth = width;
		this.source = source;
		this.isModification = false;
		this.setBounds(2, 2, width-3, height-3);
		this.setBackground(backGroundColor);
		initPanel();
		initComponents();
		
	}
	
	public SubmitPanel(int width,int height,RightPanel src,Node node){///修正用
		this.own = this;
		this.panelWidth = width;
		this.source = src;
		this.isModification = true;
		this.beforeNode = node;
		this.setBounds(2, 2, width-3, height-3);
		this.setBackground(backGroundColor);
		initPanel();
		initComponents();
		titleField.setText(node.title);
		circleField.setText(node.circle);
		if(!node.artist.equals("None")){
			artistField.setText(node.artist);
		}
		String[] date = node.date.split("-");
		if(date.length == 3){
			year.setText(date[0]);
			month.setText(date[1]);
			day.setText(date[2]);
		}
		kindBox.setSelectedItem(node.table);
		if(!node.comment.equals("None")){
			commentArea.setText(node.comment);
		}
		for(String t:node.tags){
			addTag(t);
		}
		if(!node.image.equals("None")){
			try {
				imageArea.setImageIcon(new File(node.image));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	
	public void submit(){///データベースに追加
		if(isModification){///修正用なら前のdbを削除
			DBPanel.removeNode(beforeNode,false);
		}
		String title = titleField.getText();
		String circle = circleField.getText();
		String artist = "None";
		String date = "None";
		List<String> tagArray = new ArrayList<String>();
		String comment = "None";
		String image = "None";
		String thumb = "None";
		if(!artistField.getText().equalsIgnoreCase("")){
			artist = artistField.getText();
		}
		if(!year.getText().equalsIgnoreCase("")&&
		   !month.getText().equalsIgnoreCase("")&&
		   !day.getText().equalsIgnoreCase("")){
			date = year.getText()+"-"+month.getText()+"-"+day.getText();
		}
		
		for(int i = 0;i < tags.size();i++){
			tagArray.add(tags.get(i).getTag());
		}
		
		if(!commentArea.getText().equalsIgnoreCase("")){
			comment = commentArea.getText();
		}
		if(imageArea.getSource() != null){///画像を別の場所に生成
			image = imageArea.save();
		}
		Node result = SQLManager.addData((String)kindBox.getSelectedItem(), title, circle, artist, date, tagArray, comment, image,thumb);
		if(result != null){
			submitButton.setForeground(Color.GREEN);
		}else{
			submitButton.setForeground(Color.RED);
		}
		beforeNode = result;
		///source.setSubmitPanel();///submitパネルをリロード!
	}

	private void initPanel(){
		this.setLayout(null);
	}
	
	private void initComponents() {
		
		List<Root> tables = SQLManager.getTables();
		List<String> temp = new ArrayList<String>();
		for(Root r:tables)temp.add(r.getName());
		temp.add("追加...");
		kinds = temp.toArray();
		
		this.setLayout(null);
		imageArea = new MyImageLabel();
		imageArea.setBackground(otherColor);
		imageArea.setOpaque(true);
		imageArea.setBorder(new EtchedBorder(EtchedBorder.RAISED, Color.white, Color.black));
		imageArea.setBounds(4, 4, 220, 300);
		imageArea.setTransferHandler(new MyDropFileHandler(imageArea));

		this.add(imageArea);
		
		JLabel titleLabel = new JLabel("タイトル:");
		titleLabel.setFont(new Font("メイリオ", Font.PLAIN, 15));
		titleLabel.setBounds(226,8,100,20);
		titleField = new JTextField();
		titleField.setBounds(228, 30, 194, 28);
		titleField.setFont(new Font("メイリオ", Font.PLAIN, 12));
		titleField.setBackground(otherColor);
		titleField.addActionListener(this);
		this.add(titleLabel);
		this.add(titleField);
		
		JLabel circleLabel = new JLabel("サークル名:");
		circleLabel.setFont(new Font("メイリオ", Font.PLAIN, 15));
		circleLabel.setBounds(226,66,100,20);
		circleField = new JTextField();
		circleField.setBounds(228, 88, 194, 28);
		circleField.setFont(new Font("メイリオ", Font.PLAIN, 12));
		circleField.setBackground(otherColor);
		this.add(circleLabel);
		this.add(circleField);
		
		JLabel artistLabel = new JLabel("著者:");
		artistLabel.setFont(new Font("メイリオ", Font.PLAIN, 15));
		artistLabel.setBounds(226,124,100,20);
		artistField = new JTextField();
		artistField.setBounds(228, 148, 194, 28);
		artistField.setFont(new Font("メイリオ", Font.PLAIN, 12));
		artistField.setBackground(otherColor);
		this.add(artistLabel);
		this.add(artistField);
		
		JLabel dateLabel = new JLabel("発行日:");
		dateLabel.setFont(new Font("メイリオ", Font.PLAIN, 15));
		dateLabel.setBounds(226,184,100,20);
		year = new JFormattedTextField(new DecimalFormat("0000"));
		month = new JFormattedTextField(new DecimalFormat("00"));
		day = new JFormattedTextField(new DecimalFormat("00"));
		JLabel yLabel = new JLabel("年");
		JLabel mLabel = new JLabel("月");
		JLabel dLabel = new JLabel("日");
		year.setFont(new Font("メイリオ", Font.PLAIN, 16));
		month.setFont(new Font("メイリオ", Font.PLAIN, 16));
		day.setFont(new Font("メイリオ", Font.PLAIN, 16));
		year.setBounds(226+20, 206, 50, 26);
		yLabel.setBounds(226+20+53, 206, 50, 26);
		month.setBounds(296+20, 206, 26, 26);
		mLabel.setBounds(296+20+29, 206, 26, 26);
		day.setBounds(344+20, 206, 26, 26);
		dLabel.setBounds(344+20+29, 206, 26, 26);
		year.setBackground(otherColor);
		month.setBackground(otherColor);
		day.setBackground(otherColor);
		this.add(dateLabel);
		this.add(year);this.add(yLabel);
		this.add(month);this.add(mLabel);
		this.add(day);this.add(dLabel);
		
		JLabel kindLabel = new JLabel("種類:");
		kindLabel.setFont(new Font("メイリオ", Font.PLAIN, 15));
		kindLabel.setBounds(226,244,100,20);
		kindBox = new JComboBox<Object>(kinds);
		kindBox.setFont(new Font("メイリオ", Font.BOLD, 15));
		kindBox.setBounds(230, 266, 190, 30);
		kindBox.addItemListener(this);
		this.add(kindLabel);
		this.add(kindBox);
		
		JLabel tagLabel = new JLabel("タグ:");
		tagField = new JTextField();
		tagField.addActionListener(this);
		addButton = new JButton("追加");
		addButton.addActionListener(this);
		tagLabel.setFont(new Font("メイリオ", Font.PLAIN, 18));
		tagField.setFont(new Font("メイリオ", Font.PLAIN, 12));
		addButton.setFont(new Font("メイリオ", Font.PLAIN, 12));
		tagField.setBounds(4, 310+2, 150, 26);
		addButton.setBounds(154, 310+2, 70, 26);
		tagLabel.setBounds(4, 338, 60, 26);
		tagField.setBackground(otherColor);
		this.add(tagField);
		this.add(addButton);
		this.add(tagLabel);
		
		JLabel commentLabel = new JLabel("コメント:");
		commentArea = new JTextArea();
		JScrollPane scrollPane = new JScrollPane(commentArea, 
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		commentArea.setLineWrap(true);
		commentLabel.setFont(new Font("メイリオ", Font.PLAIN, 18));
		commentArea.setFont(new Font("メイリオ", Font.PLAIN, 14));
		commentLabel.setBounds(4, 400, 100, 26);
		scrollPane.setBounds(4, 428, 420, 120);
		commentArea.setBackground(otherColor);
		commentArea.setEnabled(true);
		commentArea.setEditable(true);
		this.add(commentLabel);
		this.add(scrollPane);
		
		clearButton = new JButton("Clear");
		clearButton.setFont(new Font("メイリオ", Font.PLAIN, 14));
		clearButton.setBounds(250, 552, 80, 30);
		clearButton.addActionListener(this);
		
		if(!isModification){
			submitButton = new JButton("登録");
		}else{
			submitButton = new JButton("修正");
		}
		submitButton.setFont(new Font("メイリオ", Font.PLAIN, 14));
		submitButton.setBounds(340, 552, 80, 30);
		submitButton.addActionListener(this);
		submitButton.setForeground(Color.BLACK);
		this.add(submitButton);
		this.add(clearButton);
		
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				commentArea.revalidate();///これやらないとなぜか微妙な動作
				kindBox.revalidate();
			}
		});
		

	}

	private void addTag(String tag){
		TagLabel newTag = new TagLabel(tag);
		newTag.addMouseListener(this);
		tags.add(newTag);
		reloadTag();
	}
	
	private void removeTag(TagLabel target){
		this.remove(target);
		tags.remove(target);
		reloadTag();
	}
	
	private static final int offsetX = 4,offsetY = 340;
	private static final int tagPeriod = 7,tagHeight = 26;
	private void reloadTag(){
		for(TagLabel tag:tags){
			this.remove(tag);
		}
		int x = 50,y = 0;
		for(TagLabel tag:tags){
			if((offsetX+x+tag.getWidth()) > (panelWidth-4)){
				///はみだすので改行
				x = 0;y += tagHeight;
				///if(y == 2)break;///３行目は無いので追加を諦める
			}
			tag.reloadBounds(offsetX+x, offsetY+y);
			x+=tag.getWidth()+tagPeriod;
			this.add(tag);
		}
		this.repaint();
	}
	private void removeAllTag(){
		for(TagLabel tag:tags){
			this.remove(tag);
		}
		tags.clear();
		this.repaint();
	}
	
	@Override
	public void mousePressed(MouseEvent event) {
		if(event.getButton() != 1)return;
		removeTag((TagLabel)event.getSource());
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getSource().equals(addButton)){
			String target = tagField.getText();
			if(target.equalsIgnoreCase(""))return;///空文字ならダメ
			if(target.length()>0){
				addTag(target);
			}
			tagField.setText("");
		}else if(event.getSource().equals(submitButton)){
			if(titleField.getText().equalsIgnoreCase(""))return;
			if(circleField.getText().equalsIgnoreCase(""))return;
			this.submit();
		}else if(event.getSource().equals(clearButton)){
			Object tar = kindBox.getSelectedItem();///クリアした後も種別だけは保持
			this.removeAll();
			kinds = SQLManager.getTables().toArray();
			tags.clear();
			initComponents();
			kindBox.setSelectedItem(tar);
			SwingUtilities.invokeLater(new Runnable(){
				public void run(){
					source.repaint();
				}
			});
		}else if(event.getSource().equals(tagField)){///タグ入力してるときにエンター押したら追加
			addButton.doClick();
		}else if(event.getSource().equals(titleField)){
			String word = titleField.getText();
			if(word.equalsIgnoreCase(""))return;///空っぽなら動かさない
			
			if(workingThread != null && !workingThread.isFinished()){
				///検索中
				return;
			}
			titleField.setEnabled(false);///タイトルフィールドを止める
			workingThread = new Crawler(word,this);
			workingThread.start();///検索を実行する
		}
	}
	
	public void showPopup(List<SearchResult> results){
		SearchPopup popup = new SearchPopup(results,this);
		popup.show(this, titleField.getX(), titleField.getY()+28);
		titleField.setEnabled(true);///戻す
	}
	public void setResult(SearchResult result){///遅いから全部非同期で適用
		targetResult = result;
		submitButton.setEnabled(false);///実行が完了するまで登録ボタンはオフ
		Thread th = new Thread(this);
		th.start();
	}
	private SearchResult targetResult;
	
	@Override
	public void run() {
		titleField.setText(targetResult.title);
		circleField.setText(targetResult.circle);
		artistField.setText(targetResult.artist);
		year.setText(targetResult.year);
		month.setText(targetResult.month);
		day.setText(targetResult.day);
		commentArea.setText("");
		removeAllTag();///タグを全部外す
		for(String tag:targetResult.tags){
			addTag(tag);
		}
		imageArea.setImageIcon(targetResult.imageURL);
		submitButton.setEnabled(true);
		this.repaint();
	}
	
	public void remakeKindBox(String select){
		List<Root> tables = SQLManager.getTables();
		List<String> temp = new ArrayList<String>();
		for(Root r:tables)temp.add(r.getName());
		temp.add("追加...");
		kinds = temp.toArray();
		
		this.remove(kindBox);
		kindBox = new JComboBox<Object>(kinds);
		kindBox.setFont(new Font("メイリオ", Font.BOLD, 15));
		kindBox.setBounds(230, 266, 190, 30);
		kindBox.addItemListener(this);
		if(select != null && !select.equals("")){
			kindBox.setSelectedItem(select);
		}
		this.add(kindBox);
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				kindBox.revalidate();
				own.repaint();
			}
		});
	}
	
	@Override
	public void itemStateChanged(ItemEvent event) {
		if(event.getStateChange() == ItemEvent.SELECTED){
			String tar = (String)kindBox.getSelectedItem();
			if(!tar.equals("追加..."))return;
			String ret = JOptionPane.showInputDialog(own,"追加する種類名を入力してください。");
			System.out.println(ret);
			if(ret != null && !ret.equals("")){
				SQLManager.addNewTable(ret);
			}
			remakeKindBox(ret);
		}
	}
	
	

	@Override
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}





}
