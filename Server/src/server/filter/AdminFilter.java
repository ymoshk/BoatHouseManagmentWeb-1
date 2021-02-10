package server.filter;

import engine.api.EngineContext;
import engine.model.rower.Rower;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(urlPatterns = {"/rowers/*", "/boats/*", "/weekly-activities/*"})
public class AdminFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpSession session = ((HttpServletRequest) servletRequest).getSession(false);
        if(session != null) {
            Rower loggedInUser = EngineContext.getInstance().getLoggedInUser(session.getId());

            if (loggedInUser == null || !loggedInUser.isAdmin()) {
                ((HttpServletResponse) servletResponse).sendRedirect("/unapproved");
            } else {
                filterChain.doFilter(servletRequest, servletResponse);
            }
        } else {
            ((HttpServletResponse) servletResponse).sendRedirect("/login");
        }
    }

    @Override
    public void destroy() {

    }
}
