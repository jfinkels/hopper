package perseus.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;

import perseus.util.HibernateUtil;

public class SessionCloserFilter implements Filter {

    private static Logger logger = Logger.getLogger(SessionCloserFilter.class);

    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp,
	    FilterChain chain) throws IOException, ServletException {
	chain.doFilter(req, resp);

	logger.trace("About to close session...");
	try {
	    HibernateUtil.closeSession();
	    logger.debug("Session closed!");
	} catch (HibernateException he) {
	    logger.warn("Error closing session", he);
	}	
    }

    public void init(FilterConfig arg0) throws ServletException {
    }

}
