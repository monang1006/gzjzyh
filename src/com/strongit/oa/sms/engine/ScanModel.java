package com.strongit.oa.sms.engine;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.stereotype.Service;

import com.strongit.oa.bo.ToaCalendarRemind;

@Service
public class ScanModel {

	static CommPortIdentifier portId;

	static SerialPort serialPort;

	static InputStream inStream;

	static OutputStream outStream;

	private static final int BUFFER_SIZE = 16000;

	private static final int RECV_TIMEOUT = 30 * 1000;

	protected static final int GSM7BITDEFAULT = 1;

	private static final String smscNumber = "";

	protected static final String MESSAGE_ENCODING = "MESSAGE_ENCODING_UNICODE";

	// // 设备参数
	// private int baud = 9600; //每秒位数
	// private String port = "COM1"; //COM口

	ScanThread scanThread;
	
	public static void main(String[] args) {
//		readSmsInSim(9600, "COM3");
		
		final Timer timer = new Timer();
		ScanModel scan = new ScanModel();
		
		openModem("COM3", 9600);
		send("AT\r");
		send("AT\r");
		try {
			timer.schedule(new TimerTask() {
				public void run() {
					System.out.println("###########################"+new Date()+"执行");
					send("AT\r");
					send("AT\r");
					send("AT\r");
					String res = response();
					System.out.println(res);
					serialPort.close();
				}
			
			}, 5*60*100);
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
		}

		
//		try {
//			scan.process();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	private void process() throws Exception
	{
		this.scanThread = new ScanThread();
		this.scanThread.start();
	}
	
	class ScanThread extends Thread
	{
		int i=0;
		@Override
		public void run()
		{
			try
			{
				while (i<10)
				{
					System.out.println("###########################第"+i+"次执行");
					send("AT\r");
					String res = response();
					System.out.println(res);
					i++;
				}
				
				try {
					serialPort.close();
					System.out.println("###########################结束执行 关闭连接");
				} catch (Exception e) {

				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	
	
	/**
	 * author:luosy description:发送单条短信 modifyer: description:
	 * 
	 * @param num
	 *            接受者手机号码
	 * @param content
	 *            短信内容
	 * @param baud
	 *            传输速率
	 * @param port
	 *            COM端口
	 */
	public static String readSmsInSim(int baud, String port) {

		long sTime = System.currentTimeMillis();
		String res = "";
		try {
			openModem(port, baud);// 打开链接设备
			send("AT+CMGF=0\r");// at+cmgf=0 设定格式为pdu
			res = response();// 获取返回值
			// sendMessage(num,content);//发送短信
			send("AT+CMGL=?\r");
			res = response();

			send("AT+CMGL=\"4\"\r");
			res = response();

			try {
				serialPort.close();
			} catch (Exception e) {

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		long eTime = System.currentTimeMillis();
		System.out.println("———————————短信发送用时为 ：" + (eTime - sTime));
		return res;
	}

	public static void openModem(String port, int baud) {
		try {
			// 打开设备——开始
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
			// serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
			serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN);
			// serialPort.addEventListener(this);
			serialPort.setSerialPortParams(baud, SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
			serialPort.setInputBufferSize(BUFFER_SIZE);
			serialPort.setOutputBufferSize(BUFFER_SIZE);
			serialPort.enableReceiveTimeout(RECV_TIMEOUT);
			// 打开设备——结束
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void send(String s) {
		// 发送指令——开始
		// s = "AT+CMGL=?\r";
		for (int i = 0; i < s.length(); i++) {
			try {
				outStream.write((byte) s.charAt(i));
				outStream.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// 发送指令——结束
	}

	public static void send(char c) throws Exception {
		outStream.write((byte) c);
		outStream.flush();
	}

	public static String response() {
		// 返回指令执行结果——开始
		final int RETRIES = 3;
		final int WAIT_TO_RETRY = 1000;
		StringBuffer buffer;
		int c, retry;

		retry = 0;
		buffer = new StringBuffer(BUFFER_SIZE);

		while (retry < RETRIES) {
			try {
				while (true) {
					c = inStream.read();
					if (c == -1) {
						buffer.delete(0, buffer.length());
						break;
					}
					buffer.append((char) c);
					// if(buffer.toString().matches("\\s*[\\p{ASCII}]*\\s+OK\\s")||
					// buffer.toString().matches("\\s*[\\p{ASCII}]*\\s+ERROR\\s")||
					// buffer.toString().matches("\\s*[\\p{ASCII}]*\\s+SIM
					// PIN\\s")||
					// buffer.toString().matches("\\s*[\\p{ASCII}]*\\s+READY\\s")){
					// break;
					// }

					if (buffer.toString().matches("\\s*[\\p{ASCII}]*\\s+OK\\s"))
						break;
					if (buffer.toString().matches(
							"\\s*[\\p{ASCII}]*\\s+ERROR\\s"))
						break;
					if (buffer.toString().matches(
							"\\s*[\\p{ASCII}]*\\s+SIM PIN\\s"))
						break;
					if (buffer.toString().matches(
							"\\s*[\\p{ASCII}]*\\s+READY\\s"))
						break;
				}
				retry = RETRIES;
			} catch (Exception e) {
				if (retry < RETRIES) {
					try {
						Thread.sleep(WAIT_TO_RETRY);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					retry++;
				} else
					try {
						throw e;
					} catch (Exception e1) {
						e1.printStackTrace();
					}
			}
		}
		// while (dataAvailable()) inStream.read();
		// System.out.println("getResponse-----buffer.toString()====="+buffer.toString());
		// 返回指令结果 ——接结束
		return buffer.toString();
	}

	public static void sendMessage(String phoneNum, String contents) {

		String str1, str2;
		String pdu;
		int i, j, high, low;
		char c;

		pdu = "";
		if ((smscNumber != null) && (smscNumber.length() == 0))
			pdu = pdu + "00";
		pdu = pdu + "11";
		str1 = phoneNum;// "13870932241";
		str1 = toBCDFormat(str1);
		str2 = Integer.toHexString(phoneNum.length());
		pdu = pdu + "00";
		str1 = "81" + str1;
		if (str2.length() != 2)
			str2 = "0" + str2;
		pdu = pdu + str2 + str1;
		pdu = pdu + "00"; // pdu = 0011000b813167179065F500

		str1 = phoneNum;
		pdu = pdu + "08";
		pdu = pdu + getValidityPeriodBits();

		str1 = contents;
		str2 = "";
		for (i = 0; i < str1.length(); i++) {
			c = str1.charAt(i);
			high = (int) (c / 256);
			low = c % 256;
			str2 = str2
					+ ((Integer.toHexString(high).length() < 2) ? "0"
							+ Integer.toHexString(high) : Integer
							.toHexString(high));
			str2 = str2
					+ ((Integer.toHexString(low).length() < 2) ? "0"
							+ Integer.toHexString(low) : Integer
							.toHexString(low));
		}
		str1 = Integer.toHexString(contents.length() * 2);
		if (str1.length() != 2)
			str1 = "0" + str1;
		pdu = pdu + str1 + str2;

		pdu = pdu.toUpperCase();

		j = pdu.length();
		j /= 2;
		if (smscNumber == null)
			;
		else if (smscNumber.length() == 0)
			j--;
		else {
			j -= ((smscNumber.length() - 1) / 2);
			j -= 2;
		}

		try {
			send(substituteSymbol("AT+CMGS=\"{1}\"\r", "\"{1}\"", "" + j));
			// response();
			send(pdu);
			send((char) 26);
			response();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static String toBCDFormat(String s) {
		String bcd;
		int i;

		if ((s.length() % 2) != 0)
			s = s + "F";
		bcd = "";
		for (i = 0; i < s.length(); i += 2)
			bcd = bcd + s.charAt(i + 1) + s.charAt(i);
		return bcd;
	}

	private static String getValidityPeriodBits() {
		String bits;
		int value;
		int validityPeriod = -1;

		if (validityPeriod == -1)
			bits = "FF";
		else {
			if (validityPeriod <= 12)
				value = (validityPeriod * 12) - 1;
			else if (validityPeriod <= 24)
				value = (((validityPeriod - 12) * 2) + 143);
			else if (validityPeriod <= 720)
				value = (validityPeriod / 24) + 166;
			else
				value = (validityPeriod / 168) + 192;
			bits = Integer.toHexString(value);
			if (bits.length() != 2)
				bits = "0" + bits;
			if (bits.length() > 2)
				bits = "FF";
		}
		return bits;
	}

	public static String substituteSymbol(String text, String symbol,
			String value) {
		StringBuffer buffer;

		while (text.indexOf(symbol) >= 0) {
			buffer = new StringBuffer(text);
			buffer.replace(text.indexOf(symbol), text.indexOf(symbol)
					+ symbol.length(), value);
			text = buffer.toString();
		}
		return text;
	}

	public static void clearBuffer() throws Exception {
		while (dataAvailable())
			inStream.read();
	}

	public static boolean dataAvailable() throws Exception {
		return (inStream.available() > 0 ? true : false);
	}

}