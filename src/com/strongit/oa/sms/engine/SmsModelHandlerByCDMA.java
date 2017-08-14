package com.strongit.oa.sms.engine;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.strongit.oa.util.LogPrintStackUtil;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

@Service
public class SmsModelHandlerByCDMA{
	
	private Log logger = LogFactory.getLog(this.getClass());
	
	static CommPortIdentifier portId;
    static SerialPort serialPort;
    static InputStream inStream;
    static OutputStream outStream;
    
    private static final int BUFFER_SIZE = 160000;
    private static final int RECV_TIMEOUT = 30 * 1000;
    
    protected static final int GSM7BITDEFAULT = 1;
    
    private static final String MODEMRETURN_SUCESSEND = "13,10,79,75,13,";
    private static final String MODEMRETURN_SUCESSEND1 = "13,10,79,75,13,10,";
    
////    设备参数
//    private int baud = 9600;       //每秒位数
//    private String port = "COM1";  //COM口
//  
//  每次可发送八十个字
    
    
    public static void main(String[] args){
    	long sTime = System.currentTimeMillis();
    	SmsModelHandlerByCDMA s = new SmsModelHandlerByCDMA();
    	try {
//    		删除所有短信
//			String res = s.delSmsInSim(115200,"COM10");
    		
//    		收取所有短信
//			List<String[]> relst = s.readSmsInSim(115200,"COM10");
//			if(null!=relst){
//    			for(String[] objs :relst){
//    				System.out.println("\n"+objs[0]+":\n"+objs[1]);
//    			}
//    		}else{
//    			System.out.println("\n---->res:\n");
//    		}
    		
//    		发送短消息
    		String res = s.sendSingleSms("18970802703","001一个把String转成byte数组的小程序您好,用java 写一个10进制转16进制的算法 怎么写啊-Java",115200,"COM10");
    		System.out.println("\n---->res:\n"+res);
    		
			
			String res1 = s.sendSingleSms("13767109565","002一个把String转成byte数组的小程序您好,用java 写一个10进制转16进制的算法 怎么写啊-Java",115200,"COM10");
			System.out.println("\n---->res:\n"+res1);
			
			String res2 = s.sendSingleSms("18970802703","003一个把String转成byte数组的小程序您好,用java 写一个10进制转16进制的算法 怎么写啊-Java",115200,"COM10");
	    	System.out.println("\n---->res:\n"+res2);

			String res3 = s.sendSingleSms("13767109565","004一个把String转成byte数组的小程序您好,用java 写一个10进制转16进制的算法 怎么写啊-Java",115200,"COM10");
			System.out.println("\n---->res:\n"+res3);

			
	        long eTime = System.currentTimeMillis();
	        System.out.println("send sms to ---at----------------------------------->\n"+new Date(sTime)+"\n"+new Date(eTime)+" 共用时:"+((eTime-sTime)/1000)+"s");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /**
     * author:luosy
     * description:发送单条短信
     * modifyer:
     * description:
     * @param num 接受者手机号码
     * @param content 短信内容
     * @param baud	传输速率
     * @param port	COM端口
     */
    public String sendSingleSms(String num, String content, int baud,String port) throws Exception{
    	
        long sTime = System.currentTimeMillis();
        System.out.println("send sms to "+num+", by port:"+port+"----at------->"+new Date());
        String res = "";
        try
        {
        	try { 
        		openModem(port,baud);//打开链接设备
            } catch (Exception e) {
            	LogPrintStackUtil.printInfo(logger, "发送短信,打开设备出错( open_dev_error_port:"+port+")");
            	throw new Exception();
            }
            
            send("AT+CMGF=1\r");	//选择短消息信息格式：0-PDU;1-文本
            response();//获取返回值
            send("AT+WSCL=6,4\r");//at+WSCL=6,4 设定格式为中文
            response();//获取返回值
            
        	int len = content.length();
//        	超过70
        	if(len>70){
        		int tiaoshu = len/65+1; 	//短信条数
        		int k = 0;
        		 for(int i=1;i<=tiaoshu ;i++){
        			 String cont = "("+i+"/"+tiaoshu+")";
        			 
        			 if(i==tiaoshu){
        				 cont = cont + content.substring(k, len);
        				 sendMessage(num,cont);//发送短信
        				 res += response();
        			 }else{
        				 cont = cont + content.substring(k, i*65-i);
        				 sendMessage(num,cont);//发送短信
        				 res += response();
        			 }
        			 k=k+64;
        		 }
        	}else{
        		sendMessage(num,content);//发送短信
        		res += response();//获取返回值
        	}
            try {
            	serialPort.close(); 
            } catch (Exception e) {
            	return "CLOSEERROR";
            }
        }
        catch (Exception e)
        {
        	LogPrintStackUtil.printInfo(logger, "发送短信出错( send_sms_AT_Exception ):"+e.getMessage()+res);
        	throw new Exception();
//            return "ERROR";
        }
        long eTime = System.currentTimeMillis();
        System.out.println("\nsend sms to "+num+", by port:"+port+"----at------->"+new Date()+" ———————————by time:短信发送用时为 ："+(eTime-sTime)+"\nres:"+engContDecodecont(res) );
        
        if(null!=res&&!"".equals(res)){
			res = res.substring(0, res.length()-1);
			res = engContDecodecont(res);
		}else{
			res = "";
		}
        return res;
    }
    
    
    /**
     * author:luosy
     * description:读取sim卡上的短信
     * modifyer:
     * description:
     * @param num 接受者手机号码
     * @param content 短信内容
     * @param baud	传输速率
     * @param port	COM端口
     */
    public static List readSmsInSim(int baud,String port) throws Exception{
    	
        long sTime = System.currentTimeMillis();
        String res = "";
    	List<String[]> smslist = new ArrayList<String[]>();
        try
        {
        	try { 
        		openModem(port,baud);//打开链接设备
            } catch (Exception e) {
            	System.out.println("Exception--->readSmsInSim:openModem("+port+","+baud+")    error");
            	return smslist;
//            	throw new Exception();
//            	return "OPENERROR";
            }
            send("ATE1\r");  //ATE1 开启回显命 ATE0 关闭回显
            response();
            send("AT+CMGF=1\r");//at+cmgf=0 设定格式为pdu; at+cmgf=1 设定格式为TEXT
            response();
            send("AT+CMGL=\"ALL\"\r");
            res = response();
            res = res.trim();
            if((res.indexOf(MODEMRETURN_SUCESSEND)+MODEMRETURN_SUCESSEND.length())==res.length()
            		||(res.indexOf(MODEMRETURN_SUCESSEND1)+MODEMRETURN_SUCESSEND1.length())==res.length()){
            //读取成功
            	System.out.println("读取成功:\n"+res);
            	
            	if(isnosms(res)){//没有短信内容
            		return smslist;
            	}
            	
            	String[] allsms = res.split("13,10,13,10");

            	for(String eachsms:allsms){
//            		if(!eachsms.replaceAll("13", "").replaceAll("10", "")
//            				.replaceAll("79", "").replaceAll("75", "").replaceAll(",", "").equals("")){
            		if(!",13,10,79,75,13,10".equals(eachsms)&&!",13,10,79,75,13,10,".equals(eachsms)&&!",13,10,79,75,13,".equals(eachsms)){
            			String[] singlsms = eachsms.split("13,10");
            			String[] smsinfo = singlsms[singlsms.length-2].split(",44,");
//            			smsinfo,79,75,
            			String smscon = singlsms[singlsms.length-1];	//短信内容
            			String smscode = smsinfo[smsinfo.length-2]; //短信格式(50-->2 英文; 52-->中文;)
            			String phoneNo = smsinfo[smsinfo.length-4];	//手机号码
            			
            			String[] longsms = smscon.split(",");
            			if("5".equals(longsms[1])&&"0".equals(longsms[2])&&"3".equals(longsms[3])){
            				String flag = longsms[0]+","+longsms[1]+","+longsms[2]+","+longsms[3]+","+longsms[4]+","+longsms[5]+","+longsms[6];
//            				System.out.println("短信编号："+longsms[4]+"第"+longsms[6]+"/"+longsms[5]+"条：("+flag.length()+")");
            				smscon = smscon.substring(flag.length()+1, smscon.length()-1);
            				
            			}else{
            				smscon = smscon.substring(1, smscon.length()-1);
            			}
            			
//            			System.out.println("\n========>短信格式："+smscode);
//            			System.out.println("========>手机号码："+engContDecodecont(phoneNo));
//            			System.out.println("========>短信内容："+smscon);
            			
            			phoneNo = engContDecodecont(phoneNo).replaceAll("\"", "");
            			if("50".equals(smscode)){
            				smscon = engContDecodecont(smscon);
            			}else{
            				smscon = chnContDecodecont(smscon);
            			}
            			
            			smslist.add(new String[]{phoneNo,smscon});
            		}
            	}
            }else{
            	System.out.println("读取失败，返回错误信息:"+engContDecodecont(res));
            }
            
            try { 
            	serialPort.close(); 
            } catch (Exception e) {
            	System.out.println("Exception--->readSmsInSim:serialPort.close()    error");
            	return smslist;
//            	e.printStackTrace();
//            	throw e;
            }
        }
        catch (Exception e)
        {
        	e.printStackTrace();
        	throw new Exception();
        }finally{
        	try { 
            	serialPort.close(); 
            } catch (Exception e) {
            	System.out.println("Exception--->readSmsInSim:serialPort.close()    error");
            	return smslist;
//            	e.printStackTrace();
//            	throw e;
            }
        }
        long eTime = System.currentTimeMillis();
//        System.out.println("———————————短信接收用时为 ："+(eTime-sTime));
        return smslist;
    }
    
    
    /**
     * author:luosy
     * description: 删除sim卡上的短信
     * modifyer:
     * description:
     * @param num 接受者手机号码
     * @param content 短信内容
     * @param baud	传输速率
     * @param port	COM端口
     */
    public static String delSmsInSim(int baud,String port) throws Exception{
    	
        long sTime = System.currentTimeMillis();
        String res = "";
        try
        {
        	try { 
        		openModem(port,baud);//打开链接设备
            } catch (Exception e) {
            	throw new Exception();
//            	return "OPENERROR";
            }
            send("AT\r");//at+cmgf=0 设定格式为pdu
            res = response();//获取返回值
            send("AT+CMGD=1,4\r");
            res = response();
            try { 
            	serialPort.close(); 
            } catch (Exception e) {
            	
            }
        }
        catch (Exception e)
        {
        	throw new Exception();
        }
        long eTime = System.currentTimeMillis();
//        System.out.println("———————————短信删除用时为 ："+(eTime-sTime)+"\nres:\n"+engContDecodecont(res));
        return res;
    }
    
    
    
    
/** 基础方法----以下*/
    public static void openModem(String port,int baud)throws Exception{ 
        try { 
//      打开设备——开始 
        portId = CommPortIdentifier.getPortIdentifier(port);
        serialPort = (SerialPort) portId.open("jSMSEngine", 1971);
        inStream = serialPort.getInputStream();
        outStream = serialPort.getOutputStream();
        serialPort.notifyOnDataAvailable(true);
        serialPort.notifyOnOutputEmpty(true);
        serialPort.notifyOnBreakInterrupt(true);
        serialPort.notifyOnFramingError(true);
        serialPort.notifyOnOverrunError(true);
        serialPort.notifyOnParityError(true);
        //serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
        serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN);
//      serialPort.addEventListener(this);
        serialPort.setSerialPortParams(baud, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
        serialPort.setInputBufferSize(BUFFER_SIZE);
        serialPort.setOutputBufferSize(BUFFER_SIZE);
        serialPort.enableReceiveTimeout(RECV_TIMEOUT);
//      打开设备——结束
        } catch (Exception e) {
        	//e.printStackTrace();
        	throw new Exception();
        }
      
    }
    public static void send(String s){
//      发送指令——开始 
//        s = "AT+CMGL=?\r";
        for (int i = 0; i < s.length(); i ++)
        {
            try {
                outStream.write((byte) s.charAt(i));
                outStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        发送指令——结束
    }
    
    public static void send(List<Byte> b) throws Exception
    {
    	for (int i = 0; i < b.size(); i ++)
        {
            try {
                outStream.write(b.get(i));
                outStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void send(char c) throws Exception
    {
        outStream.write((byte) c);
        outStream.flush();
    }
    
    
    public static String response(){
//      返回指令执行结果——开始 
        final int RETRIES = 3;
        final int WAIT_TO_RETRY = 100;
        StringBuffer buffer,resStr;
        int c, retry;

        retry = 0;
        buffer = new StringBuffer(BUFFER_SIZE);
        resStr = new StringBuffer(BUFFER_SIZE);

//System.out.println("##################################________byte");
        while (retry < RETRIES)
        {
            try
                {
                    while (true)
                    {
                        c = inStream.read();
                        if (c == -1)
                        {
                            buffer.delete(0, buffer.length());
                            break;
                        }
                        buffer.append((char) c);
                        resStr.append(c+",");
                        
                        if (buffer.toString().matches("\\s*[\\p{ASCII}]*\\s+OK\\s")
                        		&& !buffer.toString().matches("\\s*[\\p{ASCII}]*\\s+\\p{Punct}CMGS:\\p{Digit}+\\s+OK\\s")) break;
                        if (buffer.toString().matches("\\s*[\\p{ASCII}]*\\s+ERROR\\s")) break;
                        if (buffer.toString().matches("\\s*[\\p{ASCII}]*\\s+SIM PIN\\s")) break;
                        if (buffer.toString().matches("\\s*[\\p{ASCII}]*\\s+READY\\s")) break;
//                        if (buffer.toString().indexOf("\\s+OK\\s")>1) break;
//						if(engContDecodecont(resStr.toString())
//								.matches("\\p{Space}.+\\p{Space}+\\P{InGreek}+\\p{Space}+.+\\p{Space}+.+\\p{Space}+\\p{Punct}CDS:2,\\p{Digit}+,\\p{Punct}\\p{Digit}+\\p{Punct},\\p{Digit}+,(\\p{Punct}\\p{Digit}{2}){4}(\\p{Space}\\p{Punct}\\p{Digit}{2}){2}\",(\\p{Punct}\\p{Digit}{2}){4}(\\p{Space}\\p{Punct}\\p{Digit}{2}){2}\",\\p{Digit}+\\p{Space}+")){	//"+CDS:2"
//							Thread.sleep(1000);
//                        	break;
//						}
                        
//						System.out.println("匹配正则："+engContDecodecont(resStr.toString())+"-->"+engContDecodecont(resStr.toString())
//								.matches("\\p{Space}*(.+\\p{Space}+)"));
//                        if(resStr.toString().indexOf("13,10,79,75,13,")>1||		//"\\s*[\\p{ASCII}]*\\s+OK\\s"
//                        		resStr.toString().indexOf("13,10,69,82,82,79,82,13")>1){	//"\\s*[\\p{ASCII}]*\\s+ERROR\\s"
//                        							13,10,43,67,77,71,76,58					//"+CMGL:"
//                        	Thread.sleep(30000);
//                        	break;
//                        }
                    }
                    retry = RETRIES;
                }
            catch (Exception e)
            {
                if (retry < RETRIES)
                {
                    try {
                        Thread.sleep(WAIT_TO_RETRY);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    retry ++;
                } else
                    try {
                        throw e;
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
            }
        }
       // while (dataAvailable()) inStream.read();
//        System.out.println("英文：resStr:\n"+engContDecodecont(resStr.toString())+"\n##################################");
//        System.out.println("byte:resStr:\n"+resStr.toString()+"\n##################################");
//        System.out.println("getResponse-----buffer.toString()=====\n"+buffer.toString());
//        System.out.println("=========================================================");
//        返回指令结果 ——接结束
        return resStr.toString();
    }

    public static void sendMessage(String phoneNum,String contents){
        try {
        	List<Byte> strlist = gbEncoding(contents);
        	send("AT+CMGS=\""+phoneNum+"\"\r");
        	send(strlist);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static String toBCDFormat(String s)
    {
        String bcd;
        int i;

        if ((s.length() % 2) != 0) s = s + "F";
        bcd = "";
        for (i = 0; i < s.length(); i += 2) bcd = bcd + s.charAt(i + 1) + s.charAt(i);
        return bcd; 
    }
    private static String getValidityPeriodBits()
    {
        String bits;
        int value;
        int validityPeriod = -1;

        if (validityPeriod == -1) bits = "FF";
        else
        {
            if (validityPeriod <= 12) value = (validityPeriod * 12) - 1;
            else if (validityPeriod <= 24) value = (((validityPeriod - 12) * 2) + 143);
            else if (validityPeriod <= 720) value = (validityPeriod / 24) + 166;
            else value = (validityPeriod / 168) + 192;
            bits = Integer.toHexString(value);
            if (bits.length() != 2) bits = "0" + bits;
            if (bits.length() > 2) bits = "FF";
        }
        return bits;
    }
    public static String substituteSymbol(String text, String symbol, String value)
    {
        StringBuffer buffer;

        while (text.indexOf(symbol) >= 0)
        {
            buffer = new StringBuffer(text);
            buffer.replace(text.indexOf(symbol), text.indexOf(symbol) + symbol.length(), value);
            text = buffer.toString();
        }
        return text;
    }
    
    public static void clearBuffer() throws Exception
    {
        while (dataAvailable()) inStream.read();
    }
    public static boolean dataAvailable() throws Exception
    {
        return (inStream.available() > 0 ? true : false);
    }
    
    public static List<Byte> gbEncoding(final String gbString) {
        char[] utfBytes = gbString.toCharArray();   
        String unicodeBytes = "";   
        for (int byteIndex = 0; byteIndex < utfBytes.length; byteIndex++) {   
            String hexB = Integer.toHexString(utfBytes[byteIndex]);   
            if (hexB.length() == 2) {   
                hexB = "00" + hexB;   
            }
            if (hexB.length() == 1) {   
                hexB = "000" + hexB;   
            }  
            unicodeBytes = unicodeBytes + hexB; 
        } 
        
        //将unicodeBytes转换成16进制的byte
        List<Byte> strlist = new ArrayList<Byte>();
    	for(int i=0;i<unicodeBytes.length();i=i+2){
        	strlist.add((byte)Integer.parseInt(unicodeBytes.substring(i, i+2), 16));
        }
    	
    	//结束符号
    	strlist.add((byte) 0x00);
    	strlist.add((byte) 0x1a);
        return strlist;   
    }

    public static String decodeUnicode(final String dataStr) {   
        int start = 0;   
        int end = 0;   
        final StringBuffer buffer = new StringBuffer();   
        while (start > -1) {   
            end = dataStr.indexOf("\\u", start + 2);   
            String charStr = "";   
            if (end == -1) {   
                charStr = dataStr.substring(start + 2, dataStr.length());   
            } else {   
                charStr = dataStr.substring(start + 2, end);   
            }   
            char letter = (char) Integer.parseInt(charStr, 16); // 16进制parse整形字符串。   
            buffer.append(new Character(letter).toString());   
            start = end;   
        }   
        return buffer.toString();   
    }
    
    public static String chnContDecodecont(String str){
    	String[] arrstr = str.split(",");
    	StringBuilder sb = new StringBuilder();
    	String st ="";
    	String string ="";
    	try{
    		for(int i=0;i+1<arrstr.length;i=i+2){
    			sb.append("\\u");
    			st = Integer.toHexString(Integer.parseInt(arrstr[i]));    
                if (st.length() < 2) {    
                    sb.append("0"); 
                    sb.append(st);
                }else{
                	sb.append(st);
                }
                st = Integer.toHexString(Integer.parseInt(arrstr[i+1]));    
                if (st.length() < 2) {    
                    sb.append("0"); 
                    sb.append(st);
                }else{
                	sb.append(st);
                }
    		}
    		
    		string = decodeUnicode(sb.toString()); 
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return string;
    }
    
    public static String engContDecodecont(String str){
    	if("".equals(str)||null==str){
    		return "";
    	}
    	String[] arrstr = str.split(",");
    	StringBuilder sb = new StringBuilder();
    	String string ="";
    	try{
    		for(int i=0;i<arrstr.length;i++){
    			
    			sb.append("\\u00");
                sb.append(new String(String.format("%x", Integer.parseInt(arrstr[i]))));
    		}
    		
    		string = decodeUnicode(sb.toString()); 
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return string;
    }
    
    public static boolean isnosms(String res){
    	res = res.substring(0, res.length()-1);
    	res = engContDecodecont(res);
    	res = res.replaceAll("\\s", "");
    	if("AT+CMGL=\"ALL\"OK".equals(res)){
    		return true;
    	}else if("AT+CMGF=1OK".equals(res)){
    		return true;
    	}else{
    		return false;
    	}
    }
}