package com.immotor.service;

import java.util.Map;

import com.immotor.entity.UserInfo;


public interface UserService {
    public Map<String,Object> getByEmail(String email)  throws Exception ;
    /**
     * 
     * @param area_code .
     * @param phone .
     * @return . 
     * @throws Exception.
     */
    Map<String, Object> getByPhone(String area_code, String phone)throws Exception;
    
    /**
     * 
     * @param username .
     * @param passcode .
     * @param scene .
     * @param userMap .
     * @return .
     * @throws Exception .
     */
    int getPasscodes(String username, String passcode, String scene, Map<String, Object> userMap) throws Exception;
    /**
     * 
     * @param user .
     * @return . 
     */
    int addUserInfo(UserInfo user);
    /**
     * 
     * @param user .
     * @return .
     */
    int mfyUserPass(UserInfo user);
    
    
}
