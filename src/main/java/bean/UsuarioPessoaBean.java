package bean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import org.apache.tomcat.util.codec.binary.Base64;
import org.omnifaces.util.Messages;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

import com.google.gson.Gson;

import dao.DaoEmail;
import dao.DaoUsuarioPessoa;
import dataTableLazy.PesquisaUsuario;
import model.Email;
import model.UsuarioPessoa;

@ManagedBean(name="usuarioPessoaBean")
@ViewScoped
public class UsuarioPessoaBean {
	
	private UsuarioPessoa usuarioPessoa = new UsuarioPessoa();
	private DaoUsuarioPessoa daoPessoa = new DaoUsuarioPessoa();
	private PesquisaUsuario<UsuarioPessoa> pessoas = new PesquisaUsuario<UsuarioPessoa>();
	//private List<UsuarioPessoa> pessoas = new ArrayList<>();
	private BarChartModel barCharModel = new BarChartModel();
	private Email email = new Email();
	private DaoEmail daoEmail = new DaoEmail();
	private String pesquisa;
	
	@PostConstruct
	public void init() {
		pessoas.load(0, 5,null, null, null);
		montarGrafico();
	//	pessoas = daoPessoa.listar(UsuarioPessoa.class);
		
	}
	
	public void montarGrafico() {
		//barCharModel = new BarChartModel();
		ChartSeries userSalario = new ChartSeries();
		for(UsuarioPessoa usuarioPessoa: pessoas.list) {	
			userSalario.set(usuarioPessoa.getNome(), usuarioPessoa.getSalario());
			
		}
		barCharModel.addSeries(userSalario);
		userSalario.setLabel(" Grafico Usuarios");
	}
	
	public void salvar() {
		try{
			daoPessoa.salvarAtualiza(usuarioPessoa);
			pessoas.list.add(usuarioPessoa);
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
	
	//Metodo Upload
	public void upload(FileUploadEvent image) {
		String imagem = "data:image/png;base64," + DatatypeConverter.printBase64Binary(image.getFile().getContents());
		usuarioPessoa.setFoto(imagem);
	}
	
	//Metodo Download 
	public void download() throws IOException {
		Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String fileDownload = params.get("fileDownloadId");
		
		UsuarioPessoa pessoa = daoPessoa.buscar(Long.parseLong(fileDownload), UsuarioPessoa.class);
		byte[] imagem = new Base64().decodeBase64(pessoa.getFoto().split("\\,")[1]);
		
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		
		response.addHeader("Content-Disposition","attachment; fileName=download.png" );
		response.setContentType("application/octet-stream");
		
		response.setContentLength(imagem.length);
		response.getOutputStream().write(imagem);
		
		response.getOutputStream().flush();
		FacesContext.getCurrentInstance().responseComplete();
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
	
	public PesquisaUsuario<UsuarioPessoa> getPessoas() {
		return pessoas;
	}
	
	public void pesquisar() {
		pessoas.pesquisa(pesquisa);
		montarGrafico();
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

	public String getPesquisa() {
		return pesquisa;
	}

	public void setPesquisa(String pesquisa) {
		this.pesquisa = pesquisa;
	}
	
	
	
}
