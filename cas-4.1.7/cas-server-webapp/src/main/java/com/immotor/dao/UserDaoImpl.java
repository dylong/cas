package com.immotor.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import javax.sql.DataSource;
import javax.validation.constraints.NotNull;

import org.jasig.cas.authentication.PreventedException;
import org.jasig.services.persondir.support.AttributeNamedPersonImpl;
import org.jasig.services.persondir.support.StubPersonAttributeDao;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;

import com.immotor.common.utils.CacheManager;
import com.immotor.common.utils.CacheResources;
import com.immotor.common.utils.Utils;
import com.immotor.entity.UserInfo;

//@Repository("userDao")
public class UserDaoImpl extends StubPersonAttributeDao implements IUserDao  {
    private static final int PASSCODE_EXPIRES = 15;
    protected static final String Timestamp = null;
    private JdbcTemplate jdbcTemplate;
    @NotNull
    private DataSource dataSource;
    
    /**
     * Method to set the datasource and generate a JdbcTemplate.
     *
     * @param dataSource the datasource to use.
     */
    public final void setDataSource(final DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.dataSource = dataSource;
    }
    /**
     * Method to return the jdbcTemplate.
    *
    * @return a fully created JdbcTemplate.
    */
   protected final JdbcTemplate getJdbcTemplate() {
       return this.jdbcTemplate;
   }

   protected final DataSource getDataSource() {
       return this.dataSource;
   }
   /**
    * 根据邮箱查询是否存在
    * @param email 邮箱
    * @return 返回该邮箱信息
    */
    @Override
    public Map<String, Object> getByEmail(String email) throws Exception {
        Map<String, Object> map =null;
        if(!CacheManager.hasCache(CacheResources.GET_EMAIL_CACHE+email)){
            String sql = "SELECT id,email,area_code,phone FROM users WHERE email = ?";
            try {
                map =this.jdbcTemplate.queryForMap(sql,  new Object[]{email});
            } catch (final IncorrectResultSizeDataAccessException e) {
                if (e.getActualSize() == 0) {
                    //  未发现与SQL查询
                    map = null;
                } 
            } catch (Exception e) {
                // 在执行查询的SQL异常
                throw new PreventedException("SQL exception while executing query for ", e);
            }
            CacheManager.putCacheMap(CacheResources.GET_EMAIL_CACHE+email,map);
        }else{
            map = CacheManager.getCacheMap(CacheResources.GET_EMAIL_CACHE+email);
        }
        return map;
    }
    
    @Override
    public Map<String, Object> getByPhone(String area_code, String phone) throws Exception {
        Map<String, Object> map = null;
        if (!CacheManager.hasCache(CacheResources.GET_PHONE_CACHE + area_code+phone)) {
            String sql = "SELECT id,email,area_code,phone FROM users WHERE area_code=? and phone = ?";
            try {
                map = this.jdbcTemplate.queryForMap(sql, new Object[] { area_code, phone });

            } catch (final IncorrectResultSizeDataAccessException e) {
                if (e.getActualSize() == 0) {
                    //  未发现与SQL查询
                    map = null;
                }
            } catch (Exception e) {
                // 在执行查询的SQL异常
                throw new PreventedException("SQL exception while executing query for ", e);
            }
            CacheManager.putCacheMap(CacheResources.GET_PHONE_CACHE+area_code+phone,map);
        } else {
            map = CacheManager.getCacheMap(CacheResources.GET_PHONE_CACHE + area_code+phone);
        }
        return map;
    }
    /**
     * 插入验证码到数据库
     */
    public int addPasscodes(String username,String passcode,String scene,Map<String,Object> userMap) throws Exception {
        String sql = "INSERT INTO passcodes(`id`,`username`,`passcode`,`user_id`,`expired`,`time`)VALUES (?,?,?,?,?,?)";
        PreparedStatementSetter pss =  new PreparedStatementSetter() {   
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                
                ps.setString(1, Utils.getUUID()); 
                ps.setString(2, username);   
                ps.setString(3, passcode);
                if(!Utils.isEmpty(userMap)){
                    ps.setString(4, String.valueOf(userMap.get("userid")));
                }else{
                    ps.setString(4, null);   
                }
                Date date = new Date();
                ps.setTimestamp(5, new Timestamp( Utils.dateAdd(date, Calendar.MINUTE, PASSCODE_EXPIRES).getTime()));
                ps.setTimestamp(6, new Timestamp( date.getTime()));
            }   
        };
        return this.jdbcTemplate.update(sql, pss);
    }
    /**
     * 根据用户名查询该用户一天内获取过多少次验证码
     * @param username 用户信息
     * @return 次数
     */
    public int getPasscodesNumber (String username)  throws Exception {
        String sql = "SELECT COUNT(*) num FROM passcodes WHERE username = ? AND TIME>=DATE(NOW()) AND TIME<DATE_ADD(DATE(NOW()),INTERVAL 1 DAY)";
        Map<String, Object> map =null;
        try {
            map = this.jdbcTemplate.queryForMap(sql, new Object[]{username});
            if(!Utils.isEmpty(map)){
                return Integer.parseInt(map.get("num").toString());
            }
        } catch (final IncorrectResultSizeDataAccessException e) {
            if (e.getActualSize() == 0) {
                //  未发现与SQL查询
                return 0;
            } 
        } catch (Exception e) {
            // 在执行查询的SQL异常
            throw new PreventedException("SQL exception while executing query for ", e);
        }
        return 0;
    }
    /**
     * 根据用户和验证码，查询该用户的验证码是有效时间
     * @param username 用户信息
     * @param passcode 验证码
     * @return
     * @throws Exception
     */
    public Map<String, Object> getPasscodesByPass (String username,String passcode)  throws Exception {
        String sql = "SELECT expired num FROM passcodes WHERE username = ? AND passcode =? ";
        Map<String, Object> map =null;
        try {
            map = this.jdbcTemplate.queryForMap(sql, new Object[]{username,passcode});
        } catch (final IncorrectResultSizeDataAccessException e) {
            if (e.getActualSize() == 0) {
                //  未发现与SQL查询
                return map;
            } 
        } catch (Exception e) {
            // 在执行查询的SQL异常
            throw new PreventedException("SQL exception while executing query for ", e);
        }
        return map;
    }
    /**
     * 插入userinfo
     */
    public int addUserInfo(UserInfo user){
        String sql ="INSERT INTO users(id,area_code,phone,password,email,active,created_at,updated_at,device_type,nickname)VALUES (?,?,?,?,?,?,?,?,?,?)";
        Date date = new Date();
       return  this.jdbcTemplate.update(sql,new Object[]{user.getId(),user.getArea_code(),user.getPhone(), user.getPassword(), 
               user.getEmail(), user.getActive(), new Timestamp(date.getTime()), new Timestamp( date.getTime()), user.getDevice_type(), user.getNickname()});
    }
    private String usergetId() {
        // TODO Auto-generated method stub
        return null;
    }
    public int mfyUserPass(UserInfo user){
        String sql ="UPDATE users SET area_code = ?,phone =?,password = ?,email = ?,updated_at = ? WHERE id = ?";
        PreparedStatementSetter pss =  new PreparedStatementSetter() {   
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                
                ps.setString(1, user.getArea_code());   
                ps.setString(2, user.getPhone());
                ps.setString(3, user.getPassword());
                ps.setString(4, user.getEmail());
                Date date = new Date();
                ps.setTimestamp(5, new Timestamp(date.getTime()));
                ps.setString(6, user.getId());
            }   
        };  
        
        if(!Utils.isEmpty(user.getArea_code()) && !Utils.isEmpty(user.getPhone())){
            if (CacheManager.hasCache(CacheResources.USERINFO_CACHE +user.getArea_code()+user.getPhone() )) {
                CacheManager.clearOnly(CacheResources.USERINFO_CACHE +user.getArea_code()+user.getPhone());
            }
        }else if(!Utils.isEmpty(user.getEmail()) ){
            if (CacheManager.hasCache(CacheResources.USERINFO_CACHE +user.getEmail())) {
                CacheManager.clearOnly(CacheResources.USERINFO_CACHE +user.getEmail());
            }
        }
        return this.jdbcTemplate.update(sql, pss);
    }

}
