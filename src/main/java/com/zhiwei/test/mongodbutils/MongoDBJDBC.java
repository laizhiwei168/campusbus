package com.zhiwei.test.mongodbutils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class MongoDBJDBC{
   public static void main( String args[] ){
	   final String IP_ADDRESS = "106.14.121.203"; // 本机地址
	   final String DB_NAME = "worldbounds"; // 数据库名称
	     ServerAddress serverAddress = new ServerAddress(IP_ADDRESS, 27017);
	   List<ServerAddress> addrs = new ArrayList<ServerAddress>();
	   addrs.add(serverAddress);

	   // MongoCredential.createScramSha1Credential()三个参数分别为 用户名 数据库名称 密码
	   MongoCredential credential = MongoCredential.createScramSha1Credential("mongoRoot", DB_NAME,
	     "MongoDB#2017".toCharArray());
	   List<MongoCredential> credentials = new ArrayList<MongoCredential>();
	   credentials.add(credential);

	   // 通过连接认证获取MongoDB连接
	   MongoClient client = new MongoClient(addrs, credentials);
	   MongoDatabase db = client.getDatabase(DB_NAME);
	   
	   
	   double[] db2={113,22};
	   BasicDBObject query3 = new BasicDBObject(); 
	   Map<String, Object> queryMap = new HashMap<>();  
	    queryMap.put("coordinates", db2);  
	    queryMap.put("type", "Point");  
	    //模糊匹配rule查询  
	    query3.put("polygons", new BasicDBObject("$geoIntersects", new BasicDBObject("$geometry",new BasicDBObject(queryMap))));  
	   
	  // MongoCollection<Document> collection = db.getCollection("worldbounds");
	   MongoCollection<Document> collection = db.getCollection("geo");
	   FindIterable<Document> docs=collection.find(query3);
	   
	   docs.forEach(new Block<Document>() {
		@Override
		public void apply(Document arg0) {
			// TODO Auto-generated method stub
			System.out.println(arg0.toJson());
		}
		 });
	   
   }
}