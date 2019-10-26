package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import hibernate.br.HibernateUtil;
import model.Telefone;
import model.UsuarioPessoa;

public class DaoTelefone  extends DaoGeneric<Telefone> {

	private EntityManager entityManager = HibernateUtil.getEntityManager();
	

	
}
