package com.strongit.oa.sms.engine;
import gnu.io.*;

import java.util.*;
import java.io.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.strongit.oa.util.LogPrintStackUtil;

@Service
public class CommTest {
	
	private Log logger = LogFactory.getLog(this.getClass());
	
	static CommPortIdentifier portIdCT;

	static Enumeration portList;

	static int bauds[] = { 9600, 115200 , 19200, 38400};
	
	static CommPortIdentifier portId;
    static SerialPort serialPort;
    static InputStream inStream;
    static OutputStream outStream;
    
    private static final int BUFFER_SIZE = 160000;
    private static final int RECV_TIMEOUT = 30 * 1000;
    
    protected static final int GSM7BITDEFAULT = 1;
    
	public String commTest() {
		portList = CommPortIdentifier.getPortIdentifiers();
		String res = "";

		while (portList.hasMoreElements()) {
			portIdCT = (CommPortIdentifier) portList.nextElement();
			if (portIdCT.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				for (int i = 0; i < bauds.length; i++) {
					LogPrintStackUtil.printInfo(logger,"Trying at "+portIdCT.getName() +"======>"+ bauds[i] + "...");
					try {
						try { 
			        		openModem(portIdCT.getName(),bauds[i]);//打开链接设备
			            } catch (Exception e) {
			            	System.out.println("Exception--->readSmsInSim:openModem("+portIdCT.getName()+","+bauds[i]+")    error");
			            }
			            send("AT+CMGF=0\r");//at+cmgf=0 设定格式为pdu; at+cmgf=1 设定格式为TEXT
			            res = response();
			            res = engContDecodecont(res);
			            System.out.println("AT+CMGF=0\\r------res="+res);
			            if(res.indexOf("OK") >= 0){
			            	send("AT+CSCS=?\r");//at+cmgf=0 设定格式为pdu; at+cmgf=1 设定格式为TEXT
			            	res = response();
			            	res = engContDecodecont(res);
			            	System.out.println("AT+CSCS\\r------res="+res);
			            	if(res.indexOf("CDMA") >= 0){
			            		return portIdCT.getName()+","+bauds[i]+",CDMA";
			            	}else{
			            		return portIdCT.getName()+","+bauds[i]+",GMS";
			            	}
						}else{
							LogPrintStackUtil.printInfo(logger, "端口:"+portIdCT.getName()+",传输比率:"+bauds[i]+"串口未连接短信猫:"+res);
						}
			            	
			            try { 
			            	if(serialPort!=null){
			            		serialPort.close(); 
			            	}
			            } catch (Exception e) {
			            	System.out.println("Exception--->readSmsInSim:serialPort.close()    error");
			            }
			            
					} catch (Exception e) {
						LogPrintStackUtil.printInfo(logger, "没有找到串口"+e.getMessage());
						return "  Nobody here!";
					} finally {
						serialPort.close();
						try {
							inStream.close();
							outStream.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
//			return "没有找到串口";
		}
		return "ERROR";//"端口查找完毕";
	}

	private static String getReply(InputStream inStream) {
		try {
			String response = "";
			int c = inStream.read();
			while (c != -1) {
				response += (char) c;
				c = inStream.read();
			}
			response = response.replaceAll("\n", "").replaceAll("\r", "");
			//System.out.println("Reply:" + response);
			return response;
		} catch (IOException e) {
			return "ERROR";
		}
	}

    /** 基础方法----以下*/
        public static void openModem(String port,int baud)throws Exception{ 
            try { 
//          打开设备——开始 
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
//          serialPort.addEventListener(this);
            serialPort.setSerialPortParams(baud, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            serialPort.setInputBufferSize(BUFFER_SIZE);
            serialPort.setOutputBufferSize(BUFFER_SIZE);
            serialPort.enableReceiveTimeout(RECV_TIMEOUT);
//          打开设备——结束
            } catch (Exception e) {
            	//e.printStackTrace();
            	throw new Exception();
            }
          
        }
        public static void send(String s){
//          发送指令——开始 
//            s = "AT+CMGL=?\r";
            for (int i = 0; i < s.length(); i ++)
            {
                try {
                    outStream.write((byte) s.charAt(i));
                    outStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
//            发送指令——结束
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
//          返回指令执行结果——开始 
            final int RETRIES = 3;
            final int WAIT_TO_RETRY = 100;
            StringBuffer buffer,resStr;
            int c, retry;

            retry = 0;
            buffer = new StringBuffer(BUFFER_SIZE);
            resStr = new StringBuffer(BUFFER_SIZE);

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
                            
                            if (buffer.toString().matches("\\s*[\\p{ASCII}]*\\s+OK\\s")) break;
                            if (buffer.toString().matches("\\s*[\\p{ASCII}]*\\s+ERROR\\s")) break;
                            if (buffer.toString().matches("\\s*[\\p{ASCII}]*\\s+SIM PIN\\s")) break;
                            if (buffer.toString().matches("\\s*[\\p{ASCII}]*\\s+READY\\s")) break;
                            if (buffer.toString().indexOf("\\s+OK\\s")>1) break;
//    						if(engContDecodecont(resStr.toString())
//    								.matches("\\p{Space}.+\\p{Space}+\\P{InGreek}+\\p{Space}+.+\\p{Space}+.+\\p{Space}+\\p{Punct}CDS:2,\\p{Digit}+,\\p{Punct}\\p{Digit}+\\p{Punct},\\p{Digit}+,(\\p{Punct}\\p{Digit}{2}){4}(\\p{Space}\\p{Punct}\\p{Digit}{2}){2}\",(\\p{Punct}\\p{Digit}{2}){4}(\\p{Space}\\p{Punct}\\p{Digit}{2}){2}\",\\p{Digit}+\\p{Space}+")){	//"+CDS:2"
//    							Thread.sleep(1000);
//                            	break;
//    						}
                            
//    						System.out.println("匹配正则："+engContDecodecont(resStr.toString())+"-->"+engContDecodecont(resStr.toString())
//    								.matches("\\p{Space}*(.+\\p{Space}+)"));
//                            if(resStr.toString().indexOf("13,10,79,75,13,")>1||		//"\\s*[\\p{ASCII}]*\\s+OK\\s"
//                            		resStr.toString().indexOf("13,10,69,82,82,79,82,13")>1){	//"\\s*[\\p{ASCII}]*\\s+ERROR\\s"
//                            							13,10,43,67,77,71,76,58					//"+CMGL:"
//                            	Thread.sleep(30000);
//                            	break;
//                            }
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
//            System.out.println("英文：resStr:\n"+engContDecodecont(resStr.toString())+"\n##################################");
//            System.out.println("byte:resStr:\n"+resStr.toString()+"\n##################################");
//            System.out.println("getResponse-----buffer.toString()=====\n"+buffer.toString());
//            System.out.println("=========================================================");
//            返回指令结果 ——接结束
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
