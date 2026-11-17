package org.apml.advantage;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

/** Publishes the Catalog as the application attribute "catalog" for the API servlets and the JSP pages. */
@WebListener
public class CatalogListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent e) {
        e.getServletContext().setAttribute("catalog", new Catalog());
    }
}
