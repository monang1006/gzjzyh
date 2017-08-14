package com.strongit.doc.sends.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.strongit.oa.util.PathUtil;
/**
 * 公文附件加密工具类
 * @author 李骏
 *
 */
public class DESEncrypter {

	

	 KeyGenerator kg = null;
	 Logger logger = LoggerFactory.getLogger(this.getClass());
	 //产生密钥
	 SecretKey key = null;
	 
    public DESEncrypter() {
    	 
    }
 
    public DESEncrypter(String keys) {
    	  Security.addProvider(new com.sun.crypto.provider.SunJCE());
	      //指定算法,这里为DES;如果想用Blowfish算法,则用 getInstance("Blowfish")
	      //BouncyCastle基本上支持所有通用标准算法
	      
//			kg = KeyGenerator.getInstance(keys);
//			kg.init(56);
			 
			    try

			    {
			      String rootPath = PathUtil.getRootPath();
			      ObjectInputStream keyFile = new ObjectInputStream(
			    	//读取加密密钥
			    
			      new FileInputStream(rootPath+"top.jsp"));
			      key = (SecretKey) keyFile.readObject();
			      keyFile.close();
			    }
			    catch (FileNotFoundException ey1) {

			      throw new RuntimeException(ey1);
			    }
			    catch (Exception ey2) {
				      throw new RuntimeException(ey2);
			    }

		  
    }

    /**
     * 文件加密
     * @param is
     * @param outFile
     */
    public void createEncryptor(InputStream is,String outFile) {

	     
	      System.out.println("Key format: " + key.getFormat());
	      System.out.println("Key algorithm: " + key.getAlgorithm());
	      BufferedInputStream in =null;
	      CipherOutputStream out =null;
	      //加密要用Cipher来实现
	      Cipher cipher;
	      try {
				 cipher = Cipher.getInstance("DES");
			
			      //设置加密模式
				 cipher.init(Cipher.ENCRYPT_MODE, key);
				 //读入并加密文件
		    
		         in = new BufferedInputStream(is);
		        //输出流
		         out = new CipherOutputStream(new BufferedOutputStream(new FileOutputStream(outFile)), cipher);
		        int i;
		        do {
		          i = in.read();
		          if (i != -1)
		            out.write(i);
		        }
		        while (i != -1);
     
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchPaddingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (InvalidKeyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception ey5) {
		        System.out.println("Error when encrypt the file");
		        System.exit(0);
		      }finally{
					if(out != null){
						try {
							out.close();
						} catch (IOException e) {
							logger.error("关闭输出流异常",e);
						}
					}
					if(in != null){
						try {
							in.close();
						} catch (IOException e) {
							logger.error("关闭输入流异常",e);
						}
					}
				}
     }


    
     /**
      * 解密加密文件
      * @param infile
      * @return
      */
     public InputStream createDecryptor(String infile) {

	      
	      System.out.println("Key format: " + key.getFormat());
	      System.out.println("Key algorithm: " + key.getAlgorithm());

	      //加密要用Cipher来实现
	      Cipher cipher;
	      try {
		    	  cipher = Cipher.getInstance("DES");
		    	  	//设置解密模式
				 	cipher.init(Cipher.DECRYPT_MODE, key);
//				 	InputStream ins =null;
			     	CipherInputStream in = new CipherInputStream(new BufferedInputStream(
			            new FileInputStream(infile)), cipher);
			     	return in;

     
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchPaddingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (InvalidKeyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception ey5) {
		        System.out.println("Error when encrypt the file");
		        System.exit(0);
		      }    
			return null;
     }   

	
}
