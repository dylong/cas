
package com.immotor.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.immotor.common.utils.Result;
import com.immotor.common.utils.ServiceUtil;
import com.immotor.common.utils.Utils;
import com.immotor.entity.UserInfo;
import com.immotor.service.UserService;

public class RegisteredController {

    private static final Logger logger = LoggerFactory.getLogger(RegisterAfterLoginController.class);

    private UserService userService;

    /**
    * scene判断  注册还是忘记密码
    * area_code来判断  手机注册还是邮箱注册，手机忘记密码还是邮箱忘记密码
    * 申请动态验证码
    */
    @ResponseBody
    @RequestMapping("passcode")
    public Map<String, Object> getPassCode(HttpServletRequest request, @ModelAttribute("user") UserInfo user) {
        Map<String, Object> userMap =  Utils.newMap();
        Map<String, Object> returnUserMap =  Utils.newMap();
        try {
            String area_code = Utils.toString(user.getArea_code());// null:邮箱登陆， not null：手机登陆
            String phone = "", username = "";
            if (Utils.isEmpty(area_code)) {
                String email = user.getEmail();
                // 根据邮箱去数据库查询是否存在
                userMap = userService.getByEmail(email);
                username = email;
            } else {
                phone = user.getPhone();
                // 根据区号和手机号码去数据库查询是否存在
                userMap = userService.getByPhone(area_code, phone);
                username = area_code + phone;
            }
            // scene: null 注册, not null：忘记密码
            String scene = user.getScene();
            if (Utils.isEmpty(scene) && !Utils.isEmpty(userMap)) {
                returnUserMap.put("rtdesc", "该用户存在");
                return Result.NO(returnUserMap);// 注册 用户不能存在
            }
            if (!Utils.isEmpty(scene) && Utils.isEmpty(userMap)) {
                returnUserMap.put("rtdesc", "该不用户存在");
                return Result.NO(returnUserMap);// 忘记密码 用户需要存在
            }
            // 获取并发送校验码
            String passcode = Utils.generatePassCode();
            int num = userService.getPasscodes(username, passcode, "", userMap);
            if (num > 0) {
                if (!Utils.isEmpty(area_code)) {
                    if (new ServiceUtil().sendPhoneCode(passcode, area_code, phone) != 0) {// 手机短信
                        returnUserMap.put("rtdesc", "手机短信发送失败");
                        return Result.NO(returnUserMap);// 短信code发送失败
                    }
                } else {
                    if (new ServiceUtil().sendEmailCode(username, passcode) != 0) {// 发送邮件code
                        returnUserMap.put("rtdesc", "邮件发送失败");
                        return Result.NO(returnUserMap);// 邮件code发送失败
                    }
                }
                HttpSession session = request.getSession();
                session.setAttribute("passcode", passcode);
                returnUserMap.put("passcode", passcode);
                return Result.OK(returnUserMap);
            }
            returnUserMap.put("rtdesc", "验证码发送次数过多");
            return Result.NO(returnUserMap);
        } catch (Exception e) {
            logger.error("###/user/passcode", e);
            //            return Result.no(Status.SERVER_ERROR);
            userMap.put("rtdesc", "验证码发送失败");
            return Result.NO(userMap);
        }
    }

    /**
     * 注册
     * @param user
     * @param regpassword
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/addRegisters", method = RequestMethod.POST)
    public Map<String, Object> addStudent(@ModelAttribute("user") UserInfo user) {
        System.out.println("进入addRegisters");
        Map<String, Object> userMap = Utils.newMap();
        Map<String, Object> returnUserMap =  Utils.newMap();
        try { // 
            if (Utils.isEmpty(user.getArea_code())) {
                String email = user.getEmail();
                // 根据邮箱去数据库查询是否存在
                userMap = userService.getByEmail(email);
            } else {
                // 根据区号和手机号码去数据库查询是否存在
                userMap = userService.getByPhone(user.getArea_code(), user.getPhone());
            }
            if (!Utils.isEmpty(userMap)) {
                returnUserMap.put("rtdesc", "该用户存在");
                return Result.NO(returnUserMap);
            }
            //        String username = Utils.isEmpty(user.getArea_code()) ? user.getEmail() : user.getArea_code() + user.getPhone();
            //        // 查询合法passcode by phone 和 passcode
            //        Map<String, Object> codeMap = passcodeService.getPasscode(username, passcode);
            //        if (Utils.isEmpty(codeMap)) {
            //            return Result.no(Status.ERROR, "Passcode");// 验证码错误
            //        }
            //        long time = Utils.String2Date(codeMap.get("expired").toString()).getTime();
            //        if (System.currentTimeMillis() - time > 0) {
            //            return Result.no(Status.EXPIRED, "Passcode");// 验证码过期
            //        }
            // 注册 
            user.setId(Utils.getUUID());
            user.setActive("1");
            int num = userService.addUserInfo(user);
            if (num == 0) {
                returnUserMap.put("rtdesc", "注册失败");
                return Result.NO(returnUserMap);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            returnUserMap.put("rtdesc", "注册失败");
            return Result.NO(returnUserMap);
        }
        return Result.OK();
    }

    /**
     * 修改用户密码
     * @param user
     * @param regpassword
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/mfyUserPass", method = RequestMethod.POST)
    public Map<String, Object> mfyUserPass(@ModelAttribute("user") UserInfo user) {
        System.out.println("进入mfyUserPass");
        Map<String, Object> userMap = Utils.newMap();
        Map<String, Object> returnUserMap = Utils.newMap();
        try { // 
            if (Utils.isEmpty(user.getArea_code())) {
                String email = user.getEmail();
                // 根据邮箱去数据库查询是否存在
                userMap = userService.getByEmail(email);
            } else {
                // 根据区号和手机号码去数据库查询是否存在
                userMap = userService.getByPhone(user.getArea_code(), user.getPhone());
            }
            if (Utils.isEmpty(userMap)) {
                returnUserMap.put("rtdesc", "该不用户存在");
                return Result.NO(returnUserMap);
            }
            //        String username = Utils.isEmpty(user.getArea_code()) ? user.getEmail() : user.getArea_code() + user.getPhone();
            //        // 查询合法passcode by phone 和 passcode
            //        Map<String, Object> codeMap = passcodeService.getPasscode(username, passcode);
            //        if (Utils.isEmpty(codeMap)) {
            //            return Result.no(Status.ERROR, "Passcode");// 验证码错误
            //        }
            //        long time = Utils.String2Date(codeMap.get("expired").toString()).getTime();
            //        if (System.currentTimeMillis() - time > 0) {
            //            return Result.no(Status.EXPIRED, "Passcode");// 验证码过期
            //        }
            // 修改
            user.setArea_code(String.valueOf(userMap.get("area_code")));
            user.setPhone(String.valueOf(userMap.get("phone")));
            user.setEmail(String.valueOf(userMap.get("email")));
            user.setId(String.valueOf(userMap.get("id")));
            int num = userService.mfyUserPass(user);
            if (num == 0) {
                returnUserMap.put("rtdesc", "修改密码失败");
                return Result.NO(returnUserMap);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            returnUserMap.put("rtdesc", "修改密码失败");
            return Result.NO(returnUserMap);
        }
        return Result.OK();
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    public static void main(String[] args) {
        Map<String, Object> userMap = Utils.newMap();
        System.out.println(userMap);
    }
}
