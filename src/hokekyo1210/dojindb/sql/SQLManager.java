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
	
	private static final String DIR = System.getProperty("user.home")+"/temp";///DBを保存するディレクトリ
	
	private static Connection connection;
	private static List<Root> tables = new ArrayList<Root>();///全データベースの木構造
	
	public static List<Root> getTables(){
		return tables;
	}
	
	public static void launchSQLManager() throws Exception{
		if(!(new File(DIR).exists())){
			new File(DIR).mkdirs();
		}
		Class.forName("org.sqlite.JDBC");
		connection = DriverManager.getConnection("jdbc:sqlite:"+DIR+"/doujin.db");
		
		createTable("同人誌");///この２つをテンプレとして作る
		createTable("音楽");
		
		if(!tableReload() || !databaseAllReload()){///テーブルをロード、失敗したら落とす
			System.exit(0);
		}
		
	}
	
	public static boolean tableReload(){///テーブルをリロード
		tables.clear();
		ResultSet ret;
		try {
			ret = query("SELECT * FROM sqlite_master");///データベースに存在するテーブルを全て取得
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
	
	public static boolean databaseAllReload(){///全データをリロード,木を再構築
		long start = System.currentTimeMillis();
		List<Node> stock = new ArrayList<Node>();///ミニ画像ロード用に溜める
		for(Root r:tables){
			r.clearCirlces();///まずテーブル内のサークルをクリアする
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
		Thread th = new Thread(new MiniImageLoader(stock));///画像を非同期でロードさせる
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
	
	public static void addNewTable(String tableName){///新しいテーブルを追加
		for(Root r : tables){
			if(r.getName().equals(tableName))return;///同じ名前のテーブルあるならダメ
		}
		createTable(tableName);
		Root newTable = new Root(tableName);
		tables.add(newTable);
		DBPanel.addNewTable(newTable);
	}
	
	public static void removeNode(Node node){///DBからNodeを消す
		///DELETE FROM user WHERE title = '名前' AND circle = 'サークル'
		String getQ = "SELECT * FROM '"+node.table+"' WHERE title = '"+node.title+"' AND circle = '"+node.circle+"'";
		String query = "DELETE FROM '"+node.table+"' WHERE title = '"+node.title+"' AND circle = '"+node.circle+"'";
		try {
			ResultSet ret = query(getQ);
			String dir = ret.getString("image");
			if(!dir.equals("None")){///画像を削除する
				File f = new File(dir);
				f.delete();
			}
			query2(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void removeTable(Root root){///テーブル削除、テーブル内のノードが全部先に消えてないとダメだよ！
		///DROP TABLE '同人誌'
		String query = "DROP TABLE '"+root.getName()+"'";
		try {
			query2(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void addNode(String table,Node node){///木にノードを追加O(RlogC) R:テーブルの数
		for(Root r:tables){
			if(!r.getName().equals(table))continue;
			Circle c = r.getCircle(node.circle);
			
			TreeNode target;///リフレッシュするノード
			
			if(c == null){///サークル新規登録
				c = new Circle(node.circle);
				r.addCircle(c);
				target = r.getTreeNode();
			}else target = c.getTreeNode();
			c.addNode(node);
//			DBPanel.treeRefresh(target);///ツリーに変更を伝える
			DBPanel.treeRefresh();
			break;
		}
	}
	
	public static boolean isExist(String table,String title,String circle){///データ重複判定
		///SELECT * FROM 'テーブル' WHERE title = '名前' AND circle = 'サークル'
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
		if(isExist(table,title,circle)){///データが重複してたらダメよ
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
		addNode(table,newNode);///クエリ飛ばしたら木への追加も行う
		return newNode;
	}

}
