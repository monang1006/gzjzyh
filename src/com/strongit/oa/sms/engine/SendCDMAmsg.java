package com.strongit.oa.sms.engine;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.strongit.oa.util.LogPrintStackUtil;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

@Service
public class SendCDMAmsg{
	
	private Log logger = LogFactory.getLog(this.getClass());
	
	static CommPortIdentifier portId;
    static SerialPort serialPort;
    static InputStream inStream;
    static OutputStream outStream;
    
    private static final int BUFFER_SIZE = 16000;
    private static final int RECV_TIMEOUT = 30 * 1000;
    
////    设备参数
//    private int baud = 9600;       //每秒位数
//    private String port = "COM1";  //COM口
//    
//    每次可发送八十个字
    
    public static void main(String[] args){
    	SendCDMAmsg s = new SendCDMAmsg();
    	try {
//			s.sendSingleSms("18970802703","一二三四五六七八一十一二三四五六七八二十一二三四五六七八三十一二三四五六七八四十一二三四五六七八五十一二三四五六七八六十一二三四五六七八七十一二三四五六七八八十",115200,"COM10");
//			s.sendSingleSms("13767109565","您好",115200,"COM10");
    		
//    		String smsCon = "您好，因公司申报国家商务部服务外包人才资金，使用了您的相关信息，近期可能会有电话调查，请配合回答。培训时间：2009-9-20至2010-6-20，培训" ;
    		String smsCon = " 一个把String转成byte数组的小程序您好,用java 写一个10进制转16进制的算法 怎么写啊 - Java / Web";
    		
    		
    		s.sendSingleSms("18970802703",smsCon,115200,"COM10");
		} catch (Exception e) {
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
        String res = "";
        try
        {
        	try {
        		openModem(port,baud);//打开链接设备
            } catch (Exception e) {
            	LogPrintStackUtil.printInfo(logger, "openModem()失败:"+e.getMessage());
            	throw new Exception();
            }
            
            send("AT+CMGF=1\r");	//选择短消息信息格式：0-PDU;1-文本
            res = response();//获取返回值
            send("AT+WSCL=6,4\r");//at+WSCL=6,4 设定格式为中文
            res = response();//获取返回值
            sendMessage(num,content);//发送短信
            res = response();//获取返回值
            System.out.println("res:"+res);
            try {
            	serialPort.close(); 
            } catch (Exception e) {
            	LogPrintStackUtil.printInfo(logger, "serialPort.close()失败:"+e.getMessage());
            	return "CLOSEERROR";
            }
        }
        catch (Exception e)
        {
        	throw new Exception();
//            return "ERROR";
        }
        long eTime = System.currentTimeMillis();
        System.out.println("send sms to "+num+", by port:"+port+"----at------->"+new Date()+" by time:"+(eTime-sTime));
//        System.out.println("———————————短信发送用时为 ："+(eTime-sTime));
        return res;
    }
    
    
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
    
    public static void send(char c) throws Exception
    {
        outStream.write((byte) c);
        outStream.flush();
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
    
    
    public static String response(){
//      返回指令执行结果——开始 
        final int RETRIES = 3;
        final int WAIT_TO_RETRY = 100;
        StringBuffer buffer;
        int c, retry;

        retry = 0;
        buffer = new StringBuffer(BUFFER_SIZE);

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
//                        if(buffer.toString().matches("\\s*[\\p{ASCII}]*\\s+OK\\s")||
//                                buffer.toString().matches("\\s*[\\p{ASCII}]*\\s+ERROR\\s")||
//                                buffer.toString().matches("\\s*[\\p{ASCII}]*\\s+SIM PIN\\s")||
//                                buffer.toString().matches("\\s*[\\p{ASCII}]*\\s+READY\\s")){
//                            break;
//                        }
System.out.print((char) c);                        
                        if (buffer.toString().matches("\\s*[\\p{ASCII}]*\\s+OK\\s")) break;
                        if (buffer.toString().matches("\\s*[\\p{ASCII}]*\\s+ERROR\\s")) break;
                        if (buffer.toString().matches("\\s*[\\p{ASCII}]*\\s+SIM PIN\\s")) break;
                        if (buffer.toString().matches("\\s*[\\p{ASCII}]*\\s+READY\\s")) break;
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
        System.out.println("======================response()=========================\n"+buffer.toString());
//        返回指令结果 ——接结束
        return buffer.toString();
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
            if (hexB.length() <= 2) {   
                hexB = "00" + hexB;   
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
  

}