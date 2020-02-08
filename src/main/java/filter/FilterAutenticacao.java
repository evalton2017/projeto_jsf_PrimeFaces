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
import model.UsuarioPessoa;


@WebFilter(urlPatterns={"/"})
public class FilterAutenticacao implements Filter{

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpSession session = req.getSession();
		
		UsuarioPessoa usuarioLogado = (UsuarioPessoa) session.getAttribute("usuarioLogado");
		
		String url = req.getServletPath();
		
		if(!url.equalsIgnoreCase("/login.xhtml") && (usuarioLogado != null && usuarioLogado.getId()!=null)) {
			RequestDispatcher dispatcher = request.getRequestDispatcher(url);
			dispatcher.forward(request, response);
			return;
		}else {
			RequestDispatcher dispatcher = request.getRequestDispatcher("/login.xhtml");
			dispatcher.forward(request, response);
			chain.doFilter(request, response);
		}
		
		
	}
	
	@Override
	public void init(FilterConfig arg0) throws ServletException{
		HibernateUtil.getEntityManager();
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
