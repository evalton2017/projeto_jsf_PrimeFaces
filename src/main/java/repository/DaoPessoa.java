package repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import hibernate.br.HibernateUtil;
import model.UsuarioPessoa;

public class DaoPessoa implements IDaoPessoa{
	
	private EntityManager entityManager = HibernateUtil.getEntityManager();

	@Override
	public UsuarioPessoa ConsultarUsuario(String login, String senha) {
		
		UsuarioPessoa pessoa = null;
		
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		
		pessoa = (UsuarioPessoa) entityManager.createQuery("From U from UsuarioPessoa u where u.login = '"+login+"' and u.senha = '" +senha+ "'").getSingleResult();
			
		transaction.commit();
		entityManager.close();
		
		return pessoa;
	}

}
