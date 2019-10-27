package bean;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;

import org.omnifaces.util.Messages;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

import com.google.gson.Gson;

import dao.DaoEmail;
import dao.DaoUsuarioPessoa;
import model.Email;
import model.UsuarioPessoa;

@ManagedBean(name="usuarioPessoaBean")
@ViewScoped
public class UsuarioPessoaBean {
	
	private UsuarioPessoa usuarioPessoa = new UsuarioPessoa();
	private DaoUsuarioPessoa daoPessoa = new DaoUsuarioPessoa();
	private List<UsuarioPessoa> pessoas = new ArrayList<>();
	private BarChartModel barCharModel = new BarChartModel();
	private Email email = new Email();
	private DaoEmail daoEmail = new DaoEmail();
	
	
	@PostConstruct
	public void init() {
		pessoas = daoPessoa.listar(UsuarioPessoa.class);
		
		ChartSeries userSalario = new ChartSeries("Salario dos Usuarios");
		
		for(UsuarioPessoa usuarioPessoa: pessoas) {	
			userSalario.set(usuarioPessoa.getNome(), usuarioPessoa.getSalario());
			
		}
		barCharModel.addSeries(userSalario);
		userSalario.setLabel("Usuarios");
	}
	
	public void salvar() {
		try{
			daoPessoa.salvar(usuarioPessoa);
			//FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Dados cadastrados com sucesso", null));
			getPessoas();
			usuarioPessoa=new UsuarioPessoa();
			init();
			Messages.addGlobalInfo("Usuario cadastrado com sucesso.");	
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar salvar o Usuario");
			erro.printStackTrace();
		}
	
	}
	
	public String novo() {
		usuarioPessoa = new UsuarioPessoa();
		return "";
	}
	
	public void addEmail() {
		try{
			email.setUsuarioPessoa(usuarioPessoa);
			email = daoEmail.salvarAtualiza(email);
			usuarioPessoa.getEmail().add(email);
			email = new Email();
			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO, "Resultado...", "Salvo com sucesso!"));
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar salvar o Usuario");
			erro.printStackTrace();
		}
		
		
	}
	
	public void removerEmail() {
		try{
			String idEmail = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("codigoEmail");
			Email remover = new Email();
			remover.setId(Long.parseLong(idEmail));
			daoEmail.deleteByid(remover);
			usuarioPessoa.getEmail().remove(remover);
			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_WARN, "Resultado...", "Email excluído!"));
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar salvar o Usuario");
			erro.printStackTrace();
		}
			
	}
	
	public List<UsuarioPessoa> getPessoas() {
		pessoas = daoPessoa.listar(UsuarioPessoa.class);
		return pessoas;
	}
	
	public void remover(ActionEvent evento) throws Exception {		
		try{
			usuarioPessoa = (UsuarioPessoa) evento.getComponent().getAttributes().get("user");
			daoPessoa.removerUsuario(usuarioPessoa);
			usuarioPessoa = new UsuarioPessoa();
			init();
			Messages.addGlobalWarn("Usuario excluído.");			
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar excluir");
			erro.printStackTrace();
		}
		
	}
	
	public void pesquisaCep(AjaxBehaviorEvent event) {
		try {
			
			URL url = new URL ("https://viacep.com.br/ws/"+usuarioPessoa.getCep()+"/json/");
			URLConnection connection = url.openConnection();
			InputStream is = connection.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
			
			String cep = "";
			StringBuilder jsonCep = new StringBuilder();
			
			while((cep = br.readLine()) != null) {
				jsonCep.append(cep);
			}
			
			UsuarioPessoa userCepPessoa = new Gson().fromJson(jsonCep.toString(), UsuarioPessoa.class);
			usuarioPessoa.setCep(userCepPessoa.getCep());
			usuarioPessoa.setLocalidade(userCepPessoa.getLocalidade());
			usuarioPessoa.setLogradouro(userCepPessoa.getLogradouro());
			usuarioPessoa.setBairro(userCepPessoa.getBairro());
			usuarioPessoa.setUf(userCepPessoa.getUf());
			usuarioPessoa.setUnidade(userCepPessoa.getUnidade());
			usuarioPessoa.setComplemento(userCepPessoa.getComplemento());
						
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public String teste() {
		
		System.out.println("Teste");
		//String lista [] = request.getParameterValues("atividadeModulo");		
		return "";
	}

	
	//GETS E SETS
	
	public UsuarioPessoa getUsuarioPessoa() {
		return usuarioPessoa;
	}
	
	public void setUsuarioPessoa(UsuarioPessoa usuarioPessoa) {
		this.usuarioPessoa = usuarioPessoa;
	}
	
	public BarChartModel getBarCharModel() {
		return barCharModel;
	}

	public void setBarCharModel(BarChartModel barCharModel) {
		this.barCharModel = barCharModel;
	}

	public Email getEmail() {
		return email;
	}

	public void setEmail(Email email) {
		this.email = email;
	}
	
	
}
