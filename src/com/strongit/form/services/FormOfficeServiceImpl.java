/**  
* @title: FormOfficeServiceImpl.java
* @package com.strongit.form.services
* @description: TODO
* @author  hecj
* @date Jul 7, 2014 9:17:52 AM
*/


package com.strongit.form.services;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


import org.apache.poi.hwpf.extractor.WordExtractor;
import org.springframework.stereotype.Service;

import com.strongit.form.services.FormOfficeService;
import com.strongit.form.utils.InputStreamUtils;
import com.strongmvc.exception.SystemException;
/**
 * @classname: FormOfficeServiceImpl	
 * @author hecj
 * @date Jul 7, 2014 9:17:52 AM
 * @version
 * @company STRONG CO.,LTD.
 * @package com.strongit.form.services
 * @update
 */
@Service
public class FormOfficeServiceImpl implements FormOfficeService{

	/**
	 * Overriding method
	 * @description
	 *
	 * @author  hecj
	 * @date    Jul 7, 2014 9:21:54 AM
	 * @param arg0
	 * @param arg1
	 * @return
	 * @throws SystemException
	 * @return  
	 * @throws
	 */
	public Boolean CheckOfficeValid(byte[] wordIn, String fileType){
		// TODO Auto-generated method stub
		FileInputStream fileIn=null;
		InputStream in=null;
		boolean officeValid=false;
		try{
			in=InputStreamUtils.byteToInputStream(wordIn);
			String wordText="";
			if("docx".equals(fileType)){
				
			}
			else{
				WordExtractor word = new WordExtractor(in);
				wordText = word.getText();				
			}
			if(wordText.length()>0){
				officeValid=true;
			}
		}catch(IOException e){
			
		}catch(Exception ex){
			
		}
		return officeValid;
	}
}
