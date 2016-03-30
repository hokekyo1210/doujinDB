package hokekyo1210.dojindb.sql;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.swing.tree.TreeNode;

import hokekyo1210.dojindb.ui.DBPanel;

public class SQLManager {
	
	private static final String DIR = System.getProperty("user.home")+"/temp";///DB��ۑ�����f�B���N�g��
	
	private static Connection connection;
	private static List<Root> tables = new ArrayList<Root>();///�S�f�[�^�x�[�X�̖؍\��
	
	public static List<Root> getTables(){
		return tables;
	}
	
	public static void launchSQLManager() throws Exception{
		if(!(new File(DIR).exists())){
			new File(DIR).mkdirs();
		}
		Class.forName("org.sqlite.JDBC");
		connection = DriverManager.getConnection("jdbc:sqlite:"+DIR+"/doujin.db");
		
		createTable("���l��");///���̂Q���e���v���Ƃ��č��
		createTable("���y");
		
		if(!tableReload() || !databaseAllReload()){///�e�[�u�������[�h�A���s�����痎�Ƃ�
			System.exit(0);
		}
		
	}
	
	public static boolean tableReload(){///�e�[�u���������[�h
		tables.clear();
		ResultSet ret;
		try {
			ret = query("SELECT * FROM sqlite_master");///�f�[�^�x�[�X�ɑ��݂���e�[�u����S�Ď擾
			while(ret.next()){
				tables.add(new Root(ret.getString(2)));
			}
			ret.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean databaseAllReload(){///�S�f�[�^�������[�h,�؂��č\�z
		long start = System.currentTimeMillis();
		List<Node> stock = new ArrayList<Node>();///�~�j�摜���[�h�p�ɗ��߂�
		for(Root r:tables){
			r.clearCirlces();///�܂��e�[�u�����̃T�[�N�����N���A����
			String tableName = r.getName();
			System.out.println("----------"+tableName+"----------");
			String selquery = "SELECT * FROM "+tableName;
			
			try {
				ResultSet ret = query(selquery);
				while(ret.next()){
					String title = ret.getString("title");
					String circle = ret.getString("circle");
					String artist = ret.getString("artist");
					String date = ret.getString("date");
					String tag = ret.getString("tag");
					String comment = ret.getString("comment");
					String image = ret.getString("image");
					String thumb = ret.getString("thumb");
					List<String> tags = new ArrayList<String>();
					if(!tag.equals("None"))tags = Arrays.asList(tag.split("_"));
					System.out.println(title+" "+circle+" "+artist+" "+date+" "+tag+"("+tags.size()+") "+comment+" "+image+" "+thumb);
					Node node = new Node(title,circle,artist,date,tags,comment,image,thumb,r.getName(),false);
					stock.add(node);
					Circle c = r.getCircle(circle);
					if(c == null){
						c = new Circle(circle);
						r.addCircle(c);
					}
					c.addNode(node);
				}
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		Thread th = new Thread(new MiniImageLoader(stock));///�摜��񓯊��Ń��[�h������
		th.start();
		System.out.println("Load success.["+(System.currentTimeMillis()-start)+"ms]");
		
		return true;
	}
	
	public static ResultSet query(String query) throws Exception{
		Statement stmt = connection.createStatement();
		return stmt.executeQuery(query);
	}
	
	public static boolean query2(String query) throws Exception{
		Statement stmt = connection.createStatement();
		return stmt.execute(query);
	}
	
	public static void createTable(String tableName){
		try{
			query2("CREATE TABLE "+tableName+"(title text,circle text,artist text,date text,tag text,comment text,image text,thumb text)");
		}catch(Exception e){System.out.println(tableName+" Already.");}
	}
	
	public static void addNewTable(String tableName){///�V�����e�[�u����ǉ�
		for(Root r : tables){
			if(r.getName().equals(tableName))return;///�������O�̃e�[�u������Ȃ�_��
		}
		createTable(tableName);
		Root newTable = new Root(tableName);
		tables.add(newTable);
		DBPanel.addNewTable(newTable);
	}
	
	public static void removeNode(Node node){///DB����Node������
		///DELETE FROM user WHERE title = '���O' AND circle = '�T�[�N��'
		String getQ = "SELECT * FROM '"+node.table+"' WHERE title = '"+node.title+"' AND circle = '"+node.circle+"'";
		String query = "DELETE FROM '"+node.table+"' WHERE title = '"+node.title+"' AND circle = '"+node.circle+"'";
		try {
			ResultSet ret = query(getQ);
			String dir = ret.getString("image");
			if(!dir.equals("None")){///�摜���폜����
				File f = new File(dir);
				f.delete();
			}
			query2(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void removeTable(Root root){///�e�[�u���폜�A�e�[�u�����̃m�[�h���S����ɏ����ĂȂ��ƃ_������I
		///DROP TABLE '���l��'
		String query = "DROP TABLE '"+root.getName()+"'";
		try {
			query2(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void addNode(String table,Node node){///�؂Ƀm�[�h��ǉ�O(RlogC) R:�e�[�u���̐�
		for(Root r:tables){
			if(!r.getName().equals(table))continue;
			Circle c = r.getCircle(node.circle);
			
			TreeNode target;///���t���b�V������m�[�h
			
			if(c == null){///�T�[�N���V�K�o�^
				c = new Circle(node.circle);
				r.addCircle(c);
				target = r.getTreeNode();
			}else target = c.getTreeNode();
			c.addNode(node);
//			DBPanel.treeRefresh(target);///�c���[�ɕύX��`����
			DBPanel.treeRefresh();
			break;
		}
	}
	
	public static boolean isExist(String table,String title,String circle){///�f�[�^�d������
		///SELECT * FROM '�e�[�u��' WHERE title = '���O' AND circle = '�T�[�N��'
		String query = "SELECT * FROM '"+table+"' WHERE title = '"+title+"' AND circle = '"+circle+"'";
		try{
			ResultSet ret = query(query);
			int i = 0;
			while(ret.next()){
				i++;
			}
			if(i >= 1)return true;
		}catch(Exception e){
			e.printStackTrace();
			return true;
		}
		return false;
	}
	
	public static Node addData(String table,String title,String circle,String artist,String date,List<String> tags,String comment,String image,String thumb){
		///INSERT INTO user VALUES(title,circle,artist,date,tag,comment,dir)
		if(isExist(table,title,circle)){///�f�[�^���d�����Ă���_����
			return null;
		}
		String tag = "None";
		if(tags.size() != 0){
			tag = "";
			for(int i = 0;i < tags.size();i++){
				if(i != 0)tag+="_";
				tag+=tags.get(i);
			}
		}
		String query = "INSERT INTO "+table+" VALUES('"+title+"','"+circle+"','"+artist+"','"+date+"','"+tag+"','"+comment+"','"+image+"','"+thumb+"')";
		try {
			query2(query);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		Node newNode = new Node(title,circle,artist,date,tags,comment,image,thumb,table,true);
		addNode(table,newNode);///�N�G����΂�����؂ւ̒ǉ����s��
		return newNode;
	}

}
