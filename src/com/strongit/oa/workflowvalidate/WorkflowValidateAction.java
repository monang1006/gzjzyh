package com.strongit.oa.workflowvalidate;

import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.TEFormTemplate;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.Position;
import com.strongit.oa.eformManager.EformManagerManager;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongit.uums.bo.TUumsBasePostOrg;
import com.strongit.uums.bo.TUumsBaseUser;
import com.strongit.workflow.bo.TwfBaseProcessfile;
import com.strongit.workflow.workflowInterface.IProcessDefinitionService;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * 业务验证
 * @author zhong
 *
 */
public class WorkflowValidateAction extends BaseActionSupport
{
	
	private String strActor;
	
	@Autowired
	private IUserService userSrv;
	
	@Autowired
	private IProcessDefinitionService pdSrv;
	
	@Autowired
	private EformManagerManager eformSrv;

	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String input() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String list() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void prepareModel() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String save() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	/** 
	 * 动态处理人验证
	 * @return
	 * @throws Exception
	 */
	public String actorValidate() throws Exception
	{
		if(StringUtils.isNotEmpty(strActor))
		{
			strActor = strActor.replace("|", ",");
			String[] actors = strActor.split(",");

			StringBuilder errors = new StringBuilder();
			
			for(int i=0;i<actors.length;i=i+2)
			{
				String idType = actors[i];
				String type = idType.substring(0, 1);
				String id = idType.substring(1);
				String name = actors[i+1];
				
				if("u".equalsIgnoreCase(type))
				{
					String[] ids = id.split("\\$");
					String uid = ids[0];
					String oid = "";
					if(ids.length == 2)
					{
						oid = ids[1];
					}
					TUumsBaseUser user = userSrv.getUumsUserInfoByUserId(uid);
					if(user == null)
					{
						errors.append("用户：");
						errors.append(name);
						errors.append("(");
						errors.append(uid);
						errors.append(") 不存在。\r\n\r\n");
					}
					else
					{
						if(!name.equals(user.getUserName()))
						{
							errors.append("用户：");
							errors.append(name);
							errors.append("(");
							errors.append(uid);
							errors.append(") 已经更改姓名。\r\n\r\n");
						}
						if(StringUtils.isNotEmpty(oid) && !oid.equals(user.getOrgId()))
						{
							errors.append("用户：");
							errors.append(name);
							errors.append("(");
							errors.append(uid);
							errors.append(") 已经更改机构。\r\n\r\n");
//							errors.append(") 已经不属于机构(");
//							errors.append(oid);
//							errors.append(")。\r\n\r\n");
						}
					}
				}
				
				if("o".equalsIgnoreCase(type))
				{
					TUumsBaseOrg org =userSrv.getOrgInfoByOrgId(id);
					if(org == null)
					{
						errors.append("机构：");
						errors.append(name);
						errors.append("(");
						errors.append(id);
						errors.append(") 不存在。\r\n\r\n");
					}
					else
					{
						if(!name.equals(org.getOrgName()))
						{
							errors.append("机构：");
							errors.append(name);
							errors.append("(");
							errors.append(id);
							errors.append(") 名称已经更改。\r\n\r\n");
						}
					}
				}
				
				if("p".equalsIgnoreCase(type))
				{
					String[] ids = id.split("\\$");
					String pid = ids[0];
					String oid = "";
					if(ids.length == 2)
					{
						oid = ids[1];
					}
					Position post = userSrv.getPostInfoByPostId(pid);
					if(post == null)
					{
						errors.append("岗位：");
						errors.append(name);
						errors.append("(");
						errors.append(pid);
						errors.append(") 不存在。\r\n\r\n");
					}
					else
					{
						if(!name.equals(post.getPostName()))
						{
							errors.append("岗位：");
							errors.append(name);
							errors.append("(");
							errors.append(pid);
							errors.append(") 已经更改名称。\r\n\r\n");
						}
						
						if(StringUtils.isNotEmpty(oid))
						{
							boolean isExistOrg = false;
							Set<TUumsBasePostOrg> orgs = post.getBasePostOrgs();
							for(TUumsBasePostOrg org : orgs)
							{
								if(oid.equals(org.getOrgId()))
									isExistOrg = true;
							}
							if(!isExistOrg)
							{
								errors.append("岗位：");
								errors.append(name);
								errors.append("(");
								errors.append(pid);
								errors.append(") 已经更改机构。\r\n\r\n");
							}
						}
					}
				}
				
				if("g".equalsIgnoreCase(type))
				{
					Position post = userSrv.getPostInfoByPostId(id);
					if(post == null)
					{
						errors.append("全局岗位：");
						errors.append(name);
						errors.append("(");
						errors.append(id);
						errors.append(") 不存在。\r\n\r\n");
					}
					else
					{
						if(!name.equals(post.getPostName()))
						{
							errors.append("全局岗位：");
							errors.append(name);
							errors.append("(");
							errors.append(id);
							errors.append(") 已经更改机构。\r\n\r\n");
						}
					}
				}

			}
			renderHtml(errors.toString());
		}
		return null;
	}
	
	/**
	 * 部门验证
	 * @return
	 * @throws Exception
	 */
	public String orgValidate() throws Exception
	{
		String id = getRequest().getParameter("orgId");
		String name = getRequest().getParameter("orgName");
		StringBuilder errors = new StringBuilder();
		TUumsBaseOrg org =userSrv.getOrgInfoByOrgId(id);
		if(org == null)
		{
			errors.append("机构：");
			errors.append(name);
			errors.append("(");
			errors.append(id);
			errors.append(") 不存在。\r\n\r\n");
		}
		else
		{
			if(!name.equals(org.getOrgName()))
			{
				errors.append("机构：");
				errors.append(name);
				errors.append("(");
				errors.append(id);
				errors.append(") 名称已经更改。\r\n\r\n");
			}
		}
		renderHtml(errors.toString());
		return null;
	}
	
	/**
	 * 流程验证
	 * @return
	 * @throws Exception
	 */
	public String processDefValidate() throws Exception
	{
		if(StringUtils.isNotEmpty(strActor))
		{
			String[] actors = strActor.split(",");

			StringBuilder errors = new StringBuilder();
			
			for(int i=0;i<actors.length;i=i+2)
			{
				String id = actors[i];
				String name = actors[i+1];
				TwfBaseProcessfile pf = pdSrv.getProcessfileByPfId(id);
				if(pf == null)
				{
					errors.append("流程：");
					errors.append(name);
					errors.append("(");
					errors.append(id);
					errors.append(") 不存在。\r\n\r\n");
				}
				else if(!name.equals(pf.getPfName()))
				{
						errors.append("流程：");
						errors.append(name);
						errors.append("(");
						errors.append(id);
						errors.append(") 名称已经更改。\r\n\r\n");
				}

			}
			renderHtml(errors.toString());
		}
		return null;
	}
	
	/**
	 * 表单验证
	 * @return
	 * @throws Exception
	 */
	public String eformValidate() throws Exception
	{
		if(StringUtils.isNotEmpty(strActor))
		{
			String[] actors = strActor.split(",");

			StringBuilder errors = new StringBuilder();
			
			for(int i=0;i<actors.length;i=i+2)
			{
				String id = actors[i];
				String name = actors[i+1];
				TEFormTemplate form = eformSrv.getForm(Long.parseLong(id));
				if(form == null)
				{
					errors.append("表单：");
					errors.append(name);
					errors.append("(");
					errors.append(id);
					errors.append(") 不存在。\r\n\r\n");
				}
				else if(!name.equals(form.getTitle()))
				{
						errors.append("表单：");
						errors.append(name);
						errors.append("(");
						errors.append(id);
						errors.append(") 名称已经更改。\r\n\r\n");
				}
			}
			renderHtml(errors.toString());
		}
		return null;
	}

	public String getStrActor() {
		return strActor;
	}

	public void setStrActor(String strActor) {
		this.strActor = strActor;
	}

}
