package com.strongit.doc.util;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class DESEncrypter {

    private String Algorithm = "DES";
    
    private SecretKey deskey;
    
    private Cipher cipher;
    
    private byte[] encryptorData;
    
    private byte[] decryptorData;

    public DESEncrypter() {
    	this("dengzczc");
    }
 
    public DESEncrypter(String key) {
    	Security.addProvider(new com.sun.crypto.provider.SunJCE());
    	deskey = new SecretKeySpec(key.getBytes(),Algorithm); 
        try {
			cipher = Cipher.getInstance(Algorithm);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		}
    }

    /**
     * 对Byte数组加密
     * @author:邓志城
     * @date:2010-9-7 上午10:19:14
     * @param datasource	需要加密的byte数组数据。
     * @return				加密后的数据
     */
    public byte[] createEncryptor(byte[] datasource) {
        try {
        	 cipher.init(Cipher.ENCRYPT_MODE, deskey);
             encryptorData = cipher.doFinal(datasource);
        }
        catch(java.security.InvalidKeyException ex){
            ex.printStackTrace();
        }
        catch(javax.crypto.BadPaddingException ex){
            ex.printStackTrace();
        }
        catch(javax.crypto.IllegalBlockSizeException ex){
            ex.printStackTrace();
        }
        return encryptorData;
     }

    /**
     * 对字符串加密
     * @author:邓志城
     * @date:2010-9-7 上午10:21:16
     * @param data		需要加密的字符串数据
     * @return			加密后的数据.
     * @throws Exception 
     */
    public String createEncryptor(String data) throws Exception { 
    	byte[] encryptorByte = createEncryptor(data.getBytes("utf-8"));
    	String byteToString = byteToString(encryptorByte);      
    	return byteToString ;
    }

    /**
     * 对字符串解密
     * @author:邓志城
     * @date:2010-9-7 上午10:28:28
     * @param data
     * @return
     * @throws IOException
     */
    public String createDecryptor(String data) throws IOException { 
    	byte[] stringToByte = stringToByte(data);
    	byte[]decryptorByte = createDecryptor(stringToByte);
    	return new String(decryptorByte,"utf-8");
    }
    
    /**
     * 对 datasource 数组进行解密
     * @param datasource 要解密的数据
     * @return 返回加密后的 byte[]
     */
     public byte[] createDecryptor(byte[] datasource) {
        try {
        	cipher.init(Cipher.DECRYPT_MODE, deskey);
        	decryptorData = cipher.doFinal(datasource);
        }
        catch(java.security.InvalidKeyException ex){
            ex.printStackTrace();
        }
        catch(javax.crypto.BadPaddingException ex){
            ex.printStackTrace();
        }
        catch(javax.crypto.IllegalBlockSizeException ex){
            ex.printStackTrace();
        }
        return decryptorData;        
     }
    
	/**
	 * 将字节数据转换成字符串
	 * @author:邓志城
	 * @date:2010-9-7 上午10:13:16
	 * @param dataByte		需要转换的字节数据
	 * @return				转换后的字符串
	 */
	private String byteToString(byte[] dataByte) {
		BASE64Encoder be = new BASE64Encoder();
        return be.encode(dataByte);
	}

	/**
	 * 将字符串转换成字节数组
	 * @author:邓志城
	 * @date:2010-9-7 上午10:14:52
	 * @param data				需要转换的字符串数据
	 * @return					字节数组
	 * @throws IOException
	 */
	private byte[] stringToByte(String data) throws IOException {  
		BASE64Decoder bd = new BASE64Decoder();
        byte[] sorData = bd.decodeBuffer(data);        
        return sorData;     
	}

	public static void main(String...strings) throws Exception {
		/*String str1 = "测试数据1";
		String str2 = "测试数据2";
		DESEncrypter des = new DESEncrypter("dengzczc");
		byte[] bstr1 = des.createEncryptor(str1.getBytes());
		System.out.println("str1=" + new String(bstr1)); 
		byte[] bdstr1 = des.createDecryptor(bstr1);
		System.out.println("dstr1 = " + new String(bdstr1)); 
		str2 = des.createEncryptor(str2);
		System.out.println(str2);
		str2 = des.createDecryptor(str2);
		System.out.println(str2);*/
		byte[] buf = "测试数据1".getBytes();
		DESEncrypter des = new DESEncrypter();
		String encrypterData = des.createEncryptor(new String(buf));
		buf = encrypterData.getBytes("utf-8");
		
		System.out.println(new String(buf,"utf-8"));
		String decryptorData = des.createDecryptor(new String(buf)) ;
		//doc.setDocContent(decryptorData.getBytes("utf-8"));
		System.out.println(decryptorData);
	}
	
}
