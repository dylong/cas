package org.jasig.cas.handler;

import java.security.GeneralSecurityException;

import javax.security.auth.login.AccountNotFoundException;
import javax.security.auth.login.FailedLoginException;
import javax.validation.constraints.NotNull;

import org.jasig.cas.adaptors.jdbc.AbstractJdbcUsernamePasswordAuthenticationHandler;
import org.jasig.cas.authentication.HandlerResult;
import org.jasig.cas.authentication.PreventedException;
import org.jasig.cas.authentication.UsernamePasswordCredential;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
/**
 * 
 * @author dylong
 * @since 4.1.7
 */
public class MultiQueryDatabaseAuthenticationHandler extends AbstractJdbcUsernamePasswordAuthenticationHandler {
    @NotNull
    private String sql;  
  
    /** 
     * @param sql The sql to set. 
     */  
    public void setSql(final String sql) {  
        this.sql = sql;  
    }

    @Override
    protected HandlerResult authenticateUsernamePasswordInternal(final UsernamePasswordCredential credential)
throws GeneralSecurityException, PreventedException {
        final String username = getPrincipalNameTransformer().transform(credential.getUsername());  
        final String encryptedPassword = this.getPasswordEncoder().encode(credential.getPassword());
        try {
            final String dbPassword = getJdbcTemplate().queryForObject(this.sql, String.class, username, username);  
            if (!dbPassword.equals(encryptedPassword)) {
                // 密码不匹配的值。
                throw new FailedLoginException("Password does not match value on record.");
            }
        } catch (final IncorrectResultSizeDataAccessException e) {
            if (e.getActualSize() == 0) {
                //  未发现与SQL查询
                throw new AccountNotFoundException(username + " not found with SQL query");
            } else {
                //  发现的多个记录
                throw new FailedLoginException("Multiple records found for " + username);
            }
        } catch (final DataAccessException e) {
            // 在执行查询的SQL异常
            throw new PreventedException("SQL exception while executing query for " + username, e);
        }
        return createHandlerResult(credential, this.principalFactory.createPrincipal(username), null);
    }

}
