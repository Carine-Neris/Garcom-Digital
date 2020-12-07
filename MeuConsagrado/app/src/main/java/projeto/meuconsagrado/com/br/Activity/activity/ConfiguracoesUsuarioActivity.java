package projeto.meuconsagrado.com.br.Activity.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import projeto.meuconsagrado.com.br.Activity.helper.ConfiguracaoFirebase;
import projeto.meuconsagrado.com.br.Activity.helper.UsuarioFirebase;
import projeto.meuconsagrado.com.br.Activity.model.Usuario;
import projeto.meuconsagrado.com.br.R;

public class ConfiguracoesUsuarioActivity extends AppCompatActivity {

    private EditText editUsuarioNome, editUsuarioMesa;
    private String idUsuario;
    private DatabaseReference firebaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes_usuario);

        //Configurações iniciais
        inicializarComponentes();
        idUsuario = UsuarioFirebase.getIdUsuario();
        firebaseRef = ConfiguracaoFirebase.getFirebase();

        //Configurações Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Configurações usuário");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Recupera dados do usuário
        recuperarDadosUsuario();

    }

    private void recuperarDadosUsuario(){

        DatabaseReference usuarioRef = firebaseRef
                .child("usuarios")
                .child( idUsuario );

        usuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if( dataSnapshot.getValue() != null ){

                    Usuario usuario = dataSnapshot.getValue(Usuario.class);
                    editUsuarioNome.setText( usuario.getNome() );
                    editUsuarioMesa.setText( usuario.getMesa() );

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void validarDadosUsuario(View view){

        //Valida se os campos foram preenchidos
        String nome = editUsuarioNome.getText().toString();
        String mesa = editUsuarioMesa.getText().toString();

        if( !nome.isEmpty()){
            if( !mesa.isEmpty()){

                Usuario usuario = new Usuario();
                usuario.setIdUsuario( idUsuario );
                usuario.setNome( nome );
                usuario.setMesa( mesa );
                usuario.salvar();

                exibirMensagem("Dados atualizados com sucesso!");
                finish();

            }else{
                exibirMensagem("Digite a mesa a qual está sentado!");
            }
        }else{
            exibirMensagem("Digite seu nome!");
        }

    }

    private void exibirMensagem(String texto){
        Toast.makeText(this, texto, Toast.LENGTH_SHORT)
                .show();
    }

    private void inicializarComponentes(){
        editUsuarioNome = findViewById(R.id.editUsuarioNome);
        editUsuarioMesa = findViewById(R.id.editUsuarioMesa);
    }

}
