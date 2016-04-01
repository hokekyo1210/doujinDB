package hokekyo1210.dojindb.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;

import hokekyo1210.dojindb.crawler.Crawler;
import hokekyo1210.dojindb.crawler.SearchResult;
import hokekyo1210.dojindb.sql.Node;
import hokekyo1210.dojindb.main.Main;
import hokekyo1210.dojindb.sql.Root;
import hokekyo1210.dojindb.sql.SQLManager;
import hokekyo1210.dojindb.ui.util.FontManager;
import hokekyo1210.dojindb.ui.util.MyDropFileHandler;

public class SubmitPanel extends JPanel implements ActionListener, MouseListener,Runnable{
	
	private static final Color backGroundColor = new Color(212,230,247);
	///private static final Color otherColor = new Color(232,255,247);
	private static final Color otherColor = new Color(246,255,247);
	
	private RightPanel source;
	private int panelWidth;
	private boolean isModification;///修正かどうか
	private Node beforeNode = null;
	
	private MyImageLabel imageArea;
	private JTextField titleField,circleField,artistField;
	private JFormattedTextField year,month,day;
	///JFormattedTextField textBox = new JFormattedTextField(new DecimalFormat("###.##"));
	private JComboBox kindBox;
	private Object[] kinds;
	private JTextField tagField;
	private JButton addButton;
	private List<TagLabel> tags = new ArrayList<TagLabel>();
	
	private JTextArea commentArea;
	private JButton clearButton,submitButton;
	
	private static Crawler workingThread = null;
	
	public SubmitPanel(int width,int height,RightPanel source){
		this.panelWidth = width;
		this.source = source;
		this.isModification = false;
		this.setBounds(2, 2, (int)Main.DIAMETER*width-3, (int)Main.DIAMETER*height-3);
		this.setBackground(backGroundColor);
		initPanel();
		initComponents();
		
	}
	
	public SubmitPanel(int width,int height,RightPanel src,Node node){///修正用
		this.panelWidth = width;
		this.source = src;
		this.isModification = true;
		this.beforeNode = node;
		this.setBounds(2, 2, (int)Main.DIAMETER*width-3, (int)Main.DIAMETER*height-3);
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
		kinds = temp.toArray();
		
		this.setLayout(null);
		imageArea = new MyImageLabel();
		imageArea.setBackground(otherColor);
		imageArea.setOpaque(true);
		imageArea.setBorder(new EtchedBorder(EtchedBorder.RAISED, Color.white, Color.black));
		imageArea.setBounds(4, 4, (int)Main.DIAMETER*220, (int)Main.DIAMETER*300);
		imageArea.setToolTipText("ここに画像をドラッグ&ドロップ");
		imageArea.setTransferHandler(new MyDropFileHandler(imageArea));

		this.add(imageArea);
		
		JLabel titleLabel = new JLabel("タイトル:");
		titleLabel.setFont(new Font("メイリオ", Font.PLAIN, (int)Main.DIAMETER*15));
		titleLabel.setBounds((int)Main.DIAMETER*226,8,(int)Main.DIAMETER*100,(int)Main.DIAMETER*20);
		titleField = new JTextField();
		titleField.setBounds((int)Main.DIAMETER*228, (int)Main.DIAMETER*30, (int)Main.DIAMETER*194, (int)Main.DIAMETER*28);
		titleField.setFont(new Font("メイリオ", Font.PLAIN, (int)Main.DIAMETER*12));
		titleField.setBackground(otherColor);
		titleField.addActionListener(this);
		this.add(titleLabel);
		this.add(titleField);
		
		JLabel circleLabel = new JLabel("サークル名:");
		circleLabel.setFont(new Font("メイリオ", Font.PLAIN, (int)Main.DIAMETER*15));
		circleLabel.setBounds((int)Main.DIAMETER*226,(int)Main.DIAMETER*66,(int)Main.DIAMETER*100,(int)Main.DIAMETER*20);
		circleField = new JTextField();
		circleField.setBounds((int)Main.DIAMETER*228, (int)Main.DIAMETER*88, (int)Main.DIAMETER*194, (int)Main.DIAMETER*28);
		circleField.setFont(new Font("メイリオ", Font.PLAIN, (int)Main.DIAMETER*12));
		circleField.setBackground(otherColor);
		this.add(circleLabel);
		this.add(circleField);
		
		JLabel artistLabel = new JLabel("著者:");
		artistLabel.setFont(new Font("メイリオ", Font.PLAIN, (int)Main.DIAMETER*15));
		artistLabel.setBounds((int)Main.DIAMETER*226,(int)Main.DIAMETER*124,(int)Main.DIAMETER*100,(int)Main.DIAMETER*20);
		artistField = new JTextField();
		artistField.setBounds((int)Main.DIAMETER*228, (int)Main.DIAMETER*148, (int)Main.DIAMETER*194, (int)Main.DIAMETER*28);
		artistField.setFont(new Font("メイリオ", Font.PLAIN, (int)Main.DIAMETER*12));
		artistField.setBackground(otherColor);
		this.add(artistLabel);
		this.add(artistField);
		
		JLabel dateLabel = new JLabel("発行日:");
		dateLabel.setFont(new Font("メイリオ", Font.PLAIN, (int)Main.DIAMETER*15));
		dateLabel.setBounds((int)Main.DIAMETER*226,(int)Main.DIAMETER*184,(int)Main.DIAMETER*100,(int)Main.DIAMETER*20);
		year = new JFormattedTextField(new DecimalFormat("0000"));
		month = new JFormattedTextField(new DecimalFormat("00"));
		day = new JFormattedTextField(new DecimalFormat("00"));
		JLabel yLabel = new JLabel("年");
		JLabel mLabel = new JLabel("月");
		JLabel dLabel = new JLabel("日");
		yLabel.setFont(FontManager.getDefaultFont(10));
		mLabel.setFont(FontManager.getDefaultFont(10));
		dLabel.setFont(FontManager.getDefaultFont(10));
		year.setFont(new Font("メイリオ", Font.PLAIN, (int)Main.DIAMETER*16));
		month.setFont(new Font("メイリオ", Font.PLAIN, (int)Main.DIAMETER*16));
		day.setFont(new Font("メイリオ", Font.PLAIN, (int)Main.DIAMETER*16));
		year.setBounds((int)Main.DIAMETER*(226+20), (int)Main.DIAMETER*206, (int)Main.DIAMETER*50, (int)Main.DIAMETER*26);
		yLabel.setBounds((int)Main.DIAMETER*(226+20+53), (int)Main.DIAMETER*206, (int)Main.DIAMETER*50, (int)Main.DIAMETER*26);
		month.setBounds((int)Main.DIAMETER*(296+20), (int)Main.DIAMETER*206, (int)Main.DIAMETER*26, (int)Main.DIAMETER*26);
		mLabel.setBounds((int)Main.DIAMETER*(296+20+29), (int)Main.DIAMETER*206, (int)Main.DIAMETER*26, (int)Main.DIAMETER*26);
		day.setBounds((int)Main.DIAMETER*(344+20), (int)Main.DIAMETER*206, (int)Main.DIAMETER*26, (int)Main.DIAMETER*26);
		dLabel.setBounds((int)Main.DIAMETER*(344+20+29), (int)Main.DIAMETER*206, (int)Main.DIAMETER*26, (int)Main.DIAMETER*26);
		year.setBackground(otherColor);
		month.setBackground(otherColor);
		day.setBackground(otherColor);
		this.add(dateLabel);
		this.add(year);this.add(yLabel);
		this.add(month);this.add(mLabel);
		this.add(day);this.add(dLabel);
		
		JLabel kindLabel = new JLabel("種類:");
		kindLabel.setFont(new Font("メイリオ", Font.PLAIN, (int)Main.DIAMETER*15));
		kindLabel.setBounds((int)Main.DIAMETER*226,(int)Main.DIAMETER*244,(int)Main.DIAMETER*100,(int)Main.DIAMETER*20);
		kindBox = new JComboBox(kinds);
		kindBox.setFont(new Font("メイリオ", Font.BOLD, (int)Main.DIAMETER*15));
		kindBox.setBounds((int)Main.DIAMETER*230, (int)Main.DIAMETER*266, (int)Main.DIAMETER*190, (int)Main.DIAMETER*30);
		this.add(kindLabel);
		this.add(kindBox);
		
		JLabel tagLabel = new JLabel("タグ:");
		tagField = new JTextField();
		tagField.addActionListener(this);
		addButton = new JButton("追加");
		addButton.addActionListener(this);
		tagLabel.setFont(new Font("メイリオ", Font.PLAIN, (int)Main.DIAMETER*18));
		tagField.setFont(new Font("メイリオ", Font.PLAIN, (int)Main.DIAMETER*12));
		addButton.setFont(new Font("メイリオ", Font.PLAIN, (int)Main.DIAMETER*12));
		tagField.setBounds((int)Main.DIAMETER*4, (int)Main.DIAMETER*310+2, (int)Main.DIAMETER*150, (int)Main.DIAMETER*26);
		addButton.setBounds((int)Main.DIAMETER*154, (int)Main.DIAMETER*310+2, (int)Main.DIAMETER*70, (int)Main.DIAMETER*26);
		tagLabel.setBounds((int)Main.DIAMETER*4, (int)Main.DIAMETER*338, (int)Main.DIAMETER*60, (int)Main.DIAMETER*26);
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
		commentLabel.setFont(new Font("メイリオ", Font.PLAIN, (int)Main.DIAMETER*18));
		commentArea.setFont(new Font("メイリオ", Font.PLAIN, (int)Main.DIAMETER*14));
		commentLabel.setBounds((int)Main.DIAMETER*4, (int)Main.DIAMETER*400, (int)Main.DIAMETER*100, (int)Main.DIAMETER*26);
		scrollPane.setBounds((int)Main.DIAMETER*4, (int)Main.DIAMETER*428, (int)Main.DIAMETER*420, (int)Main.DIAMETER*120);
		commentArea.setBackground(otherColor);
		commentArea.setEnabled(true);
		commentArea.setEditable(true);
		this.add(commentLabel);
		this.add(scrollPane);
		
		clearButton = new JButton("Clear");
		clearButton.setFont(new Font("メイリオ", Font.PLAIN, (int)Main.DIAMETER*14));
		clearButton.setBounds((int)Main.DIAMETER*250, (int)Main.DIAMETER*552, (int)Main.DIAMETER*80, (int)Main.DIAMETER*30);
		clearButton.addActionListener(this);
		
		if(!isModification){
			submitButton = new JButton("登録");
		}else{
			submitButton = new JButton("修正");
		}
		submitButton.setFont(new Font("メイリオ", Font.PLAIN, (int)Main.DIAMETER*14));
		submitButton.setBounds((int)Main.DIAMETER*340, (int)Main.DIAMETER*552, (int)Main.DIAMETER*80, (int)Main.DIAMETER*30);
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
			if((int)Main.DIAMETER*(offsetX+x+tag.getWidth()) > (int)Main.DIAMETER*(panelWidth-4)){
				///はみだすので改行
				x = 0;y += (int)Main.DIAMETER*tagHeight;
				///if(y == 2)break;///３行目は無いので追加を諦める
			}
			tag.reloadBounds((int)Main.DIAMETER*(offsetX+x), (int)Main.DIAMETER*(offsetY+y));
			x+=(int)Main.DIAMETER*(tag.getWidth()+tagPeriod);
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
	
	

	@Override
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}



}
