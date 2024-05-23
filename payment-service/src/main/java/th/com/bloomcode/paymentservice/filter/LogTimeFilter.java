package th.com.bloomcode.paymentservice.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
@WebFilter("/*")
public class LogTimeFilter implements Filter {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        long time = System.currentTimeMillis();
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            time = System.currentTimeMillis() - time;
            logger.info("{}: {} ms ", ((HttpServletRequest) servletRequest).getRequestURI(), time);
        }
    }

    @Override
    public void destroy() {

    }
}
