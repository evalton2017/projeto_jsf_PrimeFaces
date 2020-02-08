package repository;

import javax.persistence.EntityManager;

import hibernate.br.HibernateUtil;
import model.UsuarioPessoa;

public class DaoPessoa implements IDaoPessoa{
	
	private EntityManager entityManager = HibernateUtil.getEntityManager();

	@Override
	public UsuarioPessoa ConsultarUsuario(String login, String senha) {
		UsuarioPessoa pessoa = null;	
		try {				
			entityManager.getTransaction().begin();
			pessoa = (UsuarioPessoa) entityManager.createQuery("FROM UsuarioPessoa where login = '"+login+"' and senha = '" +senha+ "'").getSingleResult();
			entityManager.getTransaction().commit();
			entityManager.close();
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return pessoa;
	}

}
