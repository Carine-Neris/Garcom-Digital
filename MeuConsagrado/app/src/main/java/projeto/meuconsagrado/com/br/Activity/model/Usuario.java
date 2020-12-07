package projeto.meuconsagrado.com.br.Activity.model;

import com.google.firebase.database.DatabaseReference;

import projeto.meuconsagrado.com.br.Activity.helper.ConfiguracaoFirebase;

public class Usuario {

    private String idUsuario;
    private String nome;
    private String mesa;

    public Usuario() {
    }

    public void salvar(){

        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference usuarioRef = firebaseRef
                .child("usuarios")
                .child( getIdUsuario() );
        usuarioRef.setValue(this);

    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMesa() {
        return mesa;
    }

    public void setMesa(String Mesa) {
        this.mesa = mesa;
    }
}
