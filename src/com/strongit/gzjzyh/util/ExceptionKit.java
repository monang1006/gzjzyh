package com.strongit.gzjzyh.util;

import com.strongmvc.exception.SystemException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import org.dom4j.io.DocumentSource;
import org.dom4j.io.SAXReader;
import org.dom4j.DocumentHelper;
import org.dom4j.Document;
import org.dom4j.Element;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.io.PrintWriter;

public class ExceptionKit {

    protected ExceptionKit() {
    }

    public static String getStackTrace(Throwable t) {
        if (!(t.getMessage() != null && t.getMessage().length() > 0)) {
            if (t instanceof SystemException && ((SystemException) t).getRootCause() != null) {
                t = ((SystemException) t).getRootCause();
            }
        }
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }
    
    public static byte[] GenerateFormExceptionToXML(Exception exception) throws SystemException {
        byte[] binaryXML = null;
        try {
            Document document = DocumentHelper.createDocument();
            document.setXMLEncoding("UTF-8");
            Element exceptionElement = document.addElement("exception");
            Element messageElement = exceptionElement.addElement("message");
            String message = exception.getMessage();
            if (!(message != null && message.length() > 0)) {
                Throwable t = null;
                if (exception instanceof SystemException) {
                    t = ((SystemException) exception).getRootCause();
                } else {
                    t = exception;
                }
                if (t != null && t.getMessage() != null && t.getMessage().length() > 0) {
                    message = t.getMessage();
                } else if (t != null) {
                    message = t.toString();
                }
            }
            messageElement.setText(message == null ? "" : message);
            Element contentElement = exceptionElement.addElement("content");
            String content = ExceptionKit.getStackTrace(exception);
            contentElement.addCDATA(content == null ? "" : content);
            //生成XML输出流(解决回车换行、Tab等特殊字符转义符号，dom4j等采用apache XML解释器会过滤掉这些特殊字符，导致属性中的字符串格式丢失。)
            ByteArrayOutputStream xmlOutputStream = new ByteArrayOutputStream();
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            //transformer.setOutputProperty("indent", "yes");
            //transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.setOutputProperty("encoding", "UTF-8");
            DocumentSource source = new DocumentSource(document);
            StreamResult result = new StreamResult(xmlOutputStream);
            transformer.transform(source, result);
            binaryXML = xmlOutputStream.toByteArray();
            xmlOutputStream.close();
        } catch (Exception ex) {
            throw new SystemException(ex.getMessage(), ex);
        }
        return binaryXML;
    }

    public static String[] ParseFormExceptionToXML(byte[] binaryXml) throws SystemException {
        String[] formException = null;
        try {
            SAXReader reader = new SAXReader();
            ByteArrayInputStream bais = new ByteArrayInputStream(binaryXml);
            Document document = reader.read(bais);
            if (document != null) {
                Element exception = document.getRootElement();
                if (exception != null) {
                    Element message = exception.element("message");
                    Element content = exception.element("content");
                    formException = new String[2];
                    formException[0] = message.getText();
                    formException[1] = content.getText();
                }
            }
            bais.close();
        } catch (Exception ex) {
            throw new SystemException(ex.getMessage(), ex);
        }
        return formException;
    }
}
