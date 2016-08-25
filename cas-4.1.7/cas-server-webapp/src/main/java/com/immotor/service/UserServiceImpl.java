package com.immotor.service;

import java.util.Map;

import com.immotor.dao.IUserDao;
import com.immotor.entity.UserInfo;
/**
 * 
 * @author dylong
 * @since 4.1.7
 *
 */
//@Service("userService")
public class UserServiceImpl implements UserService {
    
//    @Autowired

    private IUserDao userDao;  
    @Override
    public Map<String, Object> getByEmail(final String email) throws Exception  {
        // TODO Auto-generated method stub
        return userDao.getByEmail(email);
    }

    @Override
    public Map<String, Object> getByPhone(String area_code, String phone)  throws Exception {
        // TODO Auto-generated method stub
        return userDao.getByPhone(area_code, phone);
    }
    public int getPasscodes (String username,String passcode,String scene,Map<String,Object> userMap)  throws Exception {
//    public int addPasscodes(String username,String passcode,String scene,Map<String,Object> userMap) throws Exception {
//        return userDao.addPasscodes(username, passcode,scene,userMap); 
        // 该用户获取过多少次验证码，如果大于3次则不然获取。
    	if(userDao.getPasscodesNumber(username)>3){
            return 0;
        }else{
            // 小于三次，则成功并且加入验证码到数据库中
            return userDao.addPasscodes(username, passcode,scene,userMap); 
        }
    }
    public int mfyUserPass(UserInfo user)   {
        return  userDao.mfyUserPass(user);
      }
      
    
    public int addUserInfo(UserInfo user){
        return userDao.addUserInfo(user);
    }
    public IUserDao getUserDao() {
        return userDao;
    }

    
    public void setUserDao(IUserDao userDao) {
        this.userDao = userDao;
    }

}
