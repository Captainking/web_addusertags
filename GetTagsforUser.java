//package temp;

import java.util.ArrayList;
import java.util.List;
import com.hankcs.hanlp.HanLP;
import redis.clients.jedis.Jedis;

public class GetTagsforUser {

	public void getTags() {

		UserDAO userDao = new UserDaoImpl();
		List<String> useridList = userDao.getUserId();
		List<String> weiboidList = new ArrayList<String>();
		for (String userid : useridList) {
			weiboidList.addAll(userDao.getWeibo(userid, 1, Integer.parseInt(userDao.getWeiboNumber(userid))));
			String content = "";
			for (String str : weiboidList) {
				content = content + str;
			}
			List<String> tags = HanLP.extractKeyword(content, 5);
			settags(tags,userid );
		}
	}
	// ����д��һ��д�����ݿ��еĲ���
	public void settags(List<String>list,String userid){
		Jedis jedis = new Jedis("localhost");
		System.out.println("Connection to server sucessfully");
		// �鿴�����Ƿ�����
		System.out.println("Server is running: " + jedis.ping());
		for(String string:list){
			jedis.del(userid+"tags");
			jedis.rpush(userid+"tags", string);
		}
	}
}
