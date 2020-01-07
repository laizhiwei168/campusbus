package com.zhiwei.test.codec;

import java.text.DecimalFormat;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author lzw
 *
 */
public class ReportProcess {
	private String identifier="";
	
	private String msgType = "deviceReq";
    private int hasMore = 0;
    private int errcode = 0;
    private byte bDeviceReq = 0x00;
    private byte bDeviceRsp = 0x01;
    
    //设备ID
    private String deviceId="";
    
    //消息ID
    private String messageID="";
    private String locationmessageID="8005";
   
    //内容长度
    private int  contentionLength=0;
    
    //校验码
    private String  verify="";
    private boolean isVerify=false;
    
    //状态信息
    private String statusInformation="";
   
    //eventTime时间
    private String eventTime="";

    //serviceId=Temperature字段
    private String temperature ="";
    
    //serviceId=Humidity字段
    private String humidity="";
    
    //serviceId=Longitude字段
    private String longitude="";
    
    //serviceId=Latitude字段
    private String latitude="";
    
    
    //serviceId=batteryVoltage字段
    private String batteryVoltage="";
    
    //serviceId=signalStrength字段
    private String signalStrength="";
    
    //serviceId=Accelerate字段
    private String accelerate="";
    private String acceleratetox="";
    private String acceleratetoy="";
    private String acceleratetoz="";
    
    

    private byte noMid = 0x00;
    private byte hasMid = 0x01;
    private boolean isContainMid = false;
    private int mid = 0;
	
    DecimalFormat df = new DecimalFormat("0.000000");
    DecimalFormat df1 = new DecimalFormat("0.0");
    StringBuffer strbf;
    private Utilty util;
    
    /**
     * @param binaryData 设备发送给平台cimc报文的payload部分
     *                   ed7e  0603  05  0100  8005  6945636f32303137303631327878  000f  0026
     *                           00000000 状态信息              4
     *                           01020304 经度值/MNC  4
     *                           02030405 纬度值/LCA  4
     *                           00000000  CID      4
     *                           0000 速度                                   2
     *                           0000 程高                                   2
     *                           0000 方向                                   2
     *                           170706140132报文时间      6
     *                           01  02  00DC温度             4
     *                           02  01  AF 湿度                3
     *                           ca41 校验码 
     *                           
     *                   byte[0]--byte[1]:  AA 72  命令头
     *                   byte[2]--byte[3]:  06 03   厂商                                                 
     *                   byte[4] ：     02                               hasMore  0表示没有后续数据，1表示有后续数据，不带按照0处理
     *                   byte[5]--byte[6]:  00 01   版本号
     *                   byte[7]--byte[8]:  80 06   消息ID   mstType 80 06 表示设备上报数据deviceReq
     *                   byte[9]--byte[22]: 00 00 00 00 00 00 00 00 00 00 00 00 00 00   设备ID
     *                   byte[23]--byte[24]:  00 1E   消息序号
     *                   byte[25]--byte[26]:  00 26   内容长度
     *                   
     *                   
     *                   
     *                   最后的两个byte是校验码 ：ca 41
     *                   
     *                   byte[4]--byte[11]:服务数据，根据需要解析//如果是deviceRsp,byte[4]表示是否携带mid, byte[5]--byte[6]表示短命令Id
     * @return
     * 
     */
    
	public ReportProcess(byte[] binaryData) {
		  
		identifier=Utilty.toStringByte(binaryData,0,1);
        //判断消息ID
		if (locationmessageID.equals(Utilty.toStringByte(binaryData,7,8))) {
            //msgType = "deviceReq";
            messageID="8005";
            
            deviceId=Utilty.toStringByte(binaryData,9,22);
            
            verify=""+((binaryData[binaryData.length-2]<< 8 & 0XFFFF)+ (binaryData[binaryData.length-1] & 0xFF));
            
            contentionLength=(binaryData[25]<< 8 & 0XFFFF)+ (binaryData[26] & 0xFF);
           
            //校验码认证判断
            if(verify.equals(""+Utilty.CRC16IBM(binaryData,binaryData.length-2))) {
            	isVerify=true;
            }else {
            	return;
            }
            
            
            
            //内容长度认证判断
            if(contentionLength==(binaryData.length-2-27)) {
            	
            	util=new Utilty();
            	statusInformation=Integer.toBinaryString(((binaryData[27] & 0xFF) << 24 ) + ((binaryData[28] & 0xFF) << 16)+((binaryData[29]& 0xFF) << 8 )+(binaryData[30]& 0xFF));
            	strbf=new StringBuffer();
            	if(statusInformation.length()<32) {//解析状态信息
            		for(int i=0;i<32-statusInformation.length();i++) {
            			strbf.append("0");
            		}
            		strbf.append(statusInformation);
            	}else {
            		strbf.append(statusInformation);
            	}
            	boolean is_location=true;
            	int bf_length=strbf.length();
            	if(strbf.substring(bf_length-1, bf_length).equals("1")) {
            		//判断定位成功
            		if(strbf.substring(bf_length-3, bf_length-1).equals("00")) {
            			
            			//处理北斗定位
            			//System.out.println("处理北斗定位");

                        //serviceId=Longitude字段
                    	long Longlongitude=((binaryData[31] << 24 & 0xFFFFFFFFL) + (binaryData[32] << 16 & 0xFFFFFFL)+(binaryData[33] << 8& 0xFFFFL)+(binaryData[34]& 0xFF));
                    	if(!(Longlongitude+"").equals("4294967295")) {//判断是否为有效值
                    		double longitudeVale=(double) (Longlongitude)/1000000;
                        	if(strbf.substring(bf_length-5, bf_length-4).equals("1")) {//判断东西经度值  0为东经 值为整数
                        		longitudeVale=longitudeVale*-1;
                        	}
                    	    longitude=df.format(longitudeVale);
                    	}else {
                    		longitude="FFFFFFFF";
                    	}
                    	
                	    
                        //serviceId=Latitude字段
                    	long Longlatitude=(binaryData[35] << 24 & 0xFFFFFFFFL) + (binaryData[36] << 16 & 0xFFFFFFL)+(binaryData[37] << 8& 0xFFFFL)+(binaryData[38]& 0xFF);
                    	if(!(Longlatitude+"").equals("4294967295")) {//判断是否为有效值
                    	double latitudeVale=(double) (Longlatitude)/1000000;
                    	if(strbf.substring(bf_length-4, bf_length-3).equals("0")) {//判断南北纬度值   1为北纬度 值为整数
                    		latitudeVale=latitudeVale*-1;
                    	}
                    	latitude=df.format(latitudeVale);
                    	}else {                    		
                    		latitude="FFFFFFFF";
                    	}
                    	
            		}else if(strbf.substring(bf_length-3, bf_length-1).equals("01")) {
            			//处理基站定位
            			
            			int MNC=(((binaryData[31]& 0xFF) << 8 ) + (binaryData[32] & 0xFF));
            			int MCC=(((binaryData[33]& 0xFF) << 8 ) + (binaryData[34] & 0xFF));
            			
            			String LCA=Utilty.toStringByte(binaryData, 35, 38);
            			String CID=Utilty.toStringByte(binaryData, 39, 42);
            		    //System.out.println("MNC:"+MNC+"   MCC:"+MCC+"  LCA:"+LCA+"  CID:"+CID);
            			MNC=11;
            			MCC=460;
            			String LCA1="79b7";
            			String CID1="795bc52";
            			try {
            				Map<String, Object> resultMap=  util.sendGetToio("http://121.35.242.253/GSM_location.ashx?mcc="+MCC+"&mnc="+MNC+"&cell_id="+CID1+"&lac="+LCA1);
            				
            				if(resultMap.size()>0) {
            					longitude= resultMap.get("r_longitude").toString();
            					latitude= resultMap.get("r_latitude").toString();
            				}else {
            					is_location=false;
            				}							
						} catch (Exception e) {
							// TODO Auto-generated catch block
							is_location=false;
							e.printStackTrace();
							
						}
            			
            		}else {
            			//没有定位成功！
            			//System.out.println("没有不知道的处理定位");
            			is_location=false;
            		}
            		
            	}else {
            		//没有定位成功！
            		//System.out.println("没有定位成功");
            		is_location=false;
            	}
            	
            	if(is_location==false) {
            		longitude="FFFFFFFF";
            		latitude="FFFFFFFF";
            	}
            	
                //处理时间
                eventTime=Utilty.toDateTime(Utilty.toStringByte(binaryData,49,54));
                
                //serviceId=Temperature 数据解析     
                if(!"65535".equals(((binaryData[57] << 8 & 0xFFFF) + (binaryData[58] & 0xFF))+"")) {
                	 //temperature =((double)(((binaryData[57] << 8 & 0xFFFF) + (binaryData[58] & 0xFF)) * 0.1f)-40)+""; /*(double) (((binaryData[5] << 8) + (binaryData[6] & 0xFF)) * 0.1f)-40;*/
                	 temperature = df1.format(((double)(((binaryData[57] << 8 & 0xFFFF) + (binaryData[58] & 0xFF)) * 0.1f)-40));
                }else {
                	temperature="FFFF";
                }
               
                
                //serviceId=Humidity字段
                if(!"65535".equals(((binaryData[61] << 8 & 0xFFFF) + (binaryData[62] & 0xFF))+"")) {
                humidity=df1.format((double)(((binaryData[61] << 8 & 0xFFFF) + (binaryData[62] & 0xFF)) * 0.1f));
                }else {
                	humidity="FFFF";
                }
                
                //serviceId=batteryVoltage字段
                if(!"65535".equals(((binaryData[65] << 8 & 0xFFFF) + (binaryData[66] & 0xFF))+"")) {
                batteryVoltage=df1.format((double)(((binaryData[65] << 8 & 0xFFFF) + (binaryData[66] & 0xFF)) * 0.1f));
                }else {
                	batteryVoltage="FFFF";
                }
                
               
                //serviceId=signalStrength字段
                if(!"255".equals((binaryData[69]& 0xFF)+"")) {
                signalStrength=(binaryData[69] & 0xFF)+"";
                }else {
                	signalStrength="FF";
                }
                
               
              //serviceId=accelerate字段
                if(!"65535".equals((((binaryData[72]& 0xFF) << 8 ) + (binaryData[73] & 0xFF))+"")) {
                	acceleratetox=df1.format((double)((((binaryData[72]& 0xFF) << 8 ) + (binaryData[73] & 0xFF)) * 0.1f));
                }else {
                	acceleratetox="FFFF";
                }
                
              //serviceId=accelerate字段
                if(!"65535".equals((((binaryData[74] & 0xFF) << 8 ) + (binaryData[75] & 0xFF))+"")) {
                	acceleratetoy=df1.format((double)((((binaryData[74]& 0xFF) << 8 ) + (binaryData[75] & 0xFF)) * 0.1f));
                }else {
                	acceleratetoy="FFFF";
                }
                
               
              //serviceId=accelerate字段
                if(!"65535".equals((((binaryData[76] & 0xFF)<< 8 ) + (binaryData[77] & 0xFF))+"")) {
                	acceleratetoz=df1.format((double)((((binaryData[76]& 0xFF) << 8 ) + (binaryData[77] & 0xFF)) * 0.1f));
                }else {
                	acceleratetoz="FFFF";
                }                
            }else {
            	return;
            }
           
        }
		
		 /*
        如果是设备对平台命令的应答，返回格式为：
       {
            "identifier":"123",
            "msgType":"deviceRsp",
            "errcode":0,
            "body" :{****} 特别注意该body体为一层json结构。
        }
	    */
        else if (binaryData[2] == bDeviceRsp) {   /* 这里需要根据CIMC通讯协议文档来*/
            msgType = "deviceRsp";
            errcode = binaryData[3];
            //此处需要考虑兼容性，如果没有传mId，则不对其进行解码
            if (binaryData[4] == hasMid) {
                mid = Utilty.getInstance().bytes2Int(binaryData, 5, 2);
                if (Utilty.getInstance().isValidofMid(mid)) {
                    isContainMid = true;
                }

            }
        } else {
        	
            return;
        }
		
		
	}
	
/*~~~~~ SmartContainerTracker_cimc itech_NBIoTDevice~~~~~*/
    
    public ObjectNode toJsonNode() {
    	
        try {
            //组装body体
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode root = mapper.createObjectNode();

            root.put("identifier", this.identifier);
           // root.put("msgType", this.msgType);
            root.put("messageId", this.messageID);
            root.put("deviceId", this.deviceId);
            root.put("eventTime", this.eventTime);
            //根据msgType字段组装消息体
            if (this.msgType.equals("deviceReq")) {
            	
            	//判断校验码处理方式
            	if(!isVerify) {
            		return null;
            	}
            	
                root.put("hasMore", this.hasMore);
                ArrayNode arrynode = mapper.createArrayNode();

                
                //serviceId=Longitude 数据组装
                ObjectNode longitudeNode = mapper.createObjectNode();
                longitudeNode.put("serviceId", "Longitude");
                ObjectNode longitudeData = mapper.createObjectNode();
                longitudeData.put("longitude", this.longitude);
                longitudeNode.put("serviceData", longitudeData);
                arrynode.add(longitudeNode);
                
                //serviceId=Latitude 数据组装
                ObjectNode latitudeNode = mapper.createObjectNode();
                latitudeNode.put("serviceId", "Latitude");
                ObjectNode latitudeData = mapper.createObjectNode();
                latitudeData.put("latitude", this.latitude);
                latitudeNode.put("serviceData", latitudeData);
                arrynode.add(latitudeNode);
                
                //serviceId=Temperature 数据组装
                ObjectNode temperatureNode = mapper.createObjectNode();
                temperatureNode.put("serviceId", "Temperature");
                ObjectNode temperatureData = mapper.createObjectNode();
                temperatureData.put("temperature", this.temperature);
                temperatureNode.put("serviceData", temperatureData);
                arrynode.add(temperatureNode);
                
                
                //serviceId=Humidity 数据组装
                ObjectNode humidityNode = mapper.createObjectNode();
                humidityNode.put("serviceId", "Humidity");
                ObjectNode humidityData = mapper.createObjectNode();
                humidityData.put("humidity", this.humidity);
                humidityNode.put("serviceData", humidityData);
                arrynode.add(humidityNode);
               
                
                //serviceId=battery 数据组装
                ObjectNode batteryNode = mapper.createObjectNode();
                batteryNode.put("serviceId", "Battery");
                ObjectNode batteryData = mapper.createObjectNode();
                batteryData.put("batteryVoltage", this.batteryVoltage);
                batteryNode.put("serviceData", batteryData);
                arrynode.add(batteryNode);

                //serviceId=Connectivity 数据组装
                ObjectNode ConnectivityNode = mapper.createObjectNode();
                ConnectivityNode.put("serviceId", "Connectivity");
                ObjectNode  ConnectivityData = mapper.createObjectNode();
                ConnectivityData.put("signalStrength", this.signalStrength);
                ConnectivityNode.put("serviceData", ConnectivityData);
                arrynode.add(ConnectivityNode);
                
                //serviceId=Connectivity 数据组装
                ObjectNode AccelerateNode = mapper.createObjectNode();
                AccelerateNode.put("serviceId", "Accelerate");
                ObjectNode  AccelerateData = mapper.createObjectNode();
                /*AccelerateData.put("accelerate", this.accelerate);*/
                AccelerateData.put("acceleratetox", this.acceleratetox);
                AccelerateData.put("acceleratetoy", this.acceleratetoy);
                AccelerateData.put("acceleratetoz", this.acceleratetoz);
                AccelerateNode.put("serviceData", AccelerateData);
                arrynode.add(AccelerateNode);
               

                root.put("data", arrynode);

            } else {
                root.put("errcode", this.errcode);
                //此处需要考虑兼容性，如果没有传mid，则不对其进行解码
                if (isContainMid) {
                    //root.put("mid", this.mid);//mid
                }
                //组装body体，只能为ObjectNode对象
                ObjectNode body = mapper.createObjectNode();
                body.put("result", 0);
                root.put("body", body);
            }
            return root;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
}
