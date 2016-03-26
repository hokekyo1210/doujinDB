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
		for(Root r:tables){
			r.clearCirlces();///まずテーブル内のサークルをクリアする
			String tableName = r.getName();
			System.out.println("----------"+tableName+"----------");
			String selquery = "SELECT * FROM "+tableName;
			List<Node> stock = new ArrayList<Node>();///ソート用に溜める
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
					List<String> tags = Arrays.asList(tag.split("_"));
					System.out.println(title+" "+circle+" "+artist+" "+date+" "+tag+"("+tags.size()+") "+comment+" "+image+" "+thumb);
					Node node = new Node(title,circle,artist,date,tags,comment,image,thumb);
					stock.add(node);
				}
				Collections.sort(stock,new NodeComp());///データのソートを実行
				for(Node n:stock){
					Circle c = r.getCircle(n.circle);
					if(c == null){///サークル未登録なら追加
						c = new Circle(n.circle);
						r.addCircle(c);
					}
					c.addNode(n);
				}
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
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
			DBPanel.treeRefresh(target);///ツリーに変更を伝える
			break;
		}
	}
	
	public static boolean addData(String table,String title,String circle,String artist,String date,List<String> tags,String comment,String image,String thumb){
		///INSERT INTO user VALUES(title,circle,artist,date,tag,comment,dir)
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
			addNode(table,new Node(title,circle,artist,date,tags,comment,image,thumb));///クエリ飛ばしたら木への追加も行う
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
