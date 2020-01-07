package com.zhiwei.campusbus.utils;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.alibaba.fastjson.JSONObject;

public class HttpClientExample {

    private final static String USER_AGENT = "Mozilla/5.0";

    public static void main(String[] args) throws Exception {

       // HttpClientExample http = new HttpClientExample();

        //System.out.println("Testing 1 - Send Http GET request");
        //String utl="http://127.0.0.1:8080/cimcbox/yard/user/addUser?username=cimc&password=123";
        //http.sendGet(utl);

        System.out.println("\nTesting 2 - Send Http POST request");
        Map<String, String> map=new HashMap<String, String>();
        map.put("device_id", "5f6830c2-47bf-495f-b40f-7848e097f591");
        map.put("latitude", "138.1101");
        map.put("longitude", "28.0121");
        try{
        Object  result= HttpClientExample.sendPost(map);
        System.out.println("********"+result.toString()+"**************");
        Map<String, Object> data = new HashMap<String, Object>();
        data = JsonUtil.jsonString2SimpleObj(result.toString(), data.getClass());
        System.out.println(data.get("ret"));
        }
        catch (Exception e) {
        	System.out.println("Exception:"+e.getMessage());
        }

    }

    // HTTP GET请求
    public static void sendGet(String s_url) throws Exception {

        String url = s_url;//"http://127.0.0.1:8080/cimcbox/yard/user/addUser?username=cimc&password=123";

        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);

        //添加请求头
        request.addHeader("User-Agent", USER_AGENT);

        HttpResponse response = client.execute(request);

        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " +
                       response.getStatusLine().getStatusCode());

        BufferedReader rd = new BufferedReader(
                       new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }

        System.out.println(result.toString());

    }
    /**
     * 传输为post，传输头是map格式
     * @param map
     * @return
     * @throws Exception
     */
    // HTTP POST请求
    public static Object sendPost(Map<String,String> map) throws Exception {

        String url = Constants.HOST_PUSDATATOLBS;

        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);

        //添加请求头
        post.setHeader("User-Agent", USER_AGENT);

        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        Iterator<Map.Entry<String, String>> it =  map.entrySet().iterator();
	       while (it.hasNext()) {
	            Map.Entry<String, String> entry = it.next(); 
	          urlParameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
	      }       
        post.setEntity(new UrlEncodedFormEntity(urlParameters));

        HttpResponse response = client.execute(post);
        /*System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + post.getEntity());
        System.out.println("Response Code : " +
                                    response.getStatusLine().getStatusCode());*/

        BufferedReader rd = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        
        return result;

    }
    
    /**
     * 处理post请求数据，传输参数为body是json格式
     * 
     * map承载要传输的json对象
     */
    public static Object sendPostToJson(Map<String,String> map){
    	try {
            //创建连接
    		// String url = Constants.HOST_PUSDATATOLBS;
            URL url = new URL(Constants.HOST_PUSDATATOLBS);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");

            connection.connect();

            //POST请求
            DataOutputStream out = new DataOutputStream(
                    connection.getOutputStream());
            JSONObject obj = new JSONObject();
            Iterator<Map.Entry<String, String>> it =  map.entrySet().iterator();
 	       while (it.hasNext()) {
 	            Map.Entry<String, String> entry = it.next(); 
 	           obj.put(entry.getKey(),  entry.getValue());
 	      }             

            out.writeBytes(obj.toString());
            out.flush();
            out.close();

            //读取响应
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String lines;
            StringBuffer sb = new StringBuffer("");
            while ((lines = reader.readLine()) != null) {
                lines = new String(lines.getBytes(), "utf-8");
                sb.append(lines);
            }
            
            reader.close();
            // 断开连接
            connection.disconnect();
            return sb;
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    	
       
    }

}

