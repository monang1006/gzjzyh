package com.strongit.oa.iphoneset;


import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Date;

import javax.imageio.ImageIO;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;


import com.strongit.oa.bo.ToaIphoneSet;
import com.strongit.oa.util.PathUtil;
import com.strongmvc.webapp.action.BaseActionSupport;


@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "iphoneset.action", type = ServletActionRedirectResult.class) })
public class IphonesetAction extends BaseActionSupport {
	private static final long serialVersionUID = 1L;
	
	private File iphoneBgPic;
	
	private File iphoneDbsyPic;	
	private File iphoneGwglPic;
	private File iphoneTxlPic;	
	private File iphoneNbyjPic;	
	private File iphoneTzggPic;	
	private File iphoneXtszPic;
	
	private IphonesetManager manager;
	
	private ToaIphoneSet model = new ToaIphoneSet(); 

	@Override
	public String delete() throws Exception {
		return null;
	}


	public Object getModel() {
		return model;
	}

	@Override
	public String input() throws Exception {
		return INPUT;
	}

	@Override
	public String list() throws Exception {
		return null;
	}

	@Override
	protected void prepareModel() throws Exception {
		this.model=this.manager.getIphoneset();
		if(null==this.model){
			model = new ToaIphoneSet();
		}
	}

	@Override
	public String save() throws Exception {
		
		if(iphoneBgPic != null) {//iphone登陆界面背景图片	
			String Path = "oa" +File.separator + "image" + File.separator + "iphone" + File.separator +"iphoneBgPic.jpg";
			System.out.println(Path);//获取web项目的路径
			String picPath = PathUtil.getRootPath()+ Path;
			System.out.println(picPath);
			File f = new File(picPath);
			if(!f.exists()) 
			f.createNewFile();
			BufferedImage im = ImageIO.read(iphoneBgPic);
			ImageIO.write(im, "jpg", f);
			
			model.setIphoneBgUpdate(new Date());
		}
		

		
		if(iphoneDbsyPic != null) {//iphone待办事宜图标	
			String Path = "oa" +File.separator + "image" + File.separator + "iphone" + File.separator +"iphoneDbsyPic.png";
			System.out.println(Path);//获取web项目的路径
			String picPath = PathUtil.getRootPath()+ Path;
			System.out.println(picPath);
			File f = new File(picPath);
			if(!f.exists()) 
			f.createNewFile();
			BufferedImage im = ImageIO.read(iphoneDbsyPic);
			ImageIO.write(im, "png", f);
			
			model.setIphoneModularUpdate(new Date());
		}
		
		if(iphoneGwglPic != null) {//iphone公文管理图标	
			String Path = "oa" +File.separator + "image" + File.separator + "iphone" + File.separator +"iphoneGwglPic.png";
			System.out.println(Path);//获取web项目的路径
			String picPath = PathUtil.getRootPath()+ Path;
			System.out.println(picPath);
			File f = new File(picPath);
			if(!f.exists()) 
			f.createNewFile();
			BufferedImage im = ImageIO.read(iphoneGwglPic);
			ImageIO.write(im, "png", f);
			
			model.setIphoneModularUpdate(new Date());
		}
		
		if(iphoneTxlPic != null) {//iphone通讯录图标	
			String Path = "oa" +File.separator + "image" + File.separator + "iphone" + File.separator +"iphoneTxlPic.png";
			System.out.println(Path);//获取web项目的路径
			String picPath = PathUtil.getRootPath()+ Path;
			System.out.println(picPath);
			File f = new File(picPath);
			if(!f.exists()) 
			f.createNewFile();
			BufferedImage im = ImageIO.read(iphoneTxlPic);
			ImageIO.write(im, "png", f);
			
			model.setIphoneModularUpdate(new Date());
		}
		
		if(iphoneNbyjPic != null) {//iphone内部邮件图标	
			String Path = "oa" +File.separator + "image" + File.separator + "iphone" + File.separator +"iphoneNbyjPic.png";
			System.out.println(Path);//获取web项目的路径
			String picPath = PathUtil.getRootPath()+ Path;
			System.out.println(picPath);
			File f = new File(picPath);
			if(!f.exists()) 
			f.createNewFile();
			BufferedImage im = ImageIO.read(iphoneNbyjPic);
			ImageIO.write(im, "png", f);
			
			model.setIphoneModularUpdate(new Date());
		}
		
		if(iphoneTzggPic != null) {//iphone通知公告图标	
			String Path = "oa" +File.separator + "image" + File.separator + "iphone" + File.separator +"iphoneTzggPic.png";
			System.out.println(Path);//获取web项目的路径
			String picPath = PathUtil.getRootPath()+ Path;
			System.out.println(picPath);
			File f = new File(picPath);
			if(!f.exists()) 
			f.createNewFile();
			BufferedImage im = ImageIO.read(iphoneTzggPic);
			ImageIO.write(im, "png", f);
			
			model.setIphoneModularUpdate(new Date());
		}
		
		if(iphoneXtszPic != null) {//iphone系统设置图标	
			String Path = "oa" +File.separator + "image" + File.separator + "iphone" + File.separator +"iphoneXtszPic.png";
			System.out.println(Path);//获取web项目的路径
			String picPath = PathUtil.getRootPath()+ Path;
			System.out.println(picPath);
			File f = new File(picPath);
			if(!f.exists()) 
			f.createNewFile();
			BufferedImage im = ImageIO.read(iphoneXtszPic);
			ImageIO.write(im, "png", f);
			
			model.setIphoneModularUpdate(new Date());
		}
		
		manager.save(model);
		
		return INPUT;
	}

	
	@Autowired
	public void setManager(IphonesetManager manager) {
		this.manager = manager;
	}


	public File getIphoneBgPic() {
		return iphoneBgPic;
	}


	public void setIphoneBgPic(File iphoneBgPic) {
		this.iphoneBgPic = iphoneBgPic;
	}


	public File getIphoneDbsyPic() {
		return iphoneDbsyPic;
	}


	public void setIphoneDbsyPic(File iphoneDbsyPic) {
		this.iphoneDbsyPic = iphoneDbsyPic;
	}


	public File getIphoneGwglPic() {
		return iphoneGwglPic;
	}


	public void setIphoneGwglPic(File iphoneGwglPic) {
		this.iphoneGwglPic = iphoneGwglPic;
	}


	public File getIphoneTxlPic() {
		return iphoneTxlPic;
	}


	public void setIphoneTxlPic(File iphoneTxlPic) {
		this.iphoneTxlPic = iphoneTxlPic;
	}


	public File getIphoneNbyjPic() {
		return iphoneNbyjPic;
	}


	public void setIphoneNbyjPic(File iphoneNbyjPic) {
		this.iphoneNbyjPic = iphoneNbyjPic;
	}


	public File getIphoneTzggPic() {
		return iphoneTzggPic;
	}


	public void setIphoneTzggPic(File iphoneTzggPic) {
		this.iphoneTzggPic = iphoneTzggPic;
	}


	public File getIphoneXtszPic() {
		return iphoneXtszPic;
	}


	public void setIphoneXtszPic(File iphoneXtszPic) {
		this.iphoneXtszPic = iphoneXtszPic;
	}
	

}
