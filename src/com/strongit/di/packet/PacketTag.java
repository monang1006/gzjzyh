package com.strongit.di.packet;

/**
 * <p>Title: Strong Data Interchange System</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2007 Jiang Xi Strong Co. Ltd. </p>
 * <p>Company: </p>
 * @author minhongbin@hotmail.com
 * @version 1.0
 */

public class PacketTag {

  //XML数据类型
  public static final String XML_DATA_TYPE_STRING = "STRING";
  public static final String XML_DATA_TYPE_NUMBER = "NUMBER";
  public static final String XML_DATA_TYPE_BLOB = "BLOB";

  public static final String TAG_PID = "PID";
  public static final String TAG_BID = "BID";
  public static final String TAG_SID = "SID";
  public static final String TAG_ERROR_CODE = "ERRCODE";
  public static final String TAG_MESSAGE_CODE = "MESSCODE";
  public static final String TAG_ERROR_MESSAGE = "ERRMESSAGE";
  public static final String SQL_NAME = "SQL-NAME";
  public static final String SQL_SELECT = "SQL-SELECT";
  public static final String SQL_FROM = "SQL-FROM";
  public static final String SQL_WHERE = "SQL-WHERE";
  public static final String SQL_ORDER = "SQL-ORDER";
  public static final String SQL_OFFSET = "SQL-OFFSET";
  public static final String SQL_SIZE = "SQL-SIZE";

  public static final String TAG_PACK = "PACK";
  public static final String TAG_HEAD = "HEAD";
  public static final String TAG_DATA = "DATA";
  public static final String TAG_VAR = "VAR";
  public static final String TAG_RSS = "RSS";
  public static final String TAG_RS = "RS";
  public static final String TAG_METADATA = "METADATA";
  public static final String TAG_FIELDS = "FIELDS";
  public static final String TAG_FIELD = "FIELD";
  public static final String TAG_ROWDATA = "ROWDATA";
  public static final String TAG_ROW = "ROW";

  public static final String TAG_RS_NAME = "NAME";
  public static final String TAG_FIELD_NAME = "NAME";
  public static final String TAG_FIELD_TYPE = "TYPE";
  public static final String TAG_FIELD_WIDTH = "WIDTH";

  public static final String PATH_TAG_HEAD = "//" + TAG_PACK + "/" + TAG_HEAD;
  public static final String PATH_TAG_VAR = "//" + TAG_PACK + "/" + TAG_DATA +
      "/" + TAG_VAR;
  public static final String PATH_TAG_RSS = "//" + TAG_PACK + "/" + TAG_DATA +
      "/" + TAG_RSS;

}