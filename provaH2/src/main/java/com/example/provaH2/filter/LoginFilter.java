package com.example.provaH2.filter;

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

        //System.out.println("SONO NEL FILTRO");
        HttpSession session=((HttpServletRequest)request).getSession();
        Boolean logged=(Boolean) session.getAttribute("loggato");
        //System.out.println("LOGGED: "+ logged);

        if(logged== null || logged== false){
            String cod=(String)((HttpServletRequest)request).getParameter("cod");
            //Page.getCurrent().setLocation("/Login");
            //System.out.println(((HttpServletRequest) request).getPathInfo() + "_-_ " +((HttpServletRequest) request).getRequestURI() + "_-_ "+ ((HttpServletRequest) request).getServletPath());
            //RequestDispatcher dispatcher=filterConfig.getServletContext().getRequestDispatcher("/Login");
            //TODO:questo è da aggiustare
            if(cod!=null){
                ((HttpServletResponse)response).sendRedirect("http://localhost:8080/?login=true&cod="+cod);
            }else{
                ((HttpServletResponse)response).sendRedirect("http://localhost:8080/?login=true");

            }
            //dispatcher.forward(request, response);
            return;
        }
        chain.doFilter(request, response);

    }


    public void init(FilterConfig fConfig) throws ServletException {
        filterConfig=fConfig;
    }

}
