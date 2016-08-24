package org.jasig.services.persondir.support;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.sql.DataSource;
import javax.validation.constraints.NotNull;

import org.jasig.services.persondir.IPersonAttributes;
import org.springframework.jdbc.core.JdbcTemplate;

import com.immotor.common.utils.CacheManager;
import com.immotor.common.utils.CacheResources;
import com.immotor.common.utils.Utils;

/**
 * 
 * @author dylong
 * @since 4.1.17
 *
 */
public class BlogStubPersonAttributeDao extends StubPersonAttributeDao {
    private JdbcTemplate jdbcTemplate;
    @NotNull
    private DataSource dataSource;
    @NotNull
    private String sql;  
    
    /** 
     * @param sql The sql to set. 
     */  
    public void setSql(final String sql) {  
        this.sql = sql;  
    }
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
     * 查询结果返回值.
     *
     * @param uid the datasource to use.
     * @return IPersonAttributes
     */
    public IPersonAttributes getPerson(final String uid) {
//        String sql = "select admin_user_id,email,login,name,phone_number from blc_admin_user where name =? or email=? or login=?";
        Map<String, List<Object>> attributes = Utils.newMap();
        // 先从缓存中获取，不存在则查询数据库，并添加
        if(!CacheManager.hasCache(CacheResources.USERINFO_CACHE+uid)){
            final Map<String, Object> map =this.jdbcTemplate.queryForMap(sql,  new Object[]{uid, uid});
            for(final Entry<String, Object> entry:map.entrySet()){    
                attributes.put(entry.getKey(), Collections.singletonList(entry.getValue()));
            }
            CacheManager.putCacheMap(CacheResources.USERINFO_CACHE+uid, attributes);
        }else{
            // 存在，从缓存中获取
            attributes = CacheManager.getCacheListmap(CacheResources.USERINFO_CACHE+uid);
        }
        return new AttributeNamedPersonImpl(attributes);
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
    
}
