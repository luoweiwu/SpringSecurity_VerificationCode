package com.Gary.GaryRESTful.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import com.Gary.GaryRESTful.properties.GarySecurityProperties;
import com.Gary.GaryRESTful.validate.code.ImageCode;
import com.Gary.GaryRESTful.validate.code.ValidateCode;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;

@RestController
public class ValidateCodeController {
	
	//操作Session
	private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();
	
	public static String sessionKey = "session_key_image_code";

	public static String sessionSmsKey = "session_key_sms_code";
	
	@Autowired
	private GarySecurityProperties garySecurityProperties;
	
	
	@GetMapping("/code/sms")	
	public void createSmsCode(HttpServletRequest request,HttpServletResponse response) throws ServletRequestBindingException
	{
		//生成短信的校验码
		ValidateCode smsCode = createSmsCode();
		//将我们的校验码放入session域中
		sessionStrategy.setAttribute(new ServletWebRequest(request), sessionSmsKey, smsCode);
		//从request域中获取手机号
		String mobile = ServletRequestUtils.getRequiredStringParameter(request, "mobile");
		//发短信(给mobile手机号发送smsCode验证码)
		sendSms(mobile,smsCode.getCode());
	}
	
	//发短信(给mobile手机号发送smsCode验证码)
	private void sendSms(String mobile, String code) {
        //1.腾讯云自己项目的AppID
        int appid = 1400184301;
        
        String appkey = "58f61b731363faba756087b9504bff46";
        
        //短信正文的id
        int templateId =275243;
        
        String smsSign = "Garyd公众号";
        
        String phoneNumber = mobile;
        
        String[] params = new String[1];
        params[0] = code;
        //将验证码打印出来
        System.out.println("验证码: "+code);
        
        SmsSingleSender sender = new SmsSingleSender(appid,appkey);
        
        //86,手机号,模板id,验证码,smsSign
        try {
			SmsSingleSenderResult result = sender.sendWithParam("86", phoneNumber, templateId, params, smsSign, "", "");
		
			//打印是否发送成功
			System.out.println(result);
        } catch (JSONException | HTTPException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}

	//生成短信的校验码
	private ValidateCode createSmsCode() {
		Random r = new Random();
		String code = "" ;
		for(int i=0;i<garySecurityProperties.getCode().getSms().getLength();i++)
		{
			code += r.nextInt(10);
		}
		return new ValidateCode(code,garySecurityProperties.getCode().getSms().getExpireIn());
	}


	@GetMapping("/code/image")
	public void createCode(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		//生成随机数的图片
		ImageCode imageCode = createImageCode(request);
		
		//将随机数放入到session中
		sessionStrategy.setAttribute(new ServletWebRequest(request), sessionKey, imageCode);
		
		//将我们生成的图片写到接口的响应的输出流中
		ImageIO.write(imageCode.getImage(), "JPEG", response.getOutputStream());
		
	}
	
	//生成图片验证码(验证码，图片，失效的时间)
	private ImageCode createImageCode(HttpServletRequest request)
	{
		//定义图片的长和宽
		int width = ServletRequestUtils.getIntParameter(request, "width", garySecurityProperties.getCode().getImage().getWidth());
		int height =  ServletRequestUtils.getIntParameter(request, "height", garySecurityProperties.getCode().getImage().getHeight());;
		
		//生成一张图片
		BufferedImage image = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
		
		//获得画笔工具
		Graphics g = image.getGraphics();
		
		//画一个矩形
		g.setColor(new Color(255,255,255));
		g.fillRect(0, 0, width, height);
		
		//画干扰线
		g.setColor(new Color(0,0,0));
		//设置字体
		g.setFont(new Font("Time New Roman",Font.ITALIC,20));
		Random random = new Random();
		
		for(int i=0;i<20;i++)
		{
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int x1 = random.nextInt(12);
			int y1 = random.nextInt(12);
			//(x,y)到(x+x1,y+y1)
			g.drawLine(x, y, x+x1, y+y1);
		}
		
		//画数据
		String sRand = "";
		for(int i = 0;i<garySecurityProperties.getCode().getImage().getLength();i++)
		{
			String rand =String.valueOf(random.nextInt(10));
			//System.out.println(rand);
			sRand += rand;
			//每一个字都改变一下颜色
			g.setColor(new Color(20+random.nextInt(110),20+random.nextInt(110),20+random.nextInt(110)));
			//画每一个数据
			g.drawString(rand, 13*i, 16);
		}
		
		g.dispose();
		
		//生成我们自己的验证码数据(图片，验证码，过期时间)
		return new ImageCode(image,sRand,garySecurityProperties.getCode().getImage().getExpireIn());
	}
	
	public SessionStrategy getSessionStrategy() {
		return sessionStrategy;
	}

	public void setSessionStrategy(SessionStrategy sessionStrategy) {
		this.sessionStrategy = sessionStrategy;
	}

	public static String getSessionKey() {
		return sessionKey;
	}

	public static void setSessionKey(String sessionKey) {
		ValidateCodeController.sessionKey = sessionKey;
	}


	
}
