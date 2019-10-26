package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

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
	
	public void removerUsuario(UsuarioPessoa pessoa) throws Exception {
		String sqlTelefone = "delete from Telefone where usuario usuarioPessoa = "+pessoa.getId();
		entityManager.createNativeQuery(sqlTelefone).executeUpdate();
		
		sqlTelefone = "delete from Email where usuario usuarioPessoa = "+pessoa.getId();
		entityManager.createNativeQuery(sqlTelefone).executeUpdate();
		
		entityManager.getTransaction().commit();
		
		super.deleteByid(pessoa);
	}

}
