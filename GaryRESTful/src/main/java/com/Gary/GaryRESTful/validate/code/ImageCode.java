package com.Gary.GaryRESTful.validate.code;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;

//图片验证码
public class ImageCode extends ValidateCode{
	
	//给前台展示的图片
	private BufferedImage image;
	
	public ImageCode(BufferedImage image,String code,int expreTime)
	{
		super(code,expreTime);
		this.image = image;
	}
	
	public ImageCode(BufferedImage image,String code,LocalDateTime expreTime)
	{
		super(code,expreTime);
		this.image = image;
	}
	
	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}
}
