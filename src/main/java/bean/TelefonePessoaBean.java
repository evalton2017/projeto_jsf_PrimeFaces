package bean;


import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.omnifaces.util.Messages;

import dao.DaoTelefone;
import dao.DaoUsuarioPessoa;
import model.Telefone;
import model.UsuarioPessoa;

@ManagedBean(name="telefonePessoaBean")
@ViewScoped
public class TelefonePessoaBean {
	//Model
	private UsuarioPessoa pessoa = new UsuarioPessoa();
	private Telefone telefone = new Telefone();
	//DAO
	private DaoUsuarioPessoa daoPessoa = new DaoUsuarioPessoa();
	private DaoTelefone daoTelefone = new DaoTelefone();
	//Listas
	private List<Telefone> telefones = new ArrayList<>();

	
	
	@PostConstruct
	public void init() {
		String idUser = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idUser");
		pessoa = daoPessoa.buscar(Long.parseLong(idUser), UsuarioPessoa.class);
	}
	
	//METODO PARA SALVAR	
	public void  salvar() {	
		try{
			telefone.setUsuarioPessoa(pessoa);
			daoTelefone.salvar(telefone);
			telefone = new Telefone();
			pessoa = daoPessoa.buscar(pessoa.getId(), UsuarioPessoa.class);
			//FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Dados cadastrados com sucesso", null));	
			Messages.addGlobalInfo("Telefone cadastrado para o usuario "+pessoa.getNome());
			
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar salvar o telefone");
			erro.printStackTrace();
		}
		
	}
	
	//METODO PARA REMOVER TELEFONE
	public void removerTelefone(ActionEvent evento) {		
		try{
			telefone = (Telefone) evento.getComponent().getAttributes().get("fone");
			daoTelefone.deleteByid(telefone);
			pessoa = daoPessoa.buscar(pessoa.getId(), UsuarioPessoa.class);
		//	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Telefone excluído", null));
			Messages.addGlobalInfo("Telefone excluído.");
			
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar salvar o Usuario");
			erro.printStackTrace();
		}
		
		
	}
	
	//	GETS E SETS
	public UsuarioPessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(UsuarioPessoa pessoa) {
		this.pessoa = pessoa;
	}

	public Telefone getTelefone() {
		return telefone;
	}

	public void setTelefone(Telefone telefone) {
		this.telefone = telefone;
	}

	public List<Telefone> getTelefones() {
		return telefones;
	}

	public void setTelefones(List<Telefone> telefones) {
		this.telefones = telefones;
	}
	
	
	



}
