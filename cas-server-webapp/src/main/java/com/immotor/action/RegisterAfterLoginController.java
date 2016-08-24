package com.immotor.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import org.jasig.cas.CentralAuthenticationService;
import org.jasig.cas.authentication.UsernamePasswordCredential;
import org.jasig.cas.ticket.TicketException;
import org.jasig.cas.ticket.TicketGrantingTicket;
import org.jasig.cas.web.support.CookieRetrievingCookieGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.immotor.entity.UserInfo;

public class RegisterAfterLoginController extends AbstractController {
    private static final Logger logger = LoggerFactory.getLogger(RegisterAfterLoginController.class);
    @NotNull
    private CentralAuthenticationService centralAuthenticationService;
    @NotNull
    private CookieRetrievingCookieGenerator ticketGrantingTicketCookieGenerator;
    
    protected ModelAndView handleRequestInternal(final HttpServletRequest request,HttpServletResponse response) throws Exception
    {
        ModelAndView signinView=new ModelAndView();
        String username=request.getParameter("username");
        String password=request.getParameter("password");
//        String username="16398526";
//        String password = "123456";
        //此处应根据用户名密码去数据库校验,证实传递的注册用户信息的有效性,代码略
        bindTicketGrantingTicket(username, password, request, response);
//        String viewName=getSignInView(request);
        signinView.setViewName(getSignInView(request));
        return signinView;
    }
    protected void bindTicketGrantingTicket(String loginName, String loginPassword,  HttpServletRequest request, HttpServletResponse response){
        try {
            UsernamePasswordCredential credentials = new UsernamePasswordCredential();
            credentials.setUsername(loginName);
            credentials.setPassword(loginPassword);
            final TicketGrantingTicket tgt = this.centralAuthenticationService.createTicketGrantingTicket(credentials);
            ticketGrantingTicketCookieGenerator.addCookie(request, response, tgt.getId());
        } catch (TicketException te) {
            logger.error("Validate the login name " + loginName + " failure, can't bind the TGT!", te);
        } catch (Exception e){
            logger.error("bindTicketGrantingTicket has exception.", e);
        }
    }
    
    protected String getSignInView(HttpServletRequest request) {
        String service = ServletRequestUtils.getStringParameter(request, "service", "");
        return ("redirect:login" + (service.length() > 0 ? "?service=" + service : ""));
    }
    
    public CentralAuthenticationService getCentralAuthenticationService() {
        return centralAuthenticationService;
    }
    
    public void setCentralAuthenticationService(CentralAuthenticationService centralAuthenticationService) {
        this.centralAuthenticationService = centralAuthenticationService;
    }
    
    public CookieRetrievingCookieGenerator getTicketGrantingTicketCookieGenerator() {
        return ticketGrantingTicketCookieGenerator;
    }
    
    public void setTicketGrantingTicketCookieGenerator(CookieRetrievingCookieGenerator ticketGrantingTicketCookieGenerator) {
        this.ticketGrantingTicketCookieGenerator = ticketGrantingTicketCookieGenerator;
    }
    
}
