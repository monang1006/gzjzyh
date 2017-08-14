<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<%@ page language="java"  contentType="text/html" import="java.sql.*" import="java.util.*" import="java.io.*"  pageEncoding="utf-8"%>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
		<title></title>
		
	</head>

	<body>
		<%				
		OutputStream ops = null;
		InputStream ips = null;
		String key = (String)request.getParameter("key");
		Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();   
		String url= "jdbc:jtds:sqlserver://14.0.0.3:1433;DatabaseName=dasep";    
		String user= "wj";   
		String password= "wj123";  
				 
		Connection conn = DriverManager.getConnection(url,user,password);   
		Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);   
		String sql = "select t.FWHDM,t.FBT,t.FWH,t.FROLE,t.FSEP from DAWJSEP as t where t.FWHDM = " + "'" + key + "'";   
		ResultSet rs=stmt.executeQuery(sql); 
		
		String userName = "";
			if(request.getSession().getAttribute("userName") != null){				
				userName = request.getSession().getAttribute("userName").toString();				
			}
		
		try{	
		out.clear();
			Boolean flag = false;
			if(rs.next()){
				flag = true;
				do{
					response.reset();
					String name = rs.getString(2);
					String num = rs.getString(3);
					String role = Integer.toString(rs.getInt(4));
					
					if((num.contains("赣府文") || role.equals("7")) && "".equals(userName)){
						String info = "您没有读取此文件的权限!";
						out.write(new String(info.getBytes("gb2312"),"iso8859-1"));
						
					}else{					
						response.setContentType("application/gd"); 
						response.addHeader("Content-Disposition", "attachment;filename=" +
						         new String(name.getBytes("gb2312"),"iso8859-1") + ".gd");
						ops = response.getOutputStream();
						ips = rs.getBinaryStream(5);
						byte b[] = new byte[1024];
						int n=0;					
						while((n=ips.read(b))!=-1){
	       					ops.write(b,0,n);
	   					}
						ops.flush();
					}
					
				}while(rs.next());
			
			}
			if(flag = false){
				String str = "该文件无版式文件";
				out.write(new String(str.getBytes("gb2312"),"iso8859-1"));
			}
			
	out=pageContext.pushBody();	
		}catch(Exception e){
			e.printStackTrace();
		}finally{
		
			if(ips != null){
				ips.close();
				ips = null;
			}
			
			if(ops != null){
				ops.close();
				ops = null;
			}
			
			if(rs != null){
				rs.close();
				rs = null;
			}
			if(stmt != null){
				stmt.close();
				stmt = null;
			}
			if(conn != null){
				conn.close();
				conn = null;
			}		
		}  
		%> 

	</body>
</html>
