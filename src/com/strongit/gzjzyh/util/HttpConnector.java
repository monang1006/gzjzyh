package com.strongit.gzjzyh.util;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.HttpClient;

import com.strongit.gzjzyh.GzjzyhConstants;
import com.strongmvc.exception.SystemException;
import java.io.ByteArrayInputStream;

public class HttpConnector {

    private HttpClient httpClient = null;
    private String uri = null;

    public void open(String connectionString) throws SystemException {
        try {
            this.uri = connectionString;
            this.httpClient = new HttpClient();
            this.httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(300000); //300秒
        } catch (Exception ex) {
            throw new SystemException(ex.getMessage(), ex);
        }
    }

    public void close() throws SystemException {
    	
    }

    public void perform(String msgContent) throws SystemException {
        try {
            byte[] binaryRequestPacket = msgContent.getBytes("UTF-8");
            ByteArrayInputStream isRequestPacket = new ByteArrayInputStream(binaryRequestPacket);
            InputStreamRequestEntity requestEntity = new InputStreamRequestEntity(isRequestPacket, binaryRequestPacket.length, GzjzyhConstants.GZJZYH_CONTENT_TYPE);
            //发出Http请求
            PostMethod postMethod = new PostMethod(this.uri);
            DefaultHttpMethodRetryHandler defaultHttpMethodRetryHandler = new DefaultHttpMethodRetryHandler(0, true);
            postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, defaultHttpMethodRetryHandler);
            postMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 300000); //300秒
            postMethod.getParams().setContentCharset("utf-8");
            postMethod.setRequestEntity(requestEntity);
            int httpStatus = this.httpClient.executeMethod(postMethod);
            if (HttpStatus.SC_OK == httpStatus) {
                String responseText = postMethod.getResponseBodyAsString();
                if (!GzjzyhConstants.HTTP_RESPONSE_SUCCESS.equals(responseText)) {
                	throw new SystemException("响应内容不正确：" + responseText);
                }
            } else {
                throw new SystemException("发送请求失败(HTTP Status Code " + httpStatus + " - " + HttpStatus.getStatusText(httpStatus) + ")。");
            }
        } catch (SystemException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new SystemException(ex.getMessage(), ex);
        }
    }
}
