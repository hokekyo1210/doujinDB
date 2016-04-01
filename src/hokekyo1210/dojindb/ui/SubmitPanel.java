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
	private boolean isModification;///�C�����ǂ���
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
	
	public SubmitPanel(int width,int height,RightPanel src,Node node){///�C���p
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
	
	public void submit(){///�f�[�^�x�[�X�ɒǉ�
		if(isModification){///�C���p�Ȃ�O��db���폜
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
		if(imageArea.getSource() != null){///�摜��ʂ̏ꏊ�ɐ���
			image = imageArea.save();
		}
		Node result = SQLManager.addData((String)kindBox.getSelectedItem(), title, circle, artist, date, tagArray, comment, image,thumb);
		if(result != null){
			submitButton.setForeground(Color.GREEN);
		}else{
			submitButton.setForeground(Color.RED);
		}
		beforeNode = result;
		///source.setSubmitPanel();///submit�p�l���������[�h!
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
		imageArea.setToolTipText("�����ɉ摜���h���b�O&�h���b�v");
		imageArea.setTransferHandler(new MyDropFileHandler(imageArea));

		this.add(imageArea);
		
		JLabel titleLabel = new JLabel("�^�C�g��:");
		titleLabel.setFont(new Font("���C���I", Font.PLAIN, (int)Main.DIAMETER*15));
		titleLabel.setBounds((int)Main.DIAMETER*226,8,(int)Main.DIAMETER*100,(int)Main.DIAMETER*20);
		titleField = new JTextField();
		titleField.setBounds((int)Main.DIAMETER*228, (int)Main.DIAMETER*30, (int)Main.DIAMETER*194, (int)Main.DIAMETER*28);
		titleField.setFont(new Font("���C���I", Font.PLAIN, (int)Main.DIAMETER*12));
		titleField.setBackground(otherColor);
		titleField.addActionListener(this);
		this.add(titleLabel);
		this.add(titleField);
		
		JLabel circleLabel = new JLabel("�T�[�N����:");
		circleLabel.setFont(new Font("���C���I", Font.PLAIN, (int)Main.DIAMETER*15));
		circleLabel.setBounds((int)Main.DIAMETER*226,(int)Main.DIAMETER*66,(int)Main.DIAMETER*100,(int)Main.DIAMETER*20);
		circleField = new JTextField();
		circleField.setBounds((int)Main.DIAMETER*228, (int)Main.DIAMETER*88, (int)Main.DIAMETER*194, (int)Main.DIAMETER*28);
		circleField.setFont(new Font("���C���I", Font.PLAIN, (int)Main.DIAMETER*12));
		circleField.setBackground(otherColor);
		this.add(circleLabel);
		this.add(circleField);
		
		JLabel artistLabel = new JLabel("����:");
		artistLabel.setFont(new Font("���C���I", Font.PLAIN, (int)Main.DIAMETER*15));
		artistLabel.setBounds((int)Main.DIAMETER*226,(int)Main.DIAMETER*124,(int)Main.DIAMETER*100,(int)Main.DIAMETER*20);
		artistField = new JTextField();
		artistField.setBounds((int)Main.DIAMETER*228, (int)Main.DIAMETER*148, (int)Main.DIAMETER*194, (int)Main.DIAMETER*28);
		artistField.setFont(new Font("���C���I", Font.PLAIN, (int)Main.DIAMETER*12));
		artistField.setBackground(otherColor);
		this.add(artistLabel);
		this.add(artistField);
		
		JLabel dateLabel = new JLabel("���s��:");
		dateLabel.setFont(new Font("���C���I", Font.PLAIN, (int)Main.DIAMETER*15));
		dateLabel.setBounds((int)Main.DIAMETER*226,(int)Main.DIAMETER*184,(int)Main.DIAMETER*100,(int)Main.DIAMETER*20);
		year = new JFormattedTextField(new DecimalFormat("0000"));
		month = new JFormattedTextField(new DecimalFormat("00"));
		day = new JFormattedTextField(new DecimalFormat("00"));
		JLabel yLabel = new JLabel("�N");
		JLabel mLabel = new JLabel("��");
		JLabel dLabel = new JLabel("��");
		yLabel.setFont(FontManager.getDefaultFont(10));
		mLabel.setFont(FontManager.getDefaultFont(10));
		dLabel.setFont(FontManager.getDefaultFont(10));
		year.setFont(new Font("���C���I", Font.PLAIN, (int)Main.DIAMETER*16));
		month.setFont(new Font("���C���I", Font.PLAIN, (int)Main.DIAMETER*16));
		day.setFont(new Font("���C���I", Font.PLAIN, (int)Main.DIAMETER*16));
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
		
		JLabel kindLabel = new JLabel("���:");
		kindLabel.setFont(new Font("���C���I", Font.PLAIN, (int)Main.DIAMETER*15));
		kindLabel.setBounds((int)Main.DIAMETER*226,(int)Main.DIAMETER*244,(int)Main.DIAMETER*100,(int)Main.DIAMETER*20);
		kindBox = new JComboBox(kinds);
		kindBox.setFont(new Font("���C���I", Font.BOLD, (int)Main.DIAMETER*15));
		kindBox.setBounds((int)Main.DIAMETER*230, (int)Main.DIAMETER*266, (int)Main.DIAMETER*190, (int)Main.DIAMETER*30);
		this.add(kindLabel);
		this.add(kindBox);
		
		JLabel tagLabel = new JLabel("�^�O:");
		tagField = new JTextField();
		tagField.addActionListener(this);
		addButton = new JButton("�ǉ�");
		addButton.addActionListener(this);
		tagLabel.setFont(new Font("���C���I", Font.PLAIN, (int)Main.DIAMETER*18));
		tagField.setFont(new Font("���C���I", Font.PLAIN, (int)Main.DIAMETER*12));
		addButton.setFont(new Font("���C���I", Font.PLAIN, (int)Main.DIAMETER*12));
		tagField.setBounds((int)Main.DIAMETER*4, (int)Main.DIAMETER*310+2, (int)Main.DIAMETER*150, (int)Main.DIAMETER*26);
		addButton.setBounds((int)Main.DIAMETER*154, (int)Main.DIAMETER*310+2, (int)Main.DIAMETER*70, (int)Main.DIAMETER*26);
		tagLabel.setBounds((int)Main.DIAMETER*4, (int)Main.DIAMETER*338, (int)Main.DIAMETER*60, (int)Main.DIAMETER*26);
		tagField.setBackground(otherColor);
		this.add(tagField);
		this.add(addButton);
		this.add(tagLabel);
		
		JLabel commentLabel = new JLabel("�R�����g:");
		commentArea = new JTextArea();
		JScrollPane scrollPane = new JScrollPane(commentArea, 
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		commentArea.setLineWrap(true);
		commentLabel.setFont(new Font("���C���I", Font.PLAIN, (int)Main.DIAMETER*18));
		commentArea.setFont(new Font("���C���I", Font.PLAIN, (int)Main.DIAMETER*14));
		commentLabel.setBounds((int)Main.DIAMETER*4, (int)Main.DIAMETER*400, (int)Main.DIAMETER*100, (int)Main.DIAMETER*26);
		scrollPane.setBounds((int)Main.DIAMETER*4, (int)Main.DIAMETER*428, (int)Main.DIAMETER*420, (int)Main.DIAMETER*120);
		commentArea.setBackground(otherColor);
		commentArea.setEnabled(true);
		commentArea.setEditable(true);
		this.add(commentLabel);
		this.add(scrollPane);
		
		clearButton = new JButton("Clear");
		clearButton.setFont(new Font("���C���I", Font.PLAIN, (int)Main.DIAMETER*14));
		clearButton.setBounds((int)Main.DIAMETER*250, (int)Main.DIAMETER*552, (int)Main.DIAMETER*80, (int)Main.DIAMETER*30);
		clearButton.addActionListener(this);
		
		if(!isModification){
			submitButton = new JButton("�o�^");
		}else{
			submitButton = new JButton("�C��");
		}
		submitButton.setFont(new Font("���C���I", Font.PLAIN, (int)Main.DIAMETER*14));
		submitButton.setBounds((int)Main.DIAMETER*340, (int)Main.DIAMETER*552, (int)Main.DIAMETER*80, (int)Main.DIAMETER*30);
		submitButton.addActionListener(this);
		submitButton.setForeground(Color.BLACK);
		this.add(submitButton);
		this.add(clearButton);
		
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				commentArea.revalidate();///������Ȃ��ƂȂ��������ȓ���
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
				///�݂͂����̂ŉ��s
				x = 0;y += (int)Main.DIAMETER*tagHeight;
				///if(y == 2)break;///�R�s�ڂ͖����̂Œǉ�����߂�
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
			if(target.equalsIgnoreCase(""))return;///�󕶎��Ȃ�_��
			if(target.length()>0){
				addTag(target);
			}
			tagField.setText("");
		}else if(event.getSource().equals(submitButton)){
			if(titleField.getText().equalsIgnoreCase(""))return;
			if(circleField.getText().equalsIgnoreCase(""))return;
			this.submit();
		}else if(event.getSource().equals(clearButton)){
			Object tar = kindBox.getSelectedItem();///�N���A���������ʂ����͕ێ�
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
		}else if(event.getSource().equals(tagField)){///�^�O���͂��Ă�Ƃ��ɃG���^�[��������ǉ�
			addButton.doClick();
		}else if(event.getSource().equals(titleField)){
			String word = titleField.getText();
			if(word.equalsIgnoreCase(""))return;///����ۂȂ瓮�����Ȃ�
			
			if(workingThread != null && !workingThread.isFinished()){
				///������
				return;
			}
			titleField.setEnabled(false);///�^�C�g���t�B�[���h���~�߂�
			workingThread = new Crawler(word,this);
			workingThread.start();///���������s����
		}
	}
	
	public void showPopup(List<SearchResult> results){
		SearchPopup popup = new SearchPopup(results,this);
		popup.show(this, titleField.getX(), titleField.getY()+28);
		titleField.setEnabled(true);///�߂�
	}
	public void setResult(SearchResult result){///�x������S���񓯊��œK�p
		targetResult = result;
		submitButton.setEnabled(false);///���s����������܂œo�^�{�^���̓I�t
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
		removeAllTag();///�^�O��S���O��
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
