package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import hibernate.br.HibernateUtil;

public class DaoGeneric <E> {

	private EntityManager entityManager = HibernateUtil.getEntityManager();
	
	public void salvar(E entidade) {
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		entityManager.persist(entidade);
		transaction.commit();
	}
	@SuppressWarnings("unchecked")
	public E pesquisar(E entidade) {
		entityManager.clear();
		Object id = HibernateUtil.getPrimaryKey(entidade);
		E e = (E) entityManager.createQuery("from "+((Class<E>) entidade).getSimpleName()+" where id = "+id).getSingleResult();
		return e;
	}
	
	@SuppressWarnings("unchecked")
	public E buscar(Long id, Class<E> entidade) {
		entityManager.clear();
		E e = (E) entityManager.createQuery("from "+entidade.getSimpleName() +" where id = "+id).getSingleResult();
		return e;
	}
	
	public E salvarAtualiza(E entidade) {
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		E entidadeSalva = entityManager.merge(entidade);
		transaction.commit();
		
		return entidadeSalva;
	}
	
	public void deleteByid(E entidade) {
		
		Object id = HibernateUtil.getPrimaryKey(entidade);
		
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		
		entityManager.createNativeQuery("delete from " + entidade.getClass().getSimpleName().toLowerCase() +
				" where id =" + id).executeUpdate();
		transaction.commit();
		
	}
	
	@SuppressWarnings("unchecked")
	public List<E> listar(Class<E> entidade){
		EntityTransaction transaction  = entityManager.getTransaction();
		transaction.begin();
		
		List<E> lista = entityManager.createQuery("from "+ entidade.getName()).getResultList();
		transaction.commit();
		
		return lista;
	}
	
	public List<E> listarQuant(Class <E> entidade){
		EntityTransaction transaction  = entityManager.getTransaction();
		transaction.begin();
		
		List<E> lista = entityManager.createQuery("from "+ entidade.getName() +" order by nome").setMaxResults(2).getResultList();
		transaction.commit();
		
		return lista;
	}
	
	
	
	
}
