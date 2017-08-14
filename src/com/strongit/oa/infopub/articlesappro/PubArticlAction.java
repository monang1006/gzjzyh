package com.strongit.oa.infopub.articlesappro;

import java.util.List;

import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.exe.ExecutionContext;

import com.strongit.oa.bo.ToaInfopublishColumnArticl;
import com.strongit.oa.infopub.articles.ArticlesManager;
import com.strongmvc.service.ServiceLocator;

/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-2-24 下午04:09:26
 * Autour: lanlc
 * Version: V1.0
 * Description：信息发布自动节点设置文章为已发布状态
 */
public class PubArticlAction implements ActionHandler {
	private ArticlesManager service = null;
	private ArticlesManager getService(){
		if(service == null){
			service = (ArticlesManager)ServiceLocator.getService("articlesManager");
		}
		return service;
	}
	
	public void execute(ExecutionContext arg0) throws Exception {
		long instanceId = arg0.getContextInstance().getProcessInstance().getId();
		String processInstanceId = String.valueOf(instanceId);
		List<Object[]> ret = getService().getFormIdAndBusinessIdByProcessInstanceId(processInstanceId);
		Object[] obj = ret.get(0);
		String bussinessId = obj[1].toString();
		String columnArticleId = bussinessId;
		ToaInfopublishColumnArticl ca = this.getService().getColumnArticl(columnArticleId);
		ca.setColumnArticleState("5");//将状态置为已发布
		service.updataColumnArtile(ca);
		arg0.getToken().unlock("token[" + arg0.getToken().getId() + "]");
		arg0.getToken().signal();
	}



}
