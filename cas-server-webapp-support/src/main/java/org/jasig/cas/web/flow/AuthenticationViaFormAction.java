/*
 * Licensed to Apereo under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Apereo licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License.  You may obtain a
 * copy of the License at the following location:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jasig.cas.web.flow;

import org.apache.commons.lang3.StringUtils;
import org.jasig.cas.CasProtocolConstants;
import org.jasig.cas.CentralAuthenticationService;
import org.jasig.cas.MessageDescriptor;
import org.jasig.cas.authentication.AuthenticationException;
import org.jasig.cas.authentication.Credential;
import org.jasig.cas.authentication.HandlerResult;
import org.jasig.cas.authentication.principal.Service;
import org.jasig.cas.ticket.ServiceTicket;
import org.jasig.cas.ticket.TicketCreationException;
import org.jasig.cas.ticket.TicketException;
import org.jasig.cas.ticket.TicketGrantingTicket;
import org.jasig.cas.ticket.registry.TicketRegistry;
import org.jasig.cas.web.support.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.web.util.CookieGenerator;
import org.springframework.webflow.core.collection.LocalAttributeMap;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * Action to authenticate credential and retrieve a TicketGrantingTicket for
 * those credential. If there is a request for renew, then it also generates
 * the Service Ticket required.
 *
 * @author Scott Battaglia
 * @since 3.0.0.4
 */
public class AuthenticationViaFormAction {

    /**
     *  Authentication success result.身份验证成功的结果.
     */
    public static final String SUCCESS = "success";

    /** Authentication succeeded with warnings from authn subsystem that should be displayed to user. 
     * 身份验证成功的警告authn子系统应该显示给用户。
     * */
    public static final String SUCCESS_WITH_WARNINGS = "successWithWarnings";

    /** Authentication success with "warn" enabled. 
     * 身份验证成功启用了“警告”。
     * */
    public static final String WARN = "warn";

    /** Authentication failure result.
     * 身份验证失败的结果。
     *  */
    public static final String AUTHENTICATION_FAILURE = "authenticationFailure";

    /** Error result. 
     *  错误的结果。
     * */
    public static final String ERROR = "error";

    /** Flow scope attribute that determines if authn is happening at a public workstation.
     * 流作用域的属性决定是否发生在公共authn工作站。
     *  */
    public static final String PUBLIC_WORKSTATION_ATTRIBUTE = "publicWorkstation";

    /** Logger instance. 
     * Logger实例。
     * **/
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /** Core we delegate to for handling all ticket related tasks. 
     * 核心委托我们处理所有机票相关的任务。
     * */
    @NotNull
    private CentralAuthenticationService centralAuthenticationService;

    @NotNull
    private CookieGenerator warnCookieGenerator;


    /**
     * Handle the submission of credentials from the post.
     * 处理提交的凭证。
     * @param context the context
     * @param credential the credential
     * @param messageContext the message context
     * @return the event
     * @since 4.1.0
     */
    public final Event submit(final RequestContext context, final Credential credential,
                              final MessageContext messageContext)  {
        if (!checkLoginTicketIfExists(context)) {
            return returnInvalidLoginTicketEvent(context, messageContext);
        }

        if (isRequestAskingForServiceTicket(context)) {
            return grantServiceTicket(context, credential);
        }

        return createTicketGrantingTicket(context, credential, messageContext);
    }

    /**
     * Tries to to determine if the login ticket in the request flow scope
     * matches the login ticket provided by the request. The comparison
     * is case-sensitive.
     * 试图确定登录票的请求流范围
     * 匹配的登录请求提供的机票。的比较
     * 是区分大小写的。
     * @param context the context
     * @return true if valid
     * @since 4.1.0
     */
    protected boolean checkLoginTicketIfExists(final RequestContext context) {
        final String loginTicketFromFlowScope = WebUtils.getLoginTicketFromFlowScope(context);
        final String loginTicketFromRequest = WebUtils.getLoginTicketFromRequest(context);

        logger.trace("Comparing login ticket in the flow scope [{}] with login ticket in the request [{}]",
                loginTicketFromFlowScope, loginTicketFromRequest);
        return StringUtils.equals(loginTicketFromFlowScope, loginTicketFromRequest);
    }

    /**
     * Return invalid login ticket event.
     * 返回无效登录票事件。
     * @param context the context
     * @param messageContext the message context
     * @return the error event
     * @since 4.1.0
     */
    protected Event returnInvalidLoginTicketEvent(final RequestContext context, final MessageContext messageContext) {
        final String loginTicketFromRequest = WebUtils.getLoginTicketFromRequest(context);
        logger.warn("Invalid login ticket [{}]", loginTicketFromRequest);
        messageContext.addMessage(new MessageBuilder().error().code("error.invalid.loginticket").build());
        return newEvent(ERROR);
    }

    /**
     * Is request asking for service ticket?
     * 请求服务票证请求吗?
     * @param context the context
     * @return true, if both service and tgt are found, and the request is not asking to renew.
     * @since 4.1.0
     */
    protected boolean isRequestAskingForServiceTicket(final RequestContext context) {
        final String ticketGrantingTicketId = WebUtils.getTicketGrantingTicketId(context);
        final Service service = WebUtils.getService(context);
        return (StringUtils.isNotBlank(context.getRequestParameters().get(CasProtocolConstants.PARAMETER_RENEW))
                && ticketGrantingTicketId != null
                && service != null);
    }

    /**
     * Grant service ticket for the given credential based on the service and tgt
     * that are found in the request context.
     * 格兰特为给定的服务票证凭据基于服务和tgt
     * 发现在请求上下文。
     * @param context the context
     * @param credential the credential
     * @return the resulting event. Warning, authentication failure or error.
     * @since 4.1.0
     */
    protected Event grantServiceTicket(final RequestContext context, final Credential credential) {
        final String ticketGrantingTicketId = WebUtils.getTicketGrantingTicketId(context);
        try {
            final Service service = WebUtils.getService(context);
            final ServiceTicket serviceTicketId = this.centralAuthenticationService.grantServiceTicket(
                    ticketGrantingTicketId, service, credential);
            WebUtils.putServiceTicketInRequestScope(context, serviceTicketId);
            putWarnCookieIfRequestParameterPresent(context);
            return newEvent(WARN);
        } catch (final AuthenticationException e) {
            return newEvent(AUTHENTICATION_FAILURE, e);
        } catch (final TicketException e) {
            if (e instanceof TicketCreationException) {
                logger.warn("Invalid attempt to access service using renew=true with different credential. "
                        + "Ending SSO session.");
                this.centralAuthenticationService.destroyTicketGrantingTicket(ticketGrantingTicketId);
            }
            return newEvent(ERROR, e);
        }
    }
    /**
     * Create ticket granting ticket for the given credentials.
     * Adds all warnings into the message context.
     * 创建给定凭证票据授予票。
     * 添加所有警告消息上下文。
     * @param context the context
     * @param credential the credential
     * @param messageContext the message context
     * @return the resulting event.
     * @since 4.1.0
     */
    protected Event createTicketGrantingTicket(final RequestContext context, final Credential credential,
                                               final MessageContext messageContext) {
        try {
            final TicketGrantingTicket tgt = this.centralAuthenticationService.createTicketGrantingTicket(credential);
            WebUtils.putTicketGrantingTicketInScopes(context, tgt);
            final Map<String, Object> usermap =tgt.getAuthentication().getPrincipal().getAttributes();
            if(usermap!=null){
                WebUtils.putUserInfo(context, usermap);
            }
            putWarnCookieIfRequestParameterPresent(context);
            putPublicWorkstationToFlowIfRequestParameterPresent(context);
            if (addWarningMessagesToMessageContextIfNeeded(tgt, messageContext)) {
                return newEvent(SUCCESS_WITH_WARNINGS);
            }
            return newEvent(SUCCESS);
        } catch (final AuthenticationException e) {
            logger.debug(e.getMessage(), e);
            return newEvent(AUTHENTICATION_FAILURE, e);
        } catch (final Exception e) {
            logger.debug(e.getMessage(), e);
            return newEvent(ERROR, e);
        }
    }

    /**
     * Add warning messages to message context if needed.
     * 如果需要添加警告消息,消息上下文。
     * @param tgtId the tgt id
     * @param messageContext the message context
     * @return true if warnings were found and added, false otherwise.
     * @since 4.1.0
     */
    protected boolean addWarningMessagesToMessageContextIfNeeded(final TicketGrantingTicket tgtId, final MessageContext messageContext) {
        boolean foundAndAddedWarnings = false;
        for (final Map.Entry<String, HandlerResult> entry : tgtId.getAuthentication().getSuccesses().entrySet()) {
            for (final MessageDescriptor message : entry.getValue().getWarnings()) {
                addWarningToContext(messageContext, message);
                foundAndAddedWarnings = true;
            }
        }
        return foundAndAddedWarnings;

    }
    /**
     * Put warn cookie if request parameter present.
     * 把warn cookie 如果 request 请求参数。
     * @param context the context
     */
    private void putWarnCookieIfRequestParameterPresent(final RequestContext context) {
        final HttpServletResponse response = WebUtils.getHttpServletResponse(context);

        if (StringUtils.isNotBlank(context.getExternalContext().getRequestParameterMap().get("warn"))) {
            this.warnCookieGenerator.addCookie(response, "true");
        } else {
            this.warnCookieGenerator.removeCookie(response);
        }
    }

    /**
     * Put public workstation into the flow if request parameter present.
     * 把公共workstation站流如果请求参数。
     * @param context the context
     */
    private void putPublicWorkstationToFlowIfRequestParameterPresent(final RequestContext context) {
        if (StringUtils.isNotBlank(context.getExternalContext()
                .getRequestParameterMap().get(PUBLIC_WORKSTATION_ATTRIBUTE))) {
            context.getFlowScope().put(PUBLIC_WORKSTATION_ATTRIBUTE, Boolean.TRUE);
        }
    }

    /**
     * New event based on the given id.
     * 基于给定的新事件id。
     * @param id the id
     * @return the event
     */
    private Event newEvent(final String id) {
        return new Event(this, id);
    }

    /**
     * New event based on the id, which contains an error attribute referring to the exception occurred.
     * 基于id的新事件,其中包含一个错误属性指的是异常发生。
     * @param id the id
     * @param error the error
     * @return the event
     */
    private Event newEvent(final String id, final Exception error) {
        return new Event(this, id, new LocalAttributeMap<Object>("error", error));
    }

    public final void setCentralAuthenticationService(final CentralAuthenticationService centralAuthenticationService) {
        this.centralAuthenticationService = centralAuthenticationService;
    }

    public final void setWarnCookieGenerator(final CookieGenerator warnCookieGenerator) {
        this.warnCookieGenerator = warnCookieGenerator;
    }

    /**
     * Sets ticket registry.
     * 注册表设置机票。
     * @param ticketRegistry the ticket registry. No longer needed as the core service layer
     *                       returns the correct object type. Will be removed in future versions.
     * @deprecated As of 4.1
     */
    @Deprecated
    public void setTicketRegistry(final TicketRegistry ticketRegistry) {
        logger.warn("setTicketRegistry() has no effect and will be removed in future CAS versions.");
    }

    /**
     * Adds a warning message to the message context.
     * 添加一个警告消息到消息上下文。
     * @param context Message context.
     * @param warning Warning message.
     */
    private void addWarningToContext(final MessageContext context, final MessageDescriptor warning) {
        final MessageBuilder builder = new MessageBuilder()
                .warning()
                .code(warning.getCode())
                .defaultText(warning.getDefaultMessage())
                .args(warning.getParams());
        context.addMessage(builder.build());
    }
}
