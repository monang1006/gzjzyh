package com.strongit.oa.util;
import java.io.IOException;   
import java.io.UnsupportedEncodingException;   
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.InvalidKeyException;   
import java.security.NoSuchAlgorithmException;   
import java.security.SecureRandom;   
import java.security.Security;   
import java.text.SimpleDateFormat;
import java.util.Calendar;
  
import javax.crypto.BadPaddingException;   
import javax.crypto.Cipher;   
import javax.crypto.IllegalBlockSizeException;   
import javax.crypto.KeyGenerator;   
import javax.crypto.NoSuchPaddingException;   
import javax.crypto.SecretKey;   
  
import org.apache.log4j.Logger;   
import org.apache.velocity.anakia.Escape;
  
import sun.misc.BASE64Decoder;   
import sun.misc.BASE64Encoder;   
  
import com.sun.crypto.provider.SunJCE;   
  
public class DesUtil {   
    private static final Logger logger = Logger.getLogger(DesUtil.class);   
    private static final String ALGORITHM = "DES";   
  
    private KeyGenerator keyGenerator;   
    private SecretKey secretKey;   
    private Cipher cipher;   
  
    public DesUtil(String key) {   
        this.init(key);   
    }   
  
    private void init(String key) {   
        Security.addProvider(new SunJCE());   
        try {   
            this.keyGenerator = KeyGenerator.getInstance(ALGORITHM);   
            this.keyGenerator.init(new SecureRandom(key.getBytes()));   
            this.secretKey = this.keyGenerator.generateKey();   
            this.cipher = Cipher.getInstance(ALGORITHM);   
        } catch (NoSuchAlgorithmException e) {   
            logger.error(e);   
        } catch (NoSuchPaddingException e) {   
            logger.error(e);   
        }   
  
    }   
  
    /**  
     * 加密  
     * @param expreStr  
     * @return  
     */  
    public String encry(String expreStr) {   
        byte[] cipByte = null;// 密文数组   
        byte[] expreByte = null;// 明文数组   
        String cipStr = ""; // 密文   
        BASE64Encoder base64Encoder = new BASE64Encoder();   
        try {   
            expreByte = expreStr.getBytes("UTF-8");   
            this.cipher.init(Cipher.ENCRYPT_MODE, this.secretKey);   
            cipByte = this.cipher.doFinal(expreByte);   
            cipStr = base64Encoder.encode(cipByte);   
        } catch (UnsupportedEncodingException e) {   
            logger.error(e);   
        } catch (InvalidKeyException e) {   
            logger.error(e);   
        } catch (IllegalBlockSizeException e) {   
            logger.error(e);   
        } catch (BadPaddingException e) {   
            logger.error(e);   
        }   
        return cipStr;   
    }   
  
    /**  
     * 解密  
     * @param cipStr  
     * @return  
     */  
    public String decry(String cipStr) {   
        byte[] cipByte = null;// 密文数组   
        byte[] expreByte = null; // 明文数组   
        String expreStr = "";// 明文   
        BASE64Decoder base64Decoder = new BASE64Decoder();   
        try {   
            cipByte = base64Decoder.decodeBuffer(cipStr);   
            this.cipher.init(Cipher.DECRYPT_MODE, this.secretKey);   
            expreByte = this.cipher.doFinal(cipByte);   
            expreStr = new String(expreByte, "UTF-8");   
        } catch (IOException e) {   
            logger.error(e);   
        } catch (InvalidKeyException e) {   
            logger.error(e);   
        } catch (IllegalBlockSizeException e) {   
            logger.error(e);   
        } catch (BadPaddingException e) {   
            logger.error(e);   
        }   
        return expreStr;   
    }   
       
    public static void main(String[] args) throws Exception { 
    	SimpleDateFormat dsdFormat = new SimpleDateFormat("yyyyMMdd");
		String strDate = dsdFormat.format(Calendar.getInstance().getTime());
        String pwd = "c4ca4238a0b923820dcc509a6f75849b";   
        System.out.println("加密前：" + pwd);   
        DesUtil desUtil = new DesUtil(strDate);   
        String encStr = desUtil.encry(pwd);   
        System.out.println("加密后：" + encStr);   
        String decStr = desUtil.decry(encStr);   
        System.out.println("解密后：" + decStr);   
        
        System.out.println(desUtil.decry("zZZottfP2a6dfRM3D+Pnlk7dIV0VTcuOH3xZMgqugY7YgDADFJ7u9A=="));
        
        String txt = URLEncoder.encode("zZZottfP2a6dfRM3D+Pnlk7dIV0VTcuOH3xZMgqugY7YgDADFJ7u9A==","utf-8");
        System.out.println(txt);
        txt = URLDecoder.decode(txt,"utf-8");
        System.out.println(txt);
    }   
} 