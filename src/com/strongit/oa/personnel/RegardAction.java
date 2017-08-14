package com.strongit.oa.personnel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import com.strongit.oa.personnel.util.Regard;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;
@SuppressWarnings({ "unchecked", "serial" })
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "regard.action", type = ServletActionRedirectResult.class) })
public class RegardAction extends BaseActionSupport {
	private Page<Regard> page = new Page<Regard>(FlexTableTag.MAX_ROWS, true);
	private Regard model =new Regard();
	private String regardId;
	private List<Regard> lus=new ArrayList<Regard>();
	@Override
	public String delete() throws Exception {
		return null;
	}

	@Override
	public String input() throws Exception {
		return INPUT;
	}

	@Override
	public String list() throws Exception {
		HttpServletRequest request= ServletActionContext.getRequest();
		HttpSession session=request.getSession();
		 int pagesize = page.getPageSize();	//得到每页的行数
		 int currentpage = page.getPageNo();	//得到当前页数
			//总页数
		
		 if(session.getAttribute("lis")!=null){
			 lus=(List<Regard>)session.getAttribute("lis");
			 System.out.println("lus:"+lus+"\n"+
					 			"rDate in list:"+lus.get(0).getRDate());
		 }
		 List<Regard> ls=new ArrayList<Regard>();
		 int listsize = lus.size();
		 int totalPage=listsize/pagesize;
		 if(listsize%pagesize!=0){
			   totalPage+=1;
		   }
		 for(int i=(currentpage-1)*pagesize;i<listsize&&i<currentpage*pagesize;i++){
			 ls.add(lus.get(i));
		 }
		 
				page.setResult(ls);
			    page.setPageNo(currentpage);
			    page.setAutoCount(true);
			    page.setPageSize(pagesize);
			page.setTotalCount(listsize);

		return SUCCESS;
	}

	@Override
	protected void prepareModel() throws Exception {
		HttpServletRequest request= ServletActionContext.getRequest();
		HttpSession session=request.getSession();
		List<Regard> lis=(List<Regard>) session.getAttribute("lis");
		
		if(regardId!=null){
			for(int i=0;i<lis.size();i++){
				if(lis.get(i).getRegardId().equals(regardId)){
					model=lis.get(i);
				}
			}
		}else{
			model=new Regard();
		}
	}

	@Override
	public String save() throws Exception {
		HttpServletRequest request= ServletActionContext.getRequest();
		HttpSession session=request.getSession();
		List<Regard> l=(List<Regard>) session.getAttribute("lis");
		Random r1=new Random();
		Random r2=new Random();
		Random r3=new Random();
		int n1=r1.nextInt(100);
		int n2=r2.nextInt(100);
		int n3=r3.nextInt(100);
		String id="001"+n1+n2+n3;
		if(l!=null){
			model.setRegardId(id);
			l.add(model);
			session.setAttribute("lis", l);
			
		}else{
			List<Regard> ll=new ArrayList<Regard>();
			model.setRegardId(id);
			ll.add(model);
			//
			System.out.println("rDate:"+model.getRDate());
			session.setAttribute("lis", ll);
		}
		return renderHtml("<script>  window.location='"
				+ getRequest().getContextPath()
				+ "/personnel/regard.action"+
				"';"+
				"</script>");
	}

	public Object getModel() {
		// TODO Auto-generated method stub
		return model;
	}
	public String edit() throws Exception{
		prepareModel();
		return "edit";
	}
	public String view() throws Exception{
		
		return "view";
	}

	public Page<Regard> getPage() {
		return page;
	}

	public void setPage(Page<Regard> page) {
		this.page = page;
	}

	public String getRegardId() {
		return regardId;
	}

	public void setRegardId(String regardId) {
		this.regardId = regardId;
	}

	public List<Regard> getLus() {
		return lus;
	}

	public void setLus(List<Regard> lus) {
		this.lus = lus;
	}

	public void setModel(Regard model) {
		this.model = model;
	}

}
