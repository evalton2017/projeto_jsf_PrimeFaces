package hibernate.br;

import java.util.List;

import org.junit.Test;

import dao.DaoGeneric;
import dao.DaoUsuarioPessoa;
import jdk.nashorn.internal.ir.annotations.Ignore;
import model.Telefone;
import model.UsuarioPessoa;

public class TesteHibernate {

	@Test
	public void testHibernateUtil() {
		HibernateUtil.getEntityManager();
	}
	
	@Ignore
	public void testSalvar() {
		DaoGeneric<UsuarioPessoa> dao = new DaoGeneric<UsuarioPessoa>();
		
		UsuarioPessoa pessoa = new UsuarioPessoa();
		pessoa.setNome("Evalton");
		pessoa.setLogin("evalton");
		pessoa.setSobreNome("Gomes");
		pessoa.setSenha("123");
		
		dao.salvar(pessoa);
	}
	
	@Ignore
	public void testBuscar() {
		DaoGeneric<UsuarioPessoa> dao = new DaoGeneric<UsuarioPessoa>();
		
		UsuarioPessoa pessoa = new UsuarioPessoa();
		
		pessoa.setId(17L);
		
		pessoa = dao.pesquisar(pessoa);
		
		System.out.println(pessoa);
	}
	
	@Ignore
	public void busca() {
		DaoGeneric<UsuarioPessoa> dao = new DaoGeneric<UsuarioPessoa>();
		
		UsuarioPessoa pessoa = dao.buscar(17L, UsuarioPessoa.class);
			
		System.out.println(pessoa);
	}
	
	@Ignore
	public void update() {
		DaoGeneric<UsuarioPessoa> dao = new DaoGeneric<UsuarioPessoa>();
				
		UsuarioPessoa pessoa = dao.buscar(19L, UsuarioPessoa.class);
				
		pessoa.setNome("Kacia");
		pessoa.setLogin("kacia");
		pessoa.setSobreNome("Sousa");
		
		dao.salvarAtualiza(pessoa);
	}
	
	@Ignore
	public void delete() {
		DaoGeneric<UsuarioPessoa> dao = new DaoGeneric<UsuarioPessoa>();
		
		UsuarioPessoa pessoa = dao.buscar(17L, UsuarioPessoa.class);
		
		dao.deleteByid(pessoa);
	}
	
	@Ignore
	public void listar() {
		DaoGeneric<UsuarioPessoa> dao = new DaoGeneric<UsuarioPessoa>();
		
		List<UsuarioPessoa> list = dao.listar(UsuarioPessoa.class);
			
		for(UsuarioPessoa usuario :list) {
			System.out.println(usuario);
			System.out.println("-------------------------------");
		}
	}
	
	@Ignore
	public void listarQuery() {
		DaoUsuarioPessoa usuarioDao = new DaoUsuarioPessoa();
		
		List<UsuarioPessoa> list = usuarioDao.buscarNome("Duke");
			
		for(UsuarioPessoa usuario :list) {
			System.out.println(usuario);
			System.out.println("-------------------------------");
		}
	}
	
	@Ignore
	public void testNameQuery() {
		DaoUsuarioPessoa usuarioDao = new DaoUsuarioPessoa();
		
		List<UsuarioPessoa> list = usuarioDao.namedQuery();
			
		for(UsuarioPessoa usuario :list) {
			System.out.println(usuario);
			System.out.println("-------------------------------");
		}
	}
	
	@Ignore
	public void gravaTelefone() {
		DaoGeneric dao = new DaoGeneric();
		
		UsuarioPessoa pessoa = (UsuarioPessoa) dao.buscar(18L, UsuarioPessoa.class);
		
		Telefone telefone = new Telefone();
		telefone.setTipo("residencial");
		telefone.setNumero("61-33391008");
		telefone.setUsuarioPessoa(pessoa);
		
		dao.salvar(telefone);
	}
	
	@Ignore
	public void consultarTelefone() {
		DaoGeneric dao = new DaoGeneric();
		
		UsuarioPessoa pessoa = (UsuarioPessoa) dao.buscar(18L, UsuarioPessoa.class);
		
		
		for(Telefone fone :pessoa.getTelefone()) {
			System.out.println(fone.getNumero());
			System.out.println("-------------------------------");
		}
	}
	

}
