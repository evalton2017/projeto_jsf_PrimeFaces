package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.transaction.Transactional;

import hibernate.br.HibernateUtil;
import model.UsuarioPessoa;

public class DaoUsuarioPessoa extends DaoGeneric<UsuarioPessoa>{
	
	private EntityManager entityManager = HibernateUtil.getEntityManager();
	
	@SuppressWarnings("unchecked")
	public List<UsuarioPessoa> buscarNome(String nome){
		EntityTransaction transaction  = entityManager.getTransaction();
		transaction.begin();
		
		List<UsuarioPessoa> lista = entityManager.createQuery("from UsuarioPessoa where nome = :nome")
				.setParameter("nome", nome).getResultList();

		return lista;
	}
	
	public List<UsuarioPessoa> namedQuery(){
		List<UsuarioPessoa> lista = entityManager.createNamedQuery("UsuarioPessoa.todos").getResultList();
		return lista;
	}
	
	public List<UsuarioPessoa> pesquisar(String pesquisa){
		Query query =  entityManager.createQuery("from UsuarioPessoa where nome like '%"+pesquisa+"%'");
		
		return query.getResultList();
	}
	
	@Transactional
	public void removerUsuario(UsuarioPessoa pessoa) throws Exception {
		
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
				
		String sqlTelefone = "delete from Email where usuarioPessoa.id = " +pessoa.getId();
		entityManager.createQuery(sqlTelefone).executeUpdate();
		
		sqlTelefone = "delete from Telefone where usuariopessoa_id = " +pessoa.getId();
		entityManager.createNativeQuery(sqlTelefone).executeUpdate();
		
		transaction.commit();
		
		super.deleteByid(pessoa);
	}

}
