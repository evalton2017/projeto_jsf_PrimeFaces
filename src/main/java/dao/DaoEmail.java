package dao;

import javax.persistence.EntityManager;

import hibernate.br.HibernateUtil;
import model.Email;

public class DaoEmail extends DaoGeneric<Email> {

	private EntityManager entityManager = HibernateUtil.getEntityManager();
}
