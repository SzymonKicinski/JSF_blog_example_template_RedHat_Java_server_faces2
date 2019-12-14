package security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SecurityCheckFilter implements Filter {
	private ServletContext servletContext;
	String publicRes;
	String loginPage;

	private static final String FACES_REDIRECT_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+ "<partial-response><redirect url=\"%s\"></redirect></partial-response>";
	
	@Override
	public void init(FilterConfig config) throws ServletException {
		servletContext = config.getServletContext();
		publicRes = config.getInitParameter("publicResource");
		if (publicRes == null) {
			publicRes = "/public";
		}
		loginPage = config.getInitParameter("loginPage");
		if (loginPage == null) {
			loginPage = "/login.jsf";
		}
	}
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		
		HttpSession session = request.getSession();
		NoteUser u = (NoteUser) session.getAttribute("user");

		boolean pass = false;

		if (u == null) {
			String path = request.getServletPath();
			if (path.startsWith(publicRes) || path.startsWith(loginPage)) {
				pass = true;
			}
		} else {
			pass = true;
		}
		
		if (!pass) {
			
			if ("partial/ajax".equals(request.getHeader("Faces-Request"))) {
				res.setContentType("text/xml");
				res.setCharacterEncoding("UTF-8");
				res.getWriter().printf(FACES_REDIRECT_XML,
						request.getContextPath() + "/");
			} else {				
				servletContext.getRequestDispatcher(loginPage).forward(request,
						response);
			}

		} else {
			chain.doFilter(request, response);
		}

	}

	@Override
	public void destroy() {
	}
}
