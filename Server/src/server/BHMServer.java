package server;

import server.constant.Constants;
import engine.api.EngineContext;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class BHMServer implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        servletContextEvent.getServletContext().setAttribute(Constants.engineAtt, EngineContext.getInstance());
        log("server is up");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

    public static void log(String msg) {
        System.out.println("Server: " + msg);
        System.out.println("-----------------------------------------------------");
    }
}
