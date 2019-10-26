package converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import dao.DaoUsuarioPessoa;
import model.UsuarioPessoa;

public class PessoaConverter implements Converter {
	
	DaoUsuarioPessoa dao = new DaoUsuarioPessoa();
	

    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return dao.buscarNome(value);
    }


	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		return ((UsuarioPessoa) value).toString();
	}



	

}
