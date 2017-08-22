package com.strongit.gzjzyh.upload;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.strongit.gzjzyh.policeregister.IPoliceRegisterManager;
import com.strongit.gzjzyh.util.FileKit;
import com.strongit.gzjzyh.util.ImageKit;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.myinfo.MyInfoManager;
import com.strongit.oa.mylog.MyLogManager;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongit.uums.security.ApplicationConfig;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

@ParentPackage("default")
@Results({ @Result(name = BaseActionSupport.RELOAD, value = "fileUpload.action", type = ServletActionRedirectResult.class) })
public class FileUploadAction extends BaseActionSupport {

	private Object model = new Object();

	private String srcImagePath;
	private int selectX;
	private int selectY;
	private int selectW;
	private int selectH;

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
		return SUCCESS;
	}

	@Override
	protected void prepareModel() throws Exception {

	}

	@Override
	public String save() throws Exception {
		return null;
	}

	public String cutImage() throws Exception {
		String fileSuffix = this.srcImagePath.substring(this.srcImagePath
				.lastIndexOf(".") + 1);
		String destImagePath = FileKit.getNewFileRelativePath("." + fileSuffix);

		ImageKit.cutImage(fileSuffix,
				FileKit.relativePath2AbsolutePath(this.srcImagePath),
				FileKit.relativePath2AbsolutePath(destImagePath), this.selectX,
				this.selectY, this.selectW, this.selectH);

		this.renderText(destImagePath);
		return null;
	}

	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getSrcImagePath() {
		return srcImagePath;
	}

	public void setSrcImagePath(String srcImagePath) {
		this.srcImagePath = srcImagePath;
	}

	public int getSelectX() {
		return selectX;
	}

	public void setSelectX(int selectX) {
		this.selectX = selectX;
	}

	public int getSelectY() {
		return selectY;
	}

	public void setSelectY(int selectY) {
		this.selectY = selectY;
	}

	public int getSelectW() {
		return selectW;
	}

	public void setSelectW(int selectW) {
		this.selectW = selectW;
	}

	public int getSelectH() {
		return selectH;
	}

	public void setSelectH(int selectH) {
		this.selectH = selectH;
	}
}