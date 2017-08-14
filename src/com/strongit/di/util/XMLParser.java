package com.strongit.di.util;

import com.strongit.di.packet.*;
import com.strongit.di.exception.SystemException;
import org.dom4j.*;
import org.dom4j.io.*;
import java.util.*;
import java.io.*;


/**
 * XML解释器
 * <p>Title: Strong Data Interchange System</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2007 Jiang Xi Strong Co. Ltd. </p>
 * <p>Company: </p>
 * @author minhongbin@hotmail.com
 * @version 1.0
 */
public class XMLParser {
  public XMLParser() {
  }

  /**
   * 将无效的XML字符替换成空格符
   * @param str
   * @return
   * @throws CAException
   */
  public static String replaceInvalidXMLCharacter(String str) {
    char cs[] = null;
    boolean bChanged = false;

    if (str != null && str.length() > 0) {
      cs = str.toCharArray();
      for (int i = 0; i < cs.length; i++) {
        if (cs[i] < 0x0020 && cs[i] != 0x0009 && cs[i] != 0x000A &&
            cs[i] != 0x000D &&
            cs[i] != 0x0000) {
          cs[i] = ' ';
          bChanged = true;
        }
      }
    }

    return (bChanged ? new String(cs) : str);
  }
/**
 * 
 * author:zhoujx
 * description:根据packet类生成xml文件
 * @param packet
 * @param filename
 * @throws SystemException
 */
  public static void generateToFile(Packet packet, String filename) throws
      SystemException {

    if (packet == null || filename == null) {
      return;
    }

    try {
      Document document = DocumentHelper.createDocument();
      if (document != null) {
        //PACK
        Element packElement = document.addElement(PacketTag.TAG_PACK);
        if (packElement != null) {
          //HEAD
          generateHEAD(packElement, packet);
          //DATA
          Element dataElement = packElement.addElement(PacketTag.TAG_DATA);
          if (dataElement != null) {
            //VAR
            generateVAR(dataElement, packet);
            //RSS
            generateRSS(dataElement, packet);
          }
        }

        //save to file
        OutputFormat format = OutputFormat.createPrettyPrint();
        XMLWriter writer = new XMLWriter(new FileOutputStream(filename), format);
        writer.write(document);
        writer.close();
      }
    }
    catch (IOException ex) {
      throw new SystemException("XMLParser.generateToFile.IOException", ex);
    }
  }

  public static String generate(Packet packet) throws SystemException {
    String strPacket = null;

    if (packet != null) {
      Document document = DocumentHelper.createDocument();
      if (document != null) {
        //PACK
        Element packElement = document.addElement(PacketTag.TAG_PACK);
        if (packElement != null) {
          //HEAD
          generateHEAD(packElement, packet);
          //DATA
          Element dataElement = packElement.addElement(PacketTag.TAG_DATA);
          if (dataElement != null) {
            //VAR
            generateVAR(dataElement, packet);
            //RSS
            generateRSS(dataElement, packet);
          }
        }

        strPacket = document.asXML();
      }
    }

    return strPacket;
  }
/**
 * 
 * author:
 * description:生成xml文件head部分
 * @param packElement
 * @param packet
 */
  protected static void generateHEAD(Element packElement, Packet packet) {
    Element headElement = packElement.addElement(PacketTag.TAG_HEAD);
    Map headMap = packet.getHeadMap();
    if (headElement != null && headMap != null) {
      for (Iterator iter = headMap.keySet().iterator(); iter.hasNext(); ) {
        String name = (String) iter.next();
        String value = replaceInvalidXMLCharacter( (String) headMap.get(name));
        headElement.addElement(name).addText(value); 
      }
    }
  }
/**
 * 
 * author:
 * description:生成xml文件VAR部分
 * @param dataElement
 * @param packet
 */
  protected static void generateVAR(Element dataElement, Packet packet) {
    Element varElement = dataElement.addElement(PacketTag.TAG_VAR);
    Map varMap = packet.getVarMap();
    if (varElement != null && varMap != null) {
      for (Iterator iter = varMap.keySet().iterator(); iter.hasNext(); ) {
        String name = (String) iter.next();
        String value = replaceInvalidXMLCharacter( (String) varMap.get(name));
        varElement.addElement(name).addCDATA(value);
      }
    }
  }
/**
 * 
 * author:
 * description:生成xml文件RSS(数据集)部分
 * @param dataElement
 * @param packet
 */
  protected static void generateRSS(Element dataElement, Packet packet) {
    Element rssElement = dataElement.addElement(PacketTag.TAG_RSS);
    Map rsMap = packet.getRSMap();
    Map metaMap = packet.getRSMetaMap();
    if (rssElement != null && rsMap != null && metaMap != null) {
      for (Iterator rsIter = rsMap.keySet().iterator(); rsIter.hasNext(); ) {
        String name = (String) rsIter.next();
        Element rsElement = rssElement.addElement(PacketTag.TAG_RS);
        if (rsElement != null) {
          rsElement.addAttribute(PacketTag.TAG_RS_NAME, name);
          List metaList = (List) metaMap.get(name);
          generateRS_METADATA(rsElement, metaList);
          List rowList = (List) rsMap.get(name);
          generateRS_ROWDATA(rsElement, rowList);
        } //if (rsElement != null) {
      } //for (Iterator rsIter = rsMap.keySet().iterator(); rsIter.hasNext(); ) {
    } //if (rssElement != null && rsMap != null && metaMap != null) {
  }
/**
 * 
 * author:
 * description:生成xml文件METADATA(元数据描述)部分
 * @param rsElement
 * @param metaList
 */
  protected static void generateRS_METADATA(Element rsElement, List metaList) {
    Element metaElement = rsElement.addElement(PacketTag.TAG_METADATA);
    if (metaElement != null) {
      Element fieldsElement = metaElement.addElement(PacketTag.TAG_FIELDS);
      if (fieldsElement != null && metaList != null && metaList.size() > 0) {
        for (int i = 0; i < metaList.size(); i++) {
          Map fieldMap = (Map) metaList.get(i);
          Element fieldElement = fieldsElement.addElement(PacketTag.TAG_FIELD);
          if (fieldElement != null && fieldMap != null) {
            for (Iterator iter = fieldMap.keySet().iterator(); iter.hasNext(); ) {
              String name = (String) iter.next();
              String value = replaceInvalidXMLCharacter( (String) fieldMap.get(
                  name));
              fieldElement.addAttribute(name, value);
            }
          } //if (fieldElement != null && fieldMap != null) {
        } //for (int i = 0; i < metaList.size(); i++) {
      } //if (fieldsElement != null && metaList != null && metaList.size() > 0) {
    } //if (metaElement != null) {
  }
  /**
   * 
   * author:
   * description:生成xml文件RS_ROWDATA(数据行)部分
   * @param rsElement
   * @param metaList
   */
  protected static void generateRS_ROWDATA(Element rsElement, List rowList) {
    Element rowsElement = rsElement.addElement(PacketTag.TAG_ROWDATA);
    if (rowsElement != null && rowList != null && rowList.size() > 0) {
      for (int i = 0; i < rowList.size(); i++) {
        Map rowMap = (Map) rowList.get(i);
        Element rowElement = rowsElement.addElement(PacketTag.TAG_ROW);
        if (rowElement != null && rowMap != null) {
          for (Iterator iter = rowMap.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String value = replaceInvalidXMLCharacter( (String) rowMap.get(name));
            rowElement.addAttribute(name, value);
          }
        } //if (rowElement != null && rowMap != null) {
      } //for (int i = 0; i < rowList.size(); i++) {
    } //if (rowsElement != null && rowList != null && rowList.size() > 0) {
  }
/**
 * 
 * author:
 * description:解析xml文件生成packet对象
 * @param strPacket
 * @return
 * @throws SystemException
 */
  public static Packet parser(String strPacket) throws SystemException {
    Packet packet = null;

    if (strPacket != null && strPacket.length() > 0) {
      packet = new Packet();

      try {
        SAXReader reader = new SAXReader();
        Document document = reader.read(new StringReader(strPacket));
        //PACK
        Element rootElement = document.getRootElement();
        if (rootElement != null) {
          //HEAD
          Element headElement = rootElement.element(PacketTag.TAG_HEAD);
          parserHEAD(headElement, packet);
          //DATA
          Element dataElement = rootElement.element(PacketTag.TAG_DATA);
          if (dataElement != null) {
            //VAR
            Element varElement = dataElement.element(PacketTag.TAG_VAR);
            parserVAR(varElement, packet);
            //RSS
            Element rssElement = dataElement.element(PacketTag.TAG_RSS);
            parserRSS(rssElement, packet);
          } //if (dataElement != null) {
        } //if (rootElement != null)
      }
      catch (DocumentException ex) {
        throw new SystemException("XMLParser.parser.DocumentException", ex);
      }

    }

    return packet;
  }
/**
 * 
 * author:解析XML文件中head头
 * description:
 * @param headElement
 * @param packet
 */
  protected static void parserHEAD(Element headElement, Packet packet) {
    if (headElement != null) {
      for (Iterator iter = headElement.elementIterator(); iter.hasNext(); ) {
        Element element = (Element) iter.next();

        String name = element.getName();
        String value = (String) element.getData();

        packet.setHead(name, value);
      }
    }
  }
/**
 * 
 * author:
 * description:解析XML文件中VAR部分
 * @param varElement
 * @param packet
 */
  protected static void parserVAR(Element varElement, Packet packet) {
    if (varElement != null) {
      for (Iterator iter = varElement.elementIterator(); iter.hasNext(); ) {
        Element element = (Element) iter.next();

        String name = element.getName();
        String value = (String) element.getData();

        packet.setVar(name, value);
      }
    }
  }
/**
 * 
 * author:
 * description:解析XML文件中RSS部分
 * @param rssElement
 * @param packet
 */
  protected static void parserRSS(Element rssElement, Packet packet) {
    if (rssElement != null) {
      for (Iterator iter = rssElement.elementIterator(); iter.hasNext(); ) {
        Element element = (Element) iter.next();
        //RS Name
        Attribute attrNameRS = element.attribute(PacketTag.TAG_RS_NAME);
        String rsName = (String) attrNameRS.getData();
        //RS METADATA
        Element metaElement = element.element(PacketTag.TAG_METADATA);
        List metaList = parserRS_METADATA(metaElement);
        //RS ROWDATA
        Element rowElement = element.element(PacketTag.TAG_ROWDATA);
        List rowList = parserRS_ROWDATA(rowElement, metaList);

        packet.setRSMeta(rsName, metaList);
        packet.setRS(rsName, rowList);
      }
    }
  }
/**
 * 
 * author:
 * description:解析XML文件中RSS部分
 * @param metaElement
 * @return
 */
  protected static List parserRS_METADATA(Element metaElement) {
    List metaList = null;

    if (metaElement != null) {
      Element fieldsElement = metaElement.element(PacketTag.TAG_FIELDS);
      if (fieldsElement != null) {
        metaList = new ArrayList();
        for (Iterator fieldIter = fieldsElement.elementIterator();
             fieldIter.hasNext(); ) {
          Element element = (Element) fieldIter.next();
          Map fieldMap = new HashMap();
          for (Iterator attrIter = element.attributeIterator();
               attrIter.hasNext(); ) {
            Attribute fieldAttribute = (Attribute) attrIter.next();
            fieldMap.put(fieldAttribute.getName(), fieldAttribute.getData());
          }
          metaList.add(fieldMap);
        }
      } //if (fieldsElement != null) {
    } //if (metaElement != null) {

    return metaList;
  }
/**
 * 
 * author:
 * description:解析XML文件中RS_ROWDATA部分
 * @param rowElement
 * @param metaList
 * @return
 */
  protected static List parserRS_ROWDATA(Element rowElement, List metaList) {
    List rowList = null;
    if (rowElement != null) {
      Map fieldnameMap = new HashMap();
      for (int i = 0; metaList != null && i < metaList.size(); i++) {
        Map metaMap = (Map) metaList.get(i);
        fieldnameMap.put(metaMap.get(PacketTag.TAG_FIELD_NAME), null);
      }

      rowList = new ArrayList();
      for (Iterator rowIter = rowElement.elementIterator(); rowIter.hasNext(); ) {
        Element element = (Element) rowIter.next();
        Map rowMap = new HashMap();
        for (Iterator attrIter = element.attributeIterator(); attrIter.hasNext(); ) {
          Attribute rowAbbribute = (Attribute) attrIter.next();
          rowMap.put(rowAbbribute.getName(), rowAbbribute.getData());
        }

        for (Iterator iter = fieldnameMap.keySet().iterator(); iter.hasNext(); ) {
          Object fieldname = iter.next();
          if (!rowMap.containsKey(fieldname)) {
            rowMap.put(fieldname, null);
          }
        }
        rowList.add(rowMap);
      }
    }

    return rowList;
  }


}