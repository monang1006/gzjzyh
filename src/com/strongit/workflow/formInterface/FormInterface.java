package com.strongit.workflow.formInterface;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.attachment.IAttachmentService;
import com.strongit.oa.common.eform.IEFormService;
import com.strongit.oa.common.eform.constants.Constants;
import com.strongit.oa.common.eform.model.EForm;
import com.strongit.oa.common.eform.model.EFormField;

/**
 * 
 * @author       喻斌
 * @company      Strongit Ltd. (C) copyright
 * @date         Dec 9, 2008  3:11:25 PM
 * @version      1.0.0.0
 * @classpath    com.strongit.workflow.formInterface.FormInterface
 * @comment		 自定义表单构件调用接口代理
 */
@Service
@Transactional
public class FormInterface implements IFormInterface {
	
	@Autowired IEFormService formService;
	
	/**
	 * added by yubin on 2008.10.18
	 * 由表单ID得到表单路径
	 * @param formId
	 * @return
	 */
	public String getFormpathById(Long formId){
/**		return ((TwfBaseForm) businessManagerDao.findUnique(
				"from TwfBaseForm t where t.tformId = ?", new Object[]{formid})).getTformPath();**/
		return null;
	}
	
	public List getAllFormsList(String formId){
		List list = new ArrayList();
		List<EForm> lstTemplate = formService.getFormTemplateList(Constants.SF);
		if (lstTemplate != null){
			for(EForm eform : lstTemplate){
				Object object = new Object[]{eform.getId().toString(),eform.getTitle()};
				list.add(object);
			}
		}
		return list;	
	}
	
	public List getAllDomainList(String formId){
		List list = new ArrayList();
		List<EFormField> lstField = formService.getFormTemplateFieldList(formId);
		if (lstField != null){
			for(EFormField eform : lstField){
				Object object = new Object[]{eform.getName(),eform.getCaption()};
				list.add(object);
			}
		}
		return list;
	}
	
	public Object getDomainValue(String formId, String domainId, String businessId){
		/*String value = formService.GetFormTemplateFieldValue(new Long(
				formId), domainId, businessId);
		return value;*/
		return formService.getFormTemplateFieldValue(formId, domainId, businessId);
	}

}