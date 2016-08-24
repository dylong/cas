package com.immotor.dao;

import java.util.Map;

import org.jasig.cas.authentication.PreventedException;

import com.immotor.entity.UserInfo;



public interface IUserDao {
    public Map<String,Object> getByEmail(String email) throws Exception ;
    public Map<String,Object> getByPhone(String area_code,String phone) throws Exception ;
    
    public int addPasscodes(String username,String passcode,String scene,Map<String,Object> userMap)  throws Exception;
    public int getPasscodesNumber (String username)  throws Exception ;
    
    public int addUserInfo(UserInfo user);
    public int mfyUserPass(UserInfo user);
   
}
