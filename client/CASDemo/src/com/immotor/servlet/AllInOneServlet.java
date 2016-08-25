
package com.immotor.servlet;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import org.jasig.cas.client.authentication.AttributePrincipal;

/**
 * Servlet implementation class AllInOneServlet
 */
@WebServlet("/AllInOneServlet")
public class AllInOneServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public AllInOneServlet() {
        super();
        // TODO Auto-generated constructor stub
        System.out.println("AllInOneServlet-->");
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        System.out.println("-->AllInOneServlet");
        System.out.println("request.getRemoteUser()-->"+request.getRemoteUser());
        if (request.getUserPrincipal() != null) {
            AttributePrincipal principal = (AttributePrincipal) request.getUserPrincipal();
            final Map attributes = principal.getAttributes();
            if (attributes != null) {
                System.out.println("attributes.toString()-->"+attributes.toString());
            } else {
                System.out.println("attributes-->" + attributes);
            }
        } else {
            System.out.println("request.getUserPrincipal()为空" + request.getUserPrincipal());
        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        this.doPost(request, response);

    }

}
