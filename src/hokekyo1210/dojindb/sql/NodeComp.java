package hokekyo1210.dojindb.sql;

import java.util.Comparator;
import java.util.Date;

public class NodeComp implements Comparator<Node>{
	
	private static final Date none = new Date(0L);
	
	@Override
	public int compare(Node n0, Node n1) {
		int ret = n0.circle.compareTo(n1.circle);///�܂��T�[�N���Ŕ�r
		if(ret == 0){///�T�[�N���������ꍇ�͓��t�Ŕ�r
			Date d0 = n0.exDate;
			Date d1 = n1.exDate;
			if(d0 == null)d0 = none;
			if(d1 == null)d1 = none;
			ret = d1.compareTo(d0);
			if(ret == 0){///���t�������ꍇ�̓^�C�g��
				ret = n0.title.compareTo(n1.title);
			}
		}
		return ret;
	}

}
