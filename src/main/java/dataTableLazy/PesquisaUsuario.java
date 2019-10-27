package dataTableLazy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import dao.DaoUsuarioPessoa;
import hibernate.br.HibernateUtil;
import model.UsuarioPessoa;

public class PesquisaUsuario<T> extends LazyDataModel<UsuarioPessoa> {
	
	private EntityManager entityManager = HibernateUtil.getEntityManager();

	private DaoUsuarioPessoa dao = new DaoUsuarioPessoa();
	public List<UsuarioPessoa> list = new ArrayList<UsuarioPessoa>();
	
	private String sql = "from UsuarioPessoa";
	
	public List<UsuarioPessoa> load(int first, int pageSize, String sortField, SortOrder sortOder, Map<String, Object> filters){
		
		list = entityManager.createQuery(getSql()).setFirstResult(first).setMaxResults(pageSize).getResultList();
		sql = " from UsuarioPessoa";
		
		setPageSize(pageSize);
		
		Integer qtdRegistro = Integer.parseInt(entityManager.createQuery("select count(1)"+getSql()).getSingleResult().toString());
		
		setRowCount(qtdRegistro);
		
		return list;
	}
	
	public String getSql() {
		return sql;
	}
	
	public List<UsuarioPessoa> getList(){
		return list;
	}
	
	public void pesquisa(String pesquisa) {
		sql += " where nome like '%"+pesquisa+"%'";
	}
}
