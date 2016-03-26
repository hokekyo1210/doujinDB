package hokekyo1210.dojindb.ui.util;


import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.ImageIcon;


public class IconUtil {

	private static HashMap<String,IconData> icons;
	
	private List<String> iconNameList;
	private String resourceName;

	public IconUtil(String resourceName){
		this.resourceName = resourceName;
		loadNames();
		if(!loadIcon()){
			System.exit(0);
		}
	}

	private void loadNames() {
		iconNameList = new ArrayList<String>();
		iconNameList.add("file.png");
		iconNameList.add("folder.png");
		iconNameList.add("folderopen.png");
		iconNameList.add("add16.png");
		iconNameList.add("add24.png");
		iconNameList.add("edit16.png");
		iconNameList.add("edit24.png");
		iconNameList.add("edit32.png");
	}

	private boolean loadIcon(){
		try{
			icons = new HashMap<String,IconData>();
			for(String name:iconNameList){
//				System.out.println(name);
				final URL filename = this.getClass().getResource("/"+resourceName+"/"+name);
				Image image = Toolkit.getDefaultToolkit().getImage(filename);
				if(image!=null){
					icons.put(name,new IconData(name,new ImageIcon(image)));
				}else{
					return false;
				}
			}
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public static ImageIcon getIcon(String string) {
		return icons.get(string).getIcon();
/*		for(IconData id:icons){
			if(id.getName().equals(string)){
				return id.getIcon();
			}
		}
		return null;
*/
	}

}
