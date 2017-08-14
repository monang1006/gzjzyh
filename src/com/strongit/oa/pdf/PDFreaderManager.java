package com.strongit.oa.pdf;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oracle.jdbc.driver.OracleResultSet;
import oracle.sql.BLOB;

import com.strongit.oa.common.service.BaseManager;
import com.strongit.oa.prsnfldr.util.FileUtil;
import com.strongit.oa.util.PathUtil;


public class PDFreaderManager extends BaseManager {
	
	  private int mFileSize;
	  private byte[] mFileBody;
	  private String mFileName;
	  private String mFileType;
	  private String mFileDate;
	  private String mFileID;

	  private String mRecordID;
	  private String mTemplate;
	  private String mDateTime;
	  private String mOption;
	  private String mMarkName;
	  private String mPassword;
	  private String mMarkList;
	  private String mBookmark;
	  private String mDescript;
	  private String mHostName;
	  private String mMarkGuid;
	  private String mCommand;
	  private String mContent;
	  private String mHtmlName;
	  private String mDirectory;
	  private String mFilePath;

	  private String mUserName;
	  private int mColumns;
	  private int mCells;
	  private String mMyDefine1;
	  private String mLocalFile;
	  private String mRemoteFile;
	  private String mLabelName;
	  private String mImageName;
	  private String mTableContent;

	  private String Sql;

	  //打印控制
	  private String mOfficePrints;
	  private int mCopies;

	  //自定义信息传递
	  private String mInfo;

	  private DBstep.iMsgServer2000 MsgObj;
	  private static com.strongit.oa.pdf.iDBManager2000 DbaObj = new iDBManager2000();
	  
	 //--------------------Bolb字段处理.开始------------------------------//
	 private void PutAtBlob(BLOB vField, int vSize) throws IOException {
	    try {
	      OutputStream outstream = vField.getBinaryOutputStream();
	      outstream.write(mFileBody, 0, vSize);
	      outstream.close();
	    }
	    catch (Exception e) {
	    	System.out.println("\n\n\n\n\n\n\n生成附件失败：" + e.getMessage());
	    	e.printStackTrace();
	    }
	  }

	  private void GetAtBlob(BLOB vField, int vSize) throws IOException {
	    try {
	      mFileBody = new byte[vSize];//42495 new byte[vSize];
	      InputStream instream = vField.getBinaryStream();
	      instream.read(mFileBody, 0, vSize);
	      instream.close();
	    }
	    catch (SQLException e) {

	    }
	  }
	  /**
	   * PDF改造直接从服务器上读取文件显示
	   * @date 2014年12月23日14:45:55
	   * @param vField
	   * @param vSize
	   * @throws IOException
	   */
	  private void GetAtBlobByName(String  pdfName, int vSize) throws IOException {
		  String rootPath = PathUtil.getRootPath();// 得到工程根路径
		  String uploadDir = "pdfFile";
		  String dir = rootPath + uploadDir;
		  InputStream instream =  new FileInputStream(dir + File.separatorChar + pdfName);
		   int t =   instream.available();
		   mFileBody = new byte[t];//42495 new byte[vSize];
		   try{
		  instream.read(mFileBody, 0, t);
		   }catch (Exception e) {
			// TODO: handle exception
			   e.printStackTrace();
		}
		  instream.close();
	  }
	  //--------------------Bolb字段处理.结束------------------------------//

	  // ************* 文档、模板管理代码    开始  *******************************

	  //调出文档，将文档内容保存在mFileBody里，以便进行打包
	  private boolean LoadFile() {
	    boolean mResult = false;
	    //String Sql = "SELECT ATTACH_CON,ATTACH_SIZE FROM t_oa_attachment WHERE ATTACH_ID='" + mRecordID + "'";
	    
	    
	    String[] data = mRecordID.split(";");
	    if(data.length<4){
	    	mResult = false;
	    	return (mResult);
	    }
	    
		String tableName = data[0];
	    String pkFieldName = data[1];
	    String pkFieldValue = data[2];
	    String PDFCONTENT = data[3];
	    String PDFCONTENT_SIZE = data[4];
	    
	    String Sql = "SELECT "+"ADOBE_PDF_NAME"+","+PDFCONTENT_SIZE+" FROM "+tableName+" WHERE "+pkFieldName+"='" + pkFieldValue + "'";
	    try {
	      if (DbaObj.OpenConnection()) {
	        try {
	          ResultSet result = DbaObj.ExecuteQuery(Sql);
	          if (result.next()) {
	            try {
	            mFileSize=new Integer(result.getString(PDFCONTENT_SIZE));
	            String ADOBE_PDF_NAME=result.getString("ADOBE_PDF_NAME");
	            //GetAtBlob(((OracleResultSet)result).getBLOB("ATTACH_CON"),mFileSize);
	            	if(ADOBE_PDF_NAME!=null && !"".equals(ADOBE_PDF_NAME)){
	            		GetAtBlobByName(ADOBE_PDF_NAME,mFileSize);
	            		mResult = true ;
	            	}else{
	            		mResult = false;
	            	}
	            }
	            catch (IOException ex) {
	              System.out.println(ex.toString());
	            }
	          }
	          result.close();
	        }
	        catch (SQLException e) {
	          System.out.println(e.getMessage());
	          mResult = false;
	        }
	      }
	    }
	    finally {
	      DbaObj.CloseConnection();
	    }
	    return (mResult);
	  }
	  /**
		 * @author:niwy
		 * @description:  获取业务表中的pdf名字，用于更新工程路径下面的pdf文件
		 * @date : 2011-12-18
		 * @modifyer:
		 * @description:
		 * @param vSize
		 * @return
		 */
		private String LoadDBFileName() {
			String[] data = mRecordID.split(";");
			String tableName = data[0];
			String pkFieldName = data[1];
			String pkFieldValue = data[2];
			String pdfName = "";
			String ADOBE_PDF_NAME  = "ADOBE_PDF_NAME";
			String Sql = "SELECT  " + ADOBE_PDF_NAME + " FROM "
					+ tableName + " WHERE " + pkFieldName + "='" + pkFieldValue
					+ "'";
			String fileName ="";
			//	    super.getFieldValue(fieldName, tableName, pkFieldName, pkFieldValue)
			try {
				if (DbaObj.OpenConnection()) {
					try {
						ResultSet result = DbaObj.ExecuteQuery(Sql);
						if (result.next()) {
							fileName = result.getString(ADOBE_PDF_NAME);
						}
						result.close();
					} catch (SQLException e) {
						System.out.println(e.getMessage());
					}
				}
			} finally {
				DbaObj.CloseConnection();
			}
			return fileName;
		}
	  //保存文档，如果文档存在，则覆盖，不存在，则添加
	  private boolean SaveFile() {
	    boolean mResult = false;
	    int iFileId = -1;

	    String[] data = mRecordID.split(";");
		String tableName = data[0];
	    String pkFieldName = data[1];
	    String pkFieldValue = data[2];
	    
	    String Sql = "SELECT * FROM "+tableName+" WHERE "+pkFieldName+"='" + pkFieldValue + "'";
	    try {
	      if (DbaObj.OpenConnection()) {
	        try {
	          ResultSet result = DbaObj.ExecuteQuery(Sql);
	          if (result.next()) {
	            Sql="update "+tableName+" set PDFCONTENT_SIZE=?,PDFCONTENT=EMPTY_BLOB() WHERE "+pkFieldName+"='" + pkFieldValue + "'";
	          }
	          else {
	            Sql="insert into "+tableName+" ( "+pkFieldName+",PDFCONTENT_SIZE,PDFCONTENT ) values (?,?,EMPTY_BLOB())";
	            iFileId=DbaObj.GetMaxID(tableName,pkFieldName);
	          }
	          result.close();
	        }
	        catch (SQLException e) {
	          System.out.println(e.toString());
	          mResult = false;
	        }

	        java.sql.PreparedStatement prestmt=null;
	        try {
	          prestmt =DbaObj.Conn.prepareStatement(Sql);
	          prestmt.setString(1,""+mFileSize);
	          DbaObj.Conn.setAutoCommit(false);
	          prestmt.execute();
	          prestmt.close();
	          java.sql.Statement stmt=null;
	          stmt = DbaObj.Conn.createStatement();
	          OracleResultSet update=(OracleResultSet)stmt.executeQuery("select PDFCONTENT from "+tableName+" where "+pkFieldName+"='" + pkFieldValue+ "' for update");
	          if (update.next()){
	            try{
	              PutAtBlob(((oracle.jdbc.OracleResultSet)update).getBLOB("PDFCONTENT"),mFileSize);
	            }
	            catch (IOException e) {
	              System.out.println(e.toString());
	              mResult = false;
	            }
	          }
	          update.close();
	          stmt.close();

	          DbaObj.Conn.commit();
	          DbaObj.Conn.setAutoCommit(true);
	          mFileBody=null;
	          mResult=true;
	        }
	        catch (SQLException e) {
	          System.out.println(e.toString());
	          mResult = false;
	        }
	      }
	    }
	    finally {
	      DbaObj.CloseConnection();
	    }
	    return (mResult);
	  }
	  
	  
	/**
	 * @author:luosy
	 * @description:  加载数据库中的pdf附件
	 * @date : 2011-12-18
	 * @modifyer:
	 * @description:
	 * @param vSize
	 * @return
	 */
	  @Deprecated
	private boolean LoadDBFile() {
	    boolean mResult = false;
	    
	    String[] data = mRecordID.split(";");
		String tableName = data[0];
	    String pkFieldName = data[1];
	    String pkFieldValue = data[2];
	    String PDFCONTENT = data[3];
	    String PDFCONTENT_SIZE = data[4];
	    
	    String Sql = "SELECT "+PDFCONTENT+","+PDFCONTENT_SIZE+" FROM "+tableName+" WHERE "+pkFieldName+"='" + pkFieldValue + "'";
	    
//	    super.getFieldValue(fieldName, tableName, pkFieldName, pkFieldValue)
	    
		try {
			Blob vField = (Blob)getFieldValue(PDFCONTENT, tableName, pkFieldName, pkFieldValue);
			String pdfsize = (String)getFieldValue(PDFCONTENT_SIZE, tableName, pkFieldName, pkFieldValue);
			mFileSize=new Integer(pdfsize);
			mFileBody = new byte[mFileSize];//42495 new byte[vSize];
			InputStream instream = vField.getBinaryStream();
			instream.read(mFileBody, 0, mFileSize);
			instream.close();
			mResult = true ;
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			mResult = false;
		}
	    return (mResult);
	  }
	/**
	 * 保存pdf
	 * @param fileName
	 * @param vSize
	 * @return
	 * @throws Exception
	 */
	  public boolean SaveFileForPdf(String fileName ,int vSize) throws Exception{
			 if (mFileBody != null) {
					InputStream is = FileUtil.ByteArray2InputStream(mFileBody);
					if(!"".equals(mRecordID)){
						fileName = PdfTempFileHelper.savePdfFile(mFilePath, is,
								fileName);
					}
				}
			return true;
		 }
	 /**
	 * @author:luosy
	 * @description:	将文件保存到服务器的临时文件夹
	 * @date : 2011-12-18
	 * @modifyer:
	 * @description:
	 * @param path
	 * @return
	 */
	private boolean SaveFileTemppath(int vSize) {
		try {
		if(mFileBody != null) {
   			InputStream is = FileUtil.ByteArray2InputStream(mFileBody);
   			String fileName = PdfTempFileHelper.saveFile(mFilePath, is, mRecordID+";"+vSize);
   			MsgObj.SetMsgByName("fileName", fileName);		
   			System.out.println("\n\n\n\n\n生成附件：" + fileName);
   		}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	  }

	  /**
	 * @author:luosy
	 * @description:
	 * @date : 2011-12-18
	 * @modifyer:
	 * @description:
	 * @param recordID  pdfinfo 
	 * @param fileSize 
	 * @param is
	 * @return
	 */
	public boolean SaveFiletoBD(String recordID,int fileSize,InputStream is) {
		    boolean mResult = false;
		    int iFileId = -1;

		    String[] data = recordID.split(";");
			String tableName = data[0];
		    String pkFieldName = data[1];
		    String pkFieldValue = data[2];
		    String PDFCONTENT = data[3];
		    String PDFCONTENT_SIZE = data[4];
		    
		    String Sql = "SELECT * FROM "+tableName+" WHERE "+pkFieldName+"='" + pkFieldValue + "'";
		    try {
		      if (DbaObj.OpenConnection()) {
		        try {
		          ResultSet result = DbaObj.ExecuteQuery(Sql);
		          if (result.next()) {
		        	  Sql="update "+tableName+" set "+PDFCONTENT_SIZE+"=?,"+PDFCONTENT+"=EMPTY_BLOB() WHERE "+pkFieldName+"='" + pkFieldValue + "'";
		          }
		          else {
		        	  mResult = false;
			          System.out.println("没有找到PDF文件对应的数据");
		        	  return (mResult);
		          }
		          result.close();
		        }
		        catch (Exception e) {
		          System.out.println(e.toString());
		          mResult = false;
		        }

		        java.sql.PreparedStatement prestmt=null;
		        try {
			          prestmt =DbaObj.Conn.prepareStatement(Sql);
			          prestmt.setInt(1,fileSize);
			          DbaObj.Conn.setAutoCommit(false);
			          prestmt.execute();
			          prestmt.close();
			          java.sql.Statement stmt=null;
			          stmt = DbaObj.Conn.createStatement();
			          OracleResultSet update=(OracleResultSet)stmt.executeQuery("select "+PDFCONTENT+" from "+tableName+" where "+pkFieldName+"='" + pkFieldValue+ "' for update");
			          if (update.next()){
			            try{
			              PutAtBlob(((oracle.jdbc.OracleResultSet)update).getBLOB(PDFCONTENT),fileSize);
			            }
			            catch (IOException e) {
			              System.out.println(e.toString());
			              mResult = false;
			            }
			          }
			          update.close();
			          stmt.close();

			          DbaObj.Conn.commit();
			          DbaObj.Conn.setAutoCommit(true);
			          mFileBody=null;
			          mResult=true;
			        }catch (Exception e) {
		          System.out.println(e.toString());
		          e.printStackTrace();
		          mResult = false;
		        }
		      }
		    }
		    finally {
		      DbaObj.CloseConnection();
		    }
		    return (mResult);
		  }
	
//	 *************接收流、写回流代码    开始  *******************************
//	取得客户端发来的数据包
	  private byte[] ReadPackage(HttpServletRequest request) {
	    byte mStream[] = null;
	    int totalRead = 0;
	    int readBytes = 0;
	    int totalBytes = 0;
	    try {
	      totalBytes = request.getContentLength();
	      mStream = new byte[totalBytes];
	      while (totalRead < totalBytes) {
	        request.getInputStream();
	        readBytes = request.getInputStream().read(mStream, totalRead,
	                                                  totalBytes - totalRead);
	        totalRead += readBytes;
	        continue;
	      }
	    }
	    catch (Exception e) {
	      System.out.println(e.toString());
	    }
	    return (mStream);
	  }

//	发送处理后的数据包
	  private void SendPackage(HttpServletResponse response) {
	    try {
	      ServletOutputStream OutBinarry = response.getOutputStream();
	      OutBinarry.write(MsgObj.MsgVariant());
	      OutBinarry.flush();
	      OutBinarry.close();
	    }
	    catch (IOException e) {
	      System.out.println(e.toString());
	    }
	  }

//	 *************接收流、写回流代码    结束  *******************************

	  public void ExecuteRun(HttpServletRequest request,
	                         HttpServletResponse response) {
//	    DbaObj = new iDBManager2000(); //创建数据库对象
	    MsgObj = new DBstep.iMsgServer2000(); //创建信息包对象

	    mOption = "";
	    mRecordID = "";
	    mTemplate = "";
	    mFileBody = null;
	    mFileName = "";
	    mFileType = "";
	    mFileSize = 0;
	    mFileID = "";
	    mDateTime = "";
	    mMarkName = "";
	    mPassword = "";
	    mMarkList = "";
	    mBookmark = "";
	    mMarkGuid = "";
	    mDescript = "";
	    mCommand = "";
	    mContent = "";
	    mLabelName = "";
	    mImageName = "";
	    mTableContent = "";
	    mMyDefine1 = "";

	    mFilePath = request.getSession().getServletContext().getRealPath("");	//取得服务器路径

	    System.out.println("request.getSession().getServletContext().getRealPath:"+mFilePath);

	    try {
	      if (request.getMethod().equalsIgnoreCase("POST")) {
	        //MsgObj.MsgVariant(ReadPackage(request));                              //老版本后台类解析数据包方式（新版控件也兼容）
	        MsgObj.Load(request);                                                   //8.1.0.2版后台类新增解析接口，效率更高

	        if (MsgObj.GetMsgByName("DBSTEP").equalsIgnoreCase("DBSTEP")) {		//如果是合法的信息包
	          mOption = MsgObj.GetMsgByName("OPTION");				//取得操作信息
	          mUserName = MsgObj.GetMsgByName("USERNAME");				//取得系统用户
	          System.out.println(mOption);						//打印出调试信息

	          if (mOption.equalsIgnoreCase("LOADFILE")) {				//下面的代码为打开服务器数据库里的文件
	            mRecordID = MsgObj.GetMsgByName("RECORDID");			//取得文档编号
	            mFileName = MsgObj.GetMsgByName("FILENAME");			//取得文档名称
	            mFileType = MsgObj.GetMsgByName("FILETYPE");			//取得文档类型
	            MsgObj.MsgTextClear();						//清除文本信息
	            //if (MsgObj.MsgFileLoad(mFilePath+"\\"+mFileName))			//从文件夹调入文档
//	            if (LoadDBFile()) {							//从数据库调入pdf文档 by luosy jdbc?
	            if (LoadFile()) {							//从数据库调入文档
	              MsgObj.MsgFileBody(mFileBody);					//将文件信息打包
	              MsgObj.SetMsgByName("STATUS", "打开成功!");			//设置状态信息
	              MsgObj.MsgError("");						//清除错误信息
	            }
	            else {
	              MsgObj.MsgError("打开失败!");					//设置错误信息
	            }
	          }

	          else if (mOption.equalsIgnoreCase("SAVEFILE")) {			//下面的代码为保存文件在服务器的数据库里
	        	    mRecordID = MsgObj.GetMsgByName("RECORDID"); //取得文档编号
					mFileName = MsgObj.GetMsgByName("FILENAME"); //取得文档名称
					mFileType = MsgObj.GetMsgByName("FILETYPE"); //取得文档类型
					//mMyDefine1=MsgObj.GetMsgByName("MyDefine1");			//取得客户端传递变量值 MyDefine1="自定义变量值1"
					mFileSize = MsgObj.MsgFileSize(); //取得文档大小
					mFileDate = DbaObj.GetDateTime(); //取得文档时间
					mFileBody = MsgObj.MsgFileBody(); //取得文档内容
					//mFilePath = "";							//如果保存为文件，则填写文件路径
					mUserName = mUserName; //取得保存用户名称
					mDescript = "通用版本"; //版本说明
					MsgObj.MsgTextClear(); //清除文本信息
					MsgObj.MsgFileClear(); //清除文档内容

					//if (MsgObj.MsgFileSave(mFilePath+"\\"+mRecordID+";"+mFileSize)){			//保存文档内容到文件夹中
					//if (SaveFile()) {							//保存文档内容到数据库中
			     	 String pdfName = LoadDBFileName();
				//	 String pdfName = "141333370027641331.pdf";
					if (SaveFileTemppath(mFileSize)) { //保存文档内容到工程下的PDF临时文件夹 by luosy
						SaveFileForPdf(pdfName,mFileSize);
						MsgObj.SetMsgByName("STATUS", "保存成功!"); //设置状态信息
						MsgObj.MsgError(""); //清除错误信息
					} else {
						MsgObj.MsgError("保存失败!"); //设置错误信息
					}
	          }

	          else if (mOption.equalsIgnoreCase("INSERTFILE")) {			//下面的代码为插入文件
	            mRecordID = MsgObj.GetMsgByName("RECORDID");			//取得文档编号
	            mFileName = MsgObj.GetMsgByName("FILENAME");			//取得文档名称
	            mFileType = MsgObj.GetMsgByName("FILETYPE");			//取得文档类型
	            MsgObj.MsgTextClear();
	            if (LoadFile()) {							//调入文档
	              MsgObj.MsgFileBody(mFileBody);					//将文件信息打包
	              MsgObj.SetMsgByName("POSITION", "Content");			//设置插入的位置[书签]
	              MsgObj.SetMsgByName("STATUS", "插入文件成功!");			//设置状态信息
	              MsgObj.MsgError("");						//清除错误信息
	            }
	            else {
	              MsgObj.MsgError("插入文件成功!");					//设置错误信息
	            }
	          }

	          else if (mOption.equalsIgnoreCase("DATETIME")) {			//下面的代码为请求取得服务器时间
	            MsgObj.MsgTextClear();						//清除文本信息
	            MsgObj.SetMsgByName("DATETIME", DbaObj.GetDateTime());		//标准日期格式字串，如 2005-8-16 10:20:35
	            //MsgObj.SetMsgByName("DATETIME","2006-01-01 10:24:24");		//标准日期格式字串，如 2005-8-16 10:20:35
	          }

	          else if (mOption.equalsIgnoreCase("SENDMESSAGE")) {			//下面的代码为Web页面请求信息[扩展接口]
	            mRecordID = MsgObj.GetMsgByName("RECORDID");			//取得文档编号
	            mFileName = MsgObj.GetMsgByName("FILENAME");			//取得文档名称
	            mFileType = MsgObj.GetMsgByName("FILETYPE");			//取得文档类型
	            mCommand = MsgObj.GetMsgByName("COMMAND");				//取得操作类型 InportText or ExportText
	            mContent = MsgObj.GetMsgByName("CONTENT");				//取得文本信息 Content
	            mOfficePrints = MsgObj.GetMsgByName("OFFICEPRINTS");		//取得Office文档的打印次数
	            mInfo = MsgObj.GetMsgByName("TESTINFO");				//取得客户端传来的自定义信息

	            MsgObj.MsgTextClear();
	            MsgObj.MsgFileClear();
	            System.out.println("COMMAND:"+mCommand);

	            if (mCommand.equalsIgnoreCase("SELFINFO")) {
	              mInfo = "服务器端收到客户端传来的信息：“" + mInfo + "” | ";
	              //组合返回给客户端的信息
	              mInfo = mInfo + "服务器端发回当前服务器时间：" + DbaObj.GetDateTime();
	              MsgObj.SetMsgByName("RETURNINFO", mInfo);				//将返回的信息设置到信息包中
	            }
	            else {
	              MsgObj.MsgError("客户端Web发送数据包命令没有合适的处理函数![" + mCommand + "]");
	              MsgObj.MsgTextClear();
	              MsgObj.MsgFileClear();
	            }
	          }
	        }
	        else {
	          MsgObj.MsgError("客户端发送数据包错误!");
	          MsgObj.MsgTextClear();
	          MsgObj.MsgFileClear();
	        }
	      }
	      else {
	        MsgObj.MsgError("请使用Post方法");
	        MsgObj.MsgTextClear();
	        MsgObj.MsgFileClear();
	      }
	      System.out.println("SendPackage");
	      System.out.println("");
	      //SendPackage(response);                                                  //老版后台类返回信息包数据方法（新版控件也兼容）
	      MsgObj.Send(response);                                                    //8.1.0.2新版后台类新增的功能接口，效率更高

	    }
	    catch (Exception e) {
	    	e.printStackTrace();
	      System.out.println(e.toString());
	    }
	  }
}
