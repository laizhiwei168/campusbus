package com.zhiwei.test.codec;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class CmdProcess {

    //private String identifier = "123";
    private String msgType = "deviceReq";
    private String serviceId = "Brightness";
    private String cmd = "SET_DEVICE_LEVEL";
    private int hasMore = 0;
    private int errcode = 0;
    private int mid = 0;
    private JsonNode paras;
    
    private byte[] requestDate;
    private String str="";
    
    private Utilty util=null;
    private String verify="";
    
    private int contentionLength= 0;


    public CmdProcess() {
    }

    public CmdProcess(ObjectNode input) {

        try {
            // this.identifier = input.get("identifier").asText();

            this.msgType = input.get("msgType").asText();
           
           // System.out.println("by:"+Utilty.toStringByte(requestDate,0,requestDate.length-1));
            /*
            平台收到设备上报消息，编码ACK
            {
                "identifier":"0",
                "msgType":"cloudRsp",
                "request": ***,//设备上报的码流
                "errcode":0,
                "hasMore":0
            }
            * */
            if (msgType.equals("cloudRsp")) {
            	 this.requestDate =input.get("request").binaryValue();
                //在此组装ACK的值
                this.errcode = input.get("errcode").asInt();
                this.hasMore = input.get("hasMore").asInt();
            } else {
            /*
            平台下发命令到设备，输入
            {
                "identifier":0,
                "msgType":"cloudReq",
                "serviceId":"WaterMeter",
                "cmd":"SET_DEVICE_LEVEL",
                "paras":{"value":"20"},
                "hasMore":0

            }
            * */
                //此处需要考虑兼容性，如果没有传mId，则不对其进行编码
                if (input.get("mid") != null) {
                    this.mid = input.get("mid").intValue();
                }
                this.cmd = input.get("cmd").asText();
                this.paras = input.get("paras");
                this.hasMore = input.get("hasMore").asInt();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public byte[] toByte() {
        try {
            if (this.msgType.equals("cloudReq")) {
                /*
                应用服务器下发的控制命令，本例只有一条控制命令：SET_DEVICE_LEVEL
                如果有其他控制命令，增加判断即可。
                * */
                if (this.cmd.equals("SET_DEVICE_LEVEL")) {
                    int brightlevel = paras.get("value").asInt();
                    byte[] byteRead = new byte[5];
                    byteRead[0] = (byte) 0xAA;
                    byteRead[1] = (byte) 0x72;
                    byteRead[2] = (byte) brightlevel;

                    //此处需要考虑兼容性，如果没有传mId，则不对其进行编码
                    if (Utilty.getInstance().isValidofMid(mid)) {
                        byte[] byteMid = new byte[2];
                        byteMid = Utilty.getInstance().int2Bytes(mid, 2);
                        byteRead[3] = byteMid[0];
                        byteRead[4] = byteMid[1];
                    }

                    return byteRead;
                }
            }

            /*
            平台收到设备的上报数据，根据需要编码ACK，对设备进行响应，如果此处返回null，表示不需要对设备响应。
            * */
            else if (this.msgType.equals("cloudRsp")) {
                /*byte[] ack = new byte[4];
                ack[0] = (byte) 0xAA;
                ack[1] = (byte) 0xAA;
                ack[2] = (byte) this.errcode;
                ack[3] = (byte) this.hasMore;
                return ack;*/
            	
            	
            	byte[] ack = Depacketize(this.requestDate);
            	return ack;
            	
            }
            return null;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return null;
        }
    }
    
    
    /**
     * 对平台应答时处理的报文
     * 
     */
  private  byte[] Depacketize(byte[] copyByt) {
	  
	  util=new Utilty();
    	
       this.verify=""+((copyByt[copyByt.length-2]<< 8 & 0XFFFF)+ (copyByt[copyByt.length-1] & 0xFF));
        
       this.contentionLength=(copyByt[25]<< 8 & 0XFFFF)+ (copyByt[26] & 0xFF);
      
       
       //重新组装返回byte的数组
    	byte[] newByt=new byte[34];
    	
    	for(int i=0;i<23;i++) {
    		newByt[i]=copyByt[i];
    	}
    	
    	//修改消息ID
    	newByt[7]=0x00;
    	newByt[8]=0x01;
    	
    	/**
    	 * 当util.bytes2Int(copyByt, 23, 2)+1解析出来的值超过int的值范围时会出现超出索引错误！
    	 *最典型的错误例子：当值为ffff时
    	 * 
    	 * 不能修改的原因是使用util.int2Bytes(newVales,2);的方法，方法中的参数是int，
    	 * 
    	 * 修改错误：把int转换为double
    	 * 
    	 */
    	//修改消息序号   需要遵从规则生成      
    	int newVales=(util.bytes2Int(copyByt, 23, 2)+1);
    	byte[] byteNumber= util.int2Bytes(newVales,2);
    	newByt[23]=byteNumber[0];
    	newByt[24]=byteNumber[1];
    	
    	//修改数据内容长度 Integer.toHexString(61)
    	newByt[25]=0x00;
    	newByt[26]=util.int2Bytes(5,1)[0];
    	
    	
    	/**
    	 * 编写数据内容的数据
    	 */
    	//对应数据帧的消息序号。
    	newByt[27]=copyByt[23];
    	newByt[28]=copyByt[24];
    	
    	//对应终端数据帧的消息ID
    	newByt[29]=copyByt[7];
    	newByt[30]=copyByt[8];
    	
    	
    	/**
    	 * 设置平台应答结果
    	 * 
    	 * 00H  -  帧无错误   01H – 帧数据异常 02H – 其它
    	 */
    	//帧结果
    	//校验码认证判断
        if(verify.equals(""+Utilty.CRC16IBM(copyByt,copyByt.length-2))) {
        	if(contentionLength==(copyByt.length-2-27)) {
            	newByt[31]=0x00;
            }else {
            	newByt[31]=0x01;
            }
        }else {
        	newByt[31]=0x01;
        }
        //内容长度认证判断
        
    	
    	byte[] by= util.int2Bytes( Utilty.CRC16IBM(newByt,32),2);
    	newByt[32]=by[0];
    	newByt[33]=by[1];
    	
    	return newByt;
    }

}
