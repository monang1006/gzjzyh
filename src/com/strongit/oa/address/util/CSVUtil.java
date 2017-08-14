package com.strongit.oa.address.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

import com.strongit.oa.bo.ToaAddress;
import com.strongit.oa.bo.ToaAddressMail;

public class CSVUtil {

	/**
	 * author:dengzc description:从通讯录中导出CSV格式 modifyer: description:
	 * 
	 * @param args
	 */
	
	public static void exportCSV(String[] header,HttpServletResponse response,
			List<ToaAddress> listAddress,String ext,String groupName) {
		CSVWriter writer = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<String> list = new ArrayList<String>();
		if(null == ext || "".equals(ext)){ 
			ext = "csv";
		}
		if(groupName == null || "".equals(groupName)){
			groupName = "通讯录";
		}
		
		String fileName = groupName+"."+ext;
		
		response.reset();
		response.setContentType("application/csv");
	//	response.setHeader("Content-Disposition", "inline; filename="+fileName);

		try {
			response.addHeader("Content-Disposition", "attachment;filename=" +
					new String(fileName.getBytes("gb2312"),"iso8859-1"));
			writer = new CSVWriter(new PrintWriter(new OutputStreamWriter(response.getOutputStream(),"gb2312")),',');
			writer.writeNext(header);
			for (int j=0;j<listAddress.size();j++) {
				ToaAddress oaaddress = listAddress.get(j);
				for (int i = 0; i < header.length; i++) {
					if ("姓名".equals(header[i])) {
						list.add(oaaddress.getName());
					} else if ("职务".equals(header[i])) {
						list.add(oaaddress.getNickname());
					} else if ("电子邮件".equals(header[i])) {
						//list.add(oaaddress.getDefaultEmail());
						Set<ToaAddressMail> mailSet = oaaddress.getToaAddressMails();
						StringBuilder mailStr = new StringBuilder();
						for(ToaAddressMail mail : mailSet){
							mailStr.append(mail.getMail() == null ? "" : mail.getMail()).append("\n");
						}
						list.add(mailStr.toString());
					} else if ("手机1".equals(header[i])) {
						list.add(oaaddress.getMobile1() == null ? "\t" : "\t"+oaaddress.getMobile1());
					} else if ("手机2".equals(header[i])) {
						list.add(oaaddress.getMobile2() == null ? "\t" : "\t"+oaaddress.getMobile2());
					} else if ("性别".equals(header[i])) {
						if ("1".equals(oaaddress.getSex())) {
							list.add("男");
						} else {
							list.add("女");
						}
					} else if ("生日".equals(header[i])) {
						if(null!=oaaddress.getBirthday()){
							list.add("\t"+sdf.format(oaaddress.getBirthday()));
						}else{
							list.add("");
						}
					} else if ("QQ".equals(header[i])) {
						list.add(oaaddress.getQq());
					} else if ("MSN".equals(header[i])) {
						list.add(oaaddress.getMsn());
					} else if ("主页".equals(header[i])) {
						list.add(oaaddress.getHomepage());
					} else if ("爱好".equals(header[i])) {
						list.add(oaaddress.getLikeThing());
					} else if ("国家".equals(header[i])) {
						list.add(oaaddress.getCountry());
					} else if ("家庭电话1".equals(header[i])) {
						list.add(oaaddress.getTel1());
					} else if ("家庭电话2".equals(header[i])) {
						list.add(oaaddress.getTel2());
					} else if ("省".equals(header[i])) {
						list.add(oaaddress.getProvince());
					} else if ("城市".equals(header[i])) {
						list.add(oaaddress.getCity());
					} else if ("传真".equals(header[i])) {
						list.add(oaaddress.getFax());
					} else if ("家庭地址".equals(header[i])) {
						list.add(oaaddress.getAddress());
					} else if ("公司".equals(header[i])) {
						list.add(oaaddress.getCompany());
					} else if ("职位".equals(header[i])) {
						list.add(oaaddress.getPosition());
					} else if ("部门".equals(header[i])) {
						list.add(oaaddress.getDepartment());
					} else if ("公司电话1".equals(header[i])) {
						list.add(oaaddress.getCoTel1());
					} else if ("公司电话2".equals(header[i])) {
						list.add(oaaddress.getCoTel2());
					} else if ("邮编".equals(header[i])) {
						list.add(oaaddress.getZipcode());
					} else if ("附注信息".equals(header[i])) {
						list.add(oaaddress.getAddressRemark());
					}
				}
				String[] store = list.toArray(new String[list.size()]);
				list.clear();
				writer.flush();
				writer.writeNext(store);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				writer = null;
			}
		}
	}

	public static String importCSV(File file)throws Exception{
		CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(file),"GBK"));
//		CSVReader reader = new CSVReader(new FileReader(file));
		String[] line;
        String readline = "";
        for (int row = 0; (line = reader.readNext()) != null; row++) {
            for (int col = 0; col < line.length; col++) {
                readline = readline + line[col] + (col != line.length - 1 ? "," : "Γ");
            }
        }
        if(readline.length()>0){
        	readline = readline.substring(0,readline.length()-1);
        }
        reader.close();
        return readline;
	}
	
	public static void main(String[] args) throws Exception{
		String read = importCSV(new File("f:\\temp\\t.txt"));
		System.out.println(read);
		System.out.println(System.getProperty( "file.encoding"));
	}

}
