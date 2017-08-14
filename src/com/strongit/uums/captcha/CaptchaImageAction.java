package com.strongit.uums.captcha;

import org.springframework.beans.factory.InitializingBean;

import com.opensymphony.xwork2.ActionSupport;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;

import com.octo.captcha.service.image.ImageCaptchaService;
import com.strongmvc.webapp.action.BaseActionSupport;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

@SuppressWarnings("serial")
@ParentPackage("default")
@Results( { 
	@Result(name = BaseActionSupport.RELOAD , value = "/WEB-INF/jsp/postmanage/postmanage-list.jsp"),
	@Result(name = BaseActionSupport.INPUT, value = "/WEB-INF/jsp/postmanage/postmanage-input.jsp")
	})
public class CaptchaImageAction extends ActionSupport  implements
		InitializingBean {

	private ImageCaptchaService jcaptchaService;

	public String execute() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		byte[] captchaChallengeAsJpeg = null;

		ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();

		String captchaId = request.getSession().getId();

		// 生成验证码 图片，在这里我们只需要在spring的bean配置文件中更改jcaptchaService的实现，就可以生成不同的验证码 图片

		BufferedImage challenge = jcaptchaService.getImageChallengeForID(
				captchaId, request.getLocale());
		JPEGImageEncoder jpegEncoder = JPEGCodec
				.createJPEGEncoder(jpegOutputStream);
		jpegEncoder.encode(challenge);

		captchaChallengeAsJpeg = jpegOutputStream.toByteArray();

		response.setHeader("Cache-Control", "no-store");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/jpeg");
		ServletOutputStream responseOutputStream = response.getOutputStream();
		responseOutputStream.write(captchaChallengeAsJpeg);
		responseOutputStream.flush();
		responseOutputStream.close();

		return null;
	}

	public void setJcaptchaService(ImageCaptchaService jcaptchaService) {
		this.jcaptchaService = jcaptchaService;
	}

	public void afterPropertiesSet() throws Exception {
		if (jcaptchaService == null) {
			throw new RuntimeException("Image captcha service wasn`t set!");
		}
	}

	
	

}
