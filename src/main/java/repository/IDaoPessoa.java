package repository;

import model.UsuarioPessoa;

public interface IDaoPessoa {

	UsuarioPessoa ConsultarUsuario(String login,String senha);
}
