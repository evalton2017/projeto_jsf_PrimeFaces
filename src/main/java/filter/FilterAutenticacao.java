package filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import hibernate.br.HibernateUtil;


@WebFilter(urlPatterns={"/"})
public class FilterAutenticacao implements Filter{

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpSession session = req.getSession();
		
		String usuarioLogado = (String) session.getAttribute("usuarioLogado");
		
		String url = req.getServletPath();
		
		if(!url.equalsIgnoreCase("/login.jsf") && usuarioLogado == null ||
				(usuarioLogado != null && usuarioLogado.isEmpty())) {
			RequestDispatcher dispatcher = request.getRequestDispatcher("/login.xhtml");
			dispatcher.forward(request, response);
			return;
		}else {
			chain.doFilter(request, response);
		}
		
		
	}
	
	@Override
	public void init(FilterConfig arg0) throws ServletException{
		HibernateUtil.getEntityManager();
	}

}