package com.immotor.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("user")
public class UsersController {
//    private static final Logger logger = LoggerFactory.getLogger(UsersController.class);
//    @Autowired
//    private UserService         userService;
//    @Autowired
//    private TokenService        tokenService;
//    @Autowired
//    private PasscodeService     passcodeService;
//    @Autowired
//    private ScooterService      scooterService;
//    @Autowired
//    private ServiceUtil         serviceUtil;
//    
//    /*
//     * 用户分享列表
//     */
//    @ResponseBody
//    @RequestMapping("share/list")
//    public Result shareList(HttpServletRequest request) {
//        try {
//            JsonNode node = Utils.readParam(request.getInputStream());
//            Result res = tokenService.checkToken(node.get("token").asText(), false);
//            if(res.getCode() != 200){
//                return res;
//            }
//            int currentPage = node.get("currentPage").asInt();
//            int numPerPage = node.get("numPerPage").asInt();
//            return Result.ok(userService.getShareList(currentPage, numPerPage));
//        } catch (Exception e) {
//            logger.error("###/share/list", e);
//            return Result.no(Status.SERVER_ERROR);
//        }
//    }
//    
//    /*
//     * 用户分享
//     * 0-分享图片， 1-分享html
//     */
//    @ResponseBody
//    @RequestMapping("share/upload")
//    public Result shareUpload(HttpServletRequest request) {
//        try {
//            String token = request.getParameter("token");
//            Result res = tokenService.checkToken(token, false);
//            if (res.getCode() != 200) {
//                return res;
//            }
//            String type = request.getParameter("type");
//            String title = request.getParameter("title");
//            String content = request.getParameter("content");
//            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request; 
//            MultipartFile file = multipartRequest.getFile("file");
//            String key = "";
//            if (file != null && file.getSize() > 0) {
//                key = userService.shareUpload(file, Integer.parseInt(type),res.getResult().toString(), title, content);
//            }
//            if(Utils.isEmpty(key)){
//                return Result.NO;
//            }
//            return Result.ok(key);
//        } catch (Exception e) {
//            logger.error("###/share/upload", e);
//            return Result.no(Status.SERVER_ERROR);
//        }
//    }
//    
//    /*
//     * scene判断  注册还是忘记密码
//     * area_code来判断  手机注册还是邮箱注册，手机忘记密码还是邮箱忘记密码
//     * 申请动态验证码
//     */
//    @ResponseBody
//    @RequestMapping("passcode")
//    public Result getPassCode(HttpServletRequest request) {
//        try {
//            JsonNode node = Utils.readParam(request.getInputStream());
//            Map<String, Object> userMap = Utils.newMap();
//            String area_code = Utils.nodeAsTest(node.get("area_code"));// null:邮箱登陆， not null：手机登陆
//            String phone = "", username = "";
//            if (Utils.isEmpty(area_code)) {
//                String email = node.get("email").asText();
//                userMap = userService.getByEmail(email, false);
//                username = email;
//            } else {
//                phone = node.get("phone").asText();
//                userMap = userService.getByPhone(area_code, phone, false);
//                username = area_code + phone;
//            }
//            // scene: null 注册, not null：忘记密码
//            String scene = Utils.nodeAsTest(node.get("scene"));
//            if (Utils.isEmpty(scene) && !Utils.isEmpty(userMap)) {
//                return Result.no(Status.USER_REGISTERED);// 注册 用户不能存在
//            }
//            if (!Utils.isEmpty(scene) && Utils.isEmpty(userMap)) {
//                return Result.no(Status.USER_NOT_EXISTS);// 忘记密码 用户需要存在
//            }
//            // 获取并发送校验码
//            String passcode = Utils.generatePassCode();
//            if (!Utils.isEmpty(area_code)) {
//                if (serviceUtil.sendPhoneCode(passcode, area_code, phone) != 0) {// 手机短信
//                    return Result.no(Status.SEND_FAIL);// 短信code发送失败
//                }
//            } else {
//                if (serviceUtil.sendEmailCode(username, passcode) != 0) {// 发送邮件code
//                    return Result.no(Status.SEND_FAIL);// 邮件code发送失败
//                }
//            }
//            Passcodes passcodes = addPasscodes(username, passcode, scene, userMap);// 临时保存passcode
//            return passcodeService.insert(passcodes) > 0 ? Result.OK : Result.NO;
//        } catch (Exception e) {
//            logger.error("###/user/passcode", e);
//            return Result.no(Status.SERVER_ERROR);
//        }
//    }
//
//    // 保存验证码
//    private Passcodes addPasscodes(String username, String passcode, String scene, Map<String, Object> userMap) {
//        Passcodes passcodes = new Passcodes();
//        passcodes.setUsername(username);;// 带区号
//        passcodes.setPasscode(passcode);
//        // 重置密码的时候，存上userId
//        if (!Utils.isEmpty(scene) && "reset".equals(scene)) {
//            passcodes.setUser_id(userMap.get("id").toString());
//        }
//        return passcodes;
//    }
//
//    /*
//     * 忘记密码前校验动态验证码
//     */
//    @ResponseBody
//    @RequestMapping("passcode/check")
//    public Result checkPasscode(HttpServletRequest request) {
//        JsonNode node;
//        try {
//            node = Utils.readParam(request.getInputStream());
//            // 非空校验
//            String emptyField = Utils.getEmptyField(node, "passcode");
//            if (!Utils.isEmpty(emptyField)) {
//                return Result.no(Status.NOT_NULL, emptyField);
//            }
//
//            String area_code = Utils.nodeAsTest(node.get("area_code"));// null:邮箱登陆， not null：手机登陆
//            String username = "";
//            if (Utils.isEmpty(area_code)) {
//                String email = node.get("email").asText();
//                username = email;
//            } else {
//                String phone = node.get("phone").asText();
//                username = area_code + phone;
//            }
//
//            String passcode = node.get("passcode").asText();
//            // 1，查询合法passcode by phone 和 passcode
//            Map<String, Object> codeMap = passcodeService.getPasscode(username, passcode);
//            if (Utils.isEmpty(codeMap)) {
//                return Result.no(Status.ERROR, "Passcode");// 验证码错误
//            }
//            long time = Utils.String2Date(codeMap.get("expired").toString()).getTime();
//            if (System.currentTimeMillis() - time > 0) {
//                return Result.no(Status.EXPIRED, "Passcode");// 验证码过期
//            }
//
//            // 2，生成临时token
//            Map<String, Object> map = Utils.newMap();
//            map.put("id", codeMap.get("user_id"));
//            map.put("username", username);
//            String token = TokenUtil.createToken(map);
//            // 3，添加密码重置表
//            return Result.ok(addPasswordReset(username, token));
//        } catch (Exception e) {
//            logger.error("###/user/passcode/check", e);
//            return Result.no(Status.SERVER_ERROR);
//        }
//    }
//
//    // 注册
//    @ResponseBody
//    @RequestMapping(value = "register", method = RequestMethod.POST)
//    public Result register(HttpServletRequest request) {
//        try {
//            JsonNode node = Utils.readParam(request.getInputStream());
//            // 校验:非空以及长度限制
//            String emptyField = Utils.getEmptyField(node, "app_version", "passcode", "password");
//            if (!Utils.isEmpty(emptyField)) {
//                return Result.no(Status.NOT_NULL, emptyField);
//            }
//            UserDto user = getUser(node);
//            String passcode = node.get("passcode").asText();
//            String ip = Utils.getIP(request);
//            user.setDevice_type(Utils.getDeviceType(request.getHeader("user-agent")));// 0：android，1：ios，2：论坛, 3：官网，4: 其他
//            // 先校验 手机号码不能存在
//            Map<String, Object> userMap = Utils.newMap();
//            if (Utils.isEmpty(user.getArea_code())) {
//                userMap = userService.getByEmail(user.getEmail(), false);
//            } else {
//                userMap = userService.getByPhone(user.getArea_code(), user.getPhone(), false);
//            }
//            if (!Utils.isEmpty(userMap)) {
//                return Result.no(Status.USER_REGISTERED);
//            }
//            String username = Utils.isEmpty(user.getArea_code()) ? user.getEmail() : user.getArea_code() + user.getPhone();
//            // 查询合法passcode by phone 和 passcode
//            Map<String, Object> codeMap = passcodeService.getPasscode(username, passcode);
//            if (Utils.isEmpty(codeMap)) {
//                return Result.no(Status.ERROR, "Passcode");// 验证码错误
//            }
//            long time = Utils.String2Date(codeMap.get("expired").toString()).getTime();
//            if (System.currentTimeMillis() - time > 0) {
//                return Result.no(Status.EXPIRED, "Passcode");// 验证码过期
//            }
//            // 注册 就相当于登录，需要生成token 和 记录登录日志
//            userService.insert(user, ip);
//            Map<String, Object> respMap = tokenService.createToken(user.getId());
//            respMap.putAll(userService.get(user.getId()));// 查询用户信息 返回
//            Utils.removeMapKeys(respMap, "password", "remember_token", "deleted_at"); // 删除多余的属性
//            return Result.ok(respMap);
//        } catch (Exception e) {
//            logger.error("###/user/register", e);
//            return Result.no(Status.SERVER_ERROR);
//        }
//    }
//
//    /*
//     * 登录
//     */
//    @ResponseBody
//    @RequestMapping("login")
//    public Result login(HttpServletRequest request) {
//        JsonNode node;
//        try {
//            node = Utils.readParam(request.getInputStream());
//            // 非空校验
//            String emptyField = Utils.getEmptyField(node, "app_version", "password");
//            if (!Utils.isEmpty(emptyField)) {
//                return Result.no(Status.NOT_NULL, emptyField);
//            }
//            UserDto user = getUser(node);
//            String ip = Utils.getIP(request);
//            int dType = Utils.getDeviceType(request.getHeader("user-agent"));
//            // TODO 长度限制
//            Map<String, Object> userMap = Utils.newMap();
//            if (Utils.isEmpty(user.getArea_code())) {
//                userMap = userService.getByEmail(user.getEmail(), true);
//            } else {
//                userMap = userService.getByPhone(user.getArea_code(), user.getPhone(), true);
//            }
//            if (Utils.isEmpty(userMap)) {
//                return Result.no(Status.USER_NOT_EXISTS);
//            }
//            if (!DigestUtils.md5Hex(user.getPassword()).equals(userMap.get("password"))) {
//                return Result.no(Status.USER_WRONG_USERNAME);
//            }
//            String id = userMap.get("id").toString();
//            Map<String, Object> tokenMap = tokenService.createToken(id);
//            userMap.putAll(tokenMap);
//            userService.login(user.getApp_version(), user.getDeviceToken(), id, dType, ip);
//            Utils.removeMapKeys(userMap, "password", "remember_token", "deleted_at"); // 删除多余的属性
//            // 添加车辆信息
//            userMap.put("scooter", scooterService.getByUser(id));
//            return Result.ok(userMap);
//        } catch (Exception e) {
//            logger.error("###/user/login", e);
//            return Result.no(Status.SERVER_ERROR);
//        }
//    }
//
//    /*
//    * 忘记密码，重置密码
//    */
//    @ResponseBody
//    @RequestMapping("password/reset")
//    public Result reset(HttpServletRequest request) {
//        JsonNode node;
//        try {
//            node = Utils.readParam(request.getInputStream());
//            // 非空校验
//            String emptyField = Utils.getEmptyField(node, "password", "token");
//            if (!Utils.isEmpty(emptyField)) {
//                return Result.no(Status.NOT_NULL, emptyField);
//            }
//            String username = "", phone = "";
//            String area_code = Utils.nodeAsTest(node.get("area_code"));// null:邮箱登陆， not null：手机登陆
//            if (Utils.isEmpty(area_code)) {
//                String email = node.get("email").asText();
//                username = email;
//            } else {
//                phone = node.get("phone").asText();
//                username = area_code + phone;
//            }
//            String password = node.get("password").asText();
//            String token = node.get("token").asText();
//            // 1,token校验 token中 checkPhone = phone
//            String checkUsername = tokenService.checkTempToken(token);
//            if (Utils.isEmpty(checkUsername) || !checkUsername.equals(username)) {
//                return Result.no(Status.INVALID, "Token");
//            }
//            // 2,修改密码
//            int resp = Utils.isEmpty(area_code) ? userService.updatePassword(username, DigestUtils.md5Hex(password))
//                    : userService.updatePassword(area_code, phone, DigestUtils.md5Hex(password));
//            return resp > 0 ? Result.OK : Result.NO;// 手机号不存在 或者不对，直接返回操作失败
//        } catch (Exception e) {
//            logger.error("###/user/password/reset", e);
//            return Result.no(Status.SERVER_ERROR);
//        }
//    }
//
//    /*
//     * 注销
//     */
//    @ResponseBody
//    @RequestMapping("logout")
//    public Result logout(HttpServletRequest request) {
//        JsonNode node;
//        try {
//            node = Utils.readParam(request.getInputStream());
//            String ip = Utils.getIP(request);
//            int dType = Utils.getDeviceType(request.getHeader("user-agent"));
//            // 合法的token才能被注销
//            String token = node.get("token").asText();
//            Result res = tokenService.checkToken(token, false);
//            if (res.getCode() != 200) {
//                return res;
//            }
//            // 日志信息没有存 app_version, deviceToken
//            userService.logout(token, ip, dType);
//
//            return Result.ok(true);
//        } catch (Exception e) {
//            logger.error("###/user/logout", e);
//            return Result.no(Status.SERVER_ERROR);
//        }
//
//    }
//
//    /*
//     * 查询用户信息
//     */
//    @ResponseBody
//    @RequestMapping("get")
//    public Result get(HttpServletRequest request) {
//        JsonNode node;
//        try {
//            node = Utils.readParam(request.getInputStream());
//            // 1,token 校验
//            Result res = tokenService.checkToken(node.get("token").asText(), false);
//            if (res.getCode() != 200) {
//                return res;
//            }
//            Map<String, Object> map = userService.get(res.getResult().toString());
//            Utils.removeMapKeys(map, "password", "remember_token"); // 删除多余的属性
//            return Result.ok(map);
//        } catch (Exception e) {
//            logger.error("###/user/get", e);
//            return Result.no(Status.SERVER_ERROR);
//        }
//    }
//
//    /*
//     * 修改用户信息 手机号不允许修改
//     */
//    @ResponseBody
//    @RequestMapping("update")
//    public Result update(HttpServletRequest request) {
//        JsonNode node;
//        Users user;
//        try {
//            boolean isMultipart = ServletFileUpload.isMultipartContent(request);
//            String token = "";
//            if (isMultipart) {
//                token = request.getParameter("token");
//                user = getUser(request);
//            } else {
//                node = Utils.readParam(request.getInputStream());
//                token = node.get("token").asText();
//                user = getUser(node);
//            }
//            // 1,token 校验
//            Result res = tokenService.checkToken(token, false);
//            if (res.getCode() != 200) {
//                return res;
//            }
//            String userId = res.getResult().toString();
//            user.setId(userId);
//            // 2,头像上传
//            userService.addAvatar(request, user);
//            // 3,修改用户信息
//            userService.update(user);
//            // 4，查询修改后的用户信息返回
//            Map<String, Object> map = userService.get(userId);
//            Utils.removeMapKeys(map, "password", "deleted_at", "remember_token");// 删除多余的key
//
//            return Result.ok(map);
//        } catch (Exception e) {
//            logger.error("###/user/update", e);
//            return Result.no(Status.SERVER_ERROR);
//        }
//    }
//
//    /*
//     * 用户信息处理
//     */
//    private UserDto getUser(JsonNode node) {
//        UserDto user = new UserDto();
//        user.setDeviceToken(Utils.nodeAsTest(node.get("deviceToken")));
////		user.setDevice_id(Utils.nodeAsTest(node.get("device_id")));
//        user.setApp_version(Utils.nodeAsTest(node.get("app_version")));
//        user.setArea_code(Utils.nodeAsTest(node.get("area_code")));
//        user.setPhone(Utils.nodeAsTest(node.get("phone")));
//        user.setPassword(Utils.nodeAsTest(node.get("password")));
//        user.setEmail(Utils.nodeAsTest(node.get("email")));
//        user.setNickname(Utils.nodeAsTest(node.get("nickname")));
//        user.setGender(Utils.nodeAsInt(node.get("gender")));
//        user.setStatement(Utils.nodeAsTest(node.get("statement")));
//        return user;
//    }
//
//    private UserDto getUser(HttpServletRequest request) {
//        UserDto user = new UserDto();
//        user.setDeviceToken(request.getParameter("deviceToken"));
////		user.setDevice_id(request.getParameter("device_id"));
//        user.setApp_version(request.getParameter("app_version"));
//        user.setArea_code(request.getParameter("area_code"));
//        user.setPhone(request.getParameter("phone"));
//        user.setPassword(request.getParameter("password"));
//        user.setEmail(request.getParameter("email"));
//        user.setNickname(request.getParameter("nickname"));
//        String gender = request.getParameter("gender");
//        if (!Utils.isEmpty(gender)) {
//            user.setGender(Integer.parseInt(gender));
//        }
//        user.setStatement(request.getParameter("statement"));
//        return user;
//    }
//
//    /*
//     * 重置密码
//     */
//    private Map<String, Object> addPasswordReset(String username, String token) {
//        Map<String, Object> map = Utils.newMap();
//        map.put("username", username);
//        map.put("token", token);
//        tokenService.insertPasswordReset(map);
//        return map;
//    }
}
