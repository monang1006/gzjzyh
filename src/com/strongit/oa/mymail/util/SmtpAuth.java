package com.strongit.oa.mymail.util;
/**
 * 邮件发送验证类
 * @author yuhz
 *
 */
public class SmtpAuth extends javax.mail.Authenticator {    
    private String username,password;    
   
    /**
     * @author：yuhz
     * @time：Feb 14, 200910:43:20 AM
     * @desc  验证类构造方法
     * @param username
     * @param password
     */
    public SmtpAuth(String username,String password){    
        this.username = username;     
        this.password = password;     
    }
    /**
     * @author：yuhz
     * @time：Feb 14, 200910:43:20 AM
     * @desc  调用验证类验证方法
     */
    protected javax.mail.PasswordAuthentication getPasswordAuthentication() {    
        return new javax.mail.PasswordAuthentication(username,password);    
    }    
}    