package com.immotor.common.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//@Component
public class ServiceUtil {
    private static final ReadConfigUtil config = ReadConfigUtil.getInstance();
	private static final Logger logger = LoggerFactory.getLogger(ServiceUtil.class);
	/*  手机短信  */
//	@Value("${msg_apikey}")
	private String apikey =config.getValue("msg_apikey");
//	@Value("${msg_uri_send_sms}")
	private String msg_uri_send_sms =config.getValue("msg_uri_send_sms");
	/*  邮箱   */
//	@Value("${jdbc.user}")
	private String mail_url =config.getValue("mail_url");
//	@Value("${mail_apiUser}")
	private String mail_apiUser=config.getValue("mail_apiUser");
//	@Value("${mail_apiKey}")
	private String mail_apiKey=config.getValue("mail_apiKey");
//	@Value("${mail_from}")   
	private String mail_from=config.getValue("mail_from");
//	@Value("${mail_fromname}")
	private String mail_fromname=config.getValue("mail_fromname");
//	@Value("${mail_subject}")
	private String mail_subject=config.getValue("mail_subject");
//	@Value("${mail_template_invoke_name}")
	private String mail_template_invoke_name=config.getValue("mail_template_invoke_name");
	
	/*
	 * 发送邮件
	 * {"message":"success","email_id_list":["1467454085470_52628_24834_98.sc-10_10_24_226-inbound0$wqs_ccut@163.com"]}
	 */
	public int sendEmailCode(String email, String code){
		String vars = "{\"to\": [\""+ email +"\"],\"sub\":{\"%code%\": [\""+ code +"\"]}}";
		Map<String, Object> map = Utils.newMap();
		map.put("api_user", mail_apiUser);
        map.put("api_key", mail_apiKey);
        map.put("from", mail_from);
        map.put("fromname", mail_fromname);
        map.put("subject", mail_subject);
        map.put("template_invoke_name",mail_template_invoke_name );
		map.put("substitution_vars", vars);
		map.put("resp_email_id", "true");
		
		String resp = HttpClientUtil.post(mail_url, map);
		if(Utils.isEmpty(resp)){
			return -1;
		}
		Map<String, Object> respMap = Utils.json2Map(resp);
		String respMsg = respMap.get("message").toString();
		if(!respMsg.equals("success")){
			logger.error("###sendEmailCode error"+resp);
			return -1;
		}
		return 0;
		
	}
	
	/*
	 * {"http_status_code":400,"code":-3,"msg":"IP没有权限","detail":"IP 113.116.106.177 未加入白名单,可在后台‘系统设置->IP白名单设置’里添加"}
	   {"code":0,"msg":"发送成功","count":1,"fee":0.055,"unit":"RMB","mobile":"18194082860","sid":8063606842}
	 */
	public  int sendPhoneCode(String passcode, String areaCode, String phone) throws Exception{
		String resp = "";
		if(areaCode.equals("86")){
		    resp = sendSms(apikey , "【immotor】您的验证码是" + passcode, "+" + areaCode + phone);
		}else{
			resp = sendSms(apikey , "【immotor】Verification code" + passcode, "+" + areaCode + phone);
		}
		if(Utils.isEmpty(resp)){
			return -1;
		}
		Map<String, Object> map = Utils.json2Map(resp);
		int code = Integer.parseInt(map.get("code").toString());
		if(code != 0){
			logger.error("###sendPhoneCode error"+resp);
		}
		return code;
	}
	
	
    public static ReadConfigUtil getConfig() {
        return config;
    }

    
    public static Logger getLogger() {
        return logger;
    }
    
    public static void main(String[] args) throws Exception {
		new ServiceUtil().sendPhoneCode("123412", "86", "18565805582");
		
//		String str = "{\"code\":0,\"msg\":\"发送成功\"}";
//		String str = "";
//		Map<String, Object> map = Utils.json2Map(str);
//		System.out.println(map.get("code").toString());
//		System.out.println(map.get("code")=="0");
//		System.out.println("0".equals(map.get("code")));
//		System.out.println(map.get("code").toString().equals(0));
		
	}
	
	//18668078673
	private String sendSms(String apikey, String text, String mobile) throws IOException {
//		Map<String, String> params = new HashMap<String, String>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("apikey", apikey);
		params.put("text", text);
		params.put("mobile", mobile);
//		return post(URI_SEND_SMS, params);
//		return HttpClientUtil.post(config.getValue("msg_uri_send_sms"), params);
	      return HttpClientUtil.post(msg_uri_send_sms, params);

	}
	
	
//	private String post(String url, Map<String, String> paramsMap) {
//		CloseableHttpClient client = HttpClients.createDefault();
//		String responseText = "";
//		CloseableHttpResponse response = null;
//		try {
//			HttpPost method = new HttpPost(url);
//			if (paramsMap != null) {
//				List<NameValuePair> paramList = new ArrayList<NameValuePair>();
//				for (Map.Entry<String, String> param : paramsMap.entrySet()) {
//					NameValuePair pair = new BasicNameValuePair(param.getKey(),
//							param.getValue());
//					paramList.add(pair);
//				}
//				method.setEntity(new UrlEncodedFormEntity(paramList, "UTF-8"));
//			}
//			response = client.execute(method);
//			HttpEntity entity = response.getEntity();
//			if (entity != null) {
//				responseText = EntityUtils.toString(entity);
//			}
//		} catch (Exception e) {
//			logger.error("##sendSms error", e);
//		} finally {
//			try {
//				response.close();
//			} catch (Exception e) {
//				logger.error("##sendSms error", e);
//			}
//		}
//		return responseText;
//	}
}
