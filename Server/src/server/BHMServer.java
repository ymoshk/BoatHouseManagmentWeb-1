package server;

import server.constant.Constants;
import engine.api.EngineContext;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@WebListener
public class BHMServer implements ServletContextListener, HttpSessionListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        servletContextEvent.getServletContext().setAttribute(Constants.engineAtt, EngineContext.getInstance());
        servletContextEvent.getServletContext().setAttribute(Constants.sessionExpMap, new HashMap<String, LocalDateTime>());
        log("server is up");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

    public static void log(String msg) {
        System.out.println("Server: " + msg + " " + LocalDateTime.now());
        System.out.println("-----------------------------------------------------");
    }


    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        log("SESSION CREATED");
        Map<String, LocalDateTime> sessionExpMap = (Map<String, LocalDateTime>) httpSessionEvent.getSession()
                .getServletContext().getAttribute(Constants.sessionExpMap);
        //TODO - לשנות לימים\שבועות
        sessionExpMap.put(httpSessionEvent.getSession().getId(), Constants.getNewSessionExpiredDate());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
//        String sessionId = httpSessionEvent.getSession().getId();
//        EngineContext.getInstance().logout(sessionId);
        log("SESSION DESTROYED");
    }
}
