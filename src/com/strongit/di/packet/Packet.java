package com.strongit.di.packet;

import java.util.*;

/**
 * <p>Title: Strong Data Interchange System</p>
 * <p>Description: 数据交换包实体类</p>
 * <p>Copyright: Copyright (c) 2007 Jiang Xi Strong Co. Ltd. </p>
 * <p>Company: </p>
 * @author minhongbin@hotmail.com
 * @version 1.0
 */

public class Packet {

  private Map hmap; //head map，头描述
  private Map vmap; //variable map,变量描述
  private Map rmap; //recordset map,数据集描述
  private Map mmap; //recordset meta map,元数据类型描述

  private void init() {
    hmap = new HashMap();
    vmap = new HashMap();
    rmap = new HashMap();
    mmap = new HashMap();
  }

  public void clear() {
    this.init();
  }

  public Packet() {
    this.init();
  }

  public String getBID() {
    return getHead(PacketTag.TAG_BID);
  }

  public void setBID(String bid) {
    setHead(PacketTag.TAG_BID, bid);
  }

  public String getPID() {
    return getHead(PacketTag.TAG_PID);
  }

  public void setPID(String pid) {
    setHead(PacketTag.TAG_PID, pid);
  }

  public String getSID() {
    return getHead(PacketTag.TAG_SID);
  }

  public void setSID(String sid) {
    setHead(PacketTag.TAG_SID, sid);
  }

  public String getErrCode() {
    return getHead(PacketTag.TAG_MESSAGE_CODE);
  }

  public void setErrCode(String errCode) {
    setHead(PacketTag.TAG_MESSAGE_CODE, errCode);
  }

  public String getErrMessage() {
    return getHead(PacketTag.TAG_ERROR_MESSAGE);
  }

  public void setErrMessage(String errMessage) {
    setHead(PacketTag.TAG_ERROR_MESSAGE, errMessage);
  }

  public void setHead(String name, String value) {
    hmap.put(name, value);
  }

  public String getHead(String name) {
    return (String) hmap.get(name);
  }

  public void setVar(String name, String value) {
    vmap.put(name, value);
  }

  public String getVar(String name) {
    return (String) vmap.get(name);
  }

  public void setRS(String name, List rs) {
    rmap.put(name, rs);
  }

  public List getRS(String name) {
    return (List) rmap.get(name);
  }

  public void setRSMeta(String name, List meta) {
    mmap.put(name, meta);
  }

  public List getRSMeta(String name) {
    return (List) mmap.get(name);
  }

  public Map getRSMap() {
    return rmap;
  }

  public Map getRSMetaMap() {
    return mmap;
  }

  public Map getVarMap() {
    return vmap;
  }

  public Map getHeadMap() {
    return hmap;
  }

}