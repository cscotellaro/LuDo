package com.example.provaH2.filter;

import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginFilter implements Filter {

    FilterConfig filterConfig;

    public LoginFilter() {}

    public void destroy() {}

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        /*Object log=((HttpServletRequest)request).getSession().getAttribute("logged");
        if(log==null) {
            RequestDispatcher dispatcher=filterConfig.getServletContext().getRequestDispatcher("/Login");
            dispatcher.forward(request, response);
            return;
        }else {
            boolean logged= (boolean)log;
            if(!logged) {
                RequestDispatcher dispatcher=filterConfig.getServletContext().getRequestDispatcher("/login.jsp");
                dispatcher.forward(request, response);
                return;
            }
        }
        */
        System.out.println("SONO NEL FILTRO");
        HttpSession session=((HttpServletRequest)request).getSession();
        Boolean logged=(Boolean) session.getAttribute("loggato");
        System.out.println("LOGGED: "+ logged);

        if(logged== null || logged== false){
            //Page.getCurrent().setLocation("/Login");
            System.out.println(((HttpServletRequest) request).getPathInfo() + "_-_ " +((HttpServletRequest) request).getRequestURI() + "_-_ "+ ((HttpServletRequest) request).getServletPath());
            RequestDispatcher dispatcher=filterConfig.getServletContext().getRequestDispatcher("/Login");
            ((HttpServletResponse)response).sendRedirect("http://localhost:8080/Login");
            //dispatcher.forward(request, response);
            return;
        }
        // pass the request along the filter chain
        chain.doFilter(request, response);

    }


    public void init(FilterConfig fConfig) throws ServletException {
        filterConfig=fConfig;
    }

}
