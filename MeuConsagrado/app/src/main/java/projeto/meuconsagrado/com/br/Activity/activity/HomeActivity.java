 package projeto.meuconsagrado.com.br.Activity.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

import projeto.meuconsagrado.com.br.Activity.adapter.AdapterEmpresa;
import projeto.meuconsagrado.com.br.Activity.helper.ConfiguracaoFirebase;
import projeto.meuconsagrado.com.br.Activity.listener.RecyclerItemClickListener;
import projeto.meuconsagrado.com.br.Activity.model.Empresa;
import projeto.meuconsagrado.com.br.R;

 public class HomeActivity extends AppCompatActivity {

     private FirebaseAuth autenticacao;
     private MaterialSearchView searchView;
     private RecyclerView recyclerEmpresa;
     private List<Empresa> empresas = new ArrayList<>();
     private DatabaseReference firebaseRef;
     private AdapterEmpresa adapterEmpresa;

     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_home);

         inicializarComponentes();
         firebaseRef = ConfiguracaoFirebase.getFirebase();
         autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

         //Configurações Toolbar
         Toolbar toolbar = findViewById(R.id.toolbar);
         toolbar.setTitle("Ifood");
         setSupportActionBar(toolbar);

         //Configura recyclerview
         recyclerEmpresa.setLayoutManager(new LinearLayoutManager(this));
         recyclerEmpresa.setHasFixedSize(true);
         adapterEmpresa = new AdapterEmpresa(empresas);
         recyclerEmpresa.setAdapter( adapterEmpresa );

         //Recupera empresas
         recuperarEmpresas();

         //Configuração do search view
         searchView.setHint("Pesquisar restaurantes");
         searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
             @Override
             public boolean onQueryTextSubmit(String query) {
                 return false;
             }

             @Override
             public boolean onQueryTextChange(String newText) {
                 pesquisarEmpresas( newText );
                 return true;
             }
         });

         //configuraração evento de clique
         recyclerEmpresa.addOnItemTouchListener(
                 new RecyclerItemClickListener(
                         this,
                         recyclerEmpresa,
                         new RecyclerItemClickListener.OnItemClickListener() {
                             @Override
                             public void onItemClick(View view, int position) {

                                 Empresa empresaSelecionada = empresas.get(position);
                                 Intent i = new Intent(HomeActivity.this, CardapioActivity.class);
                                 i.putExtra("empresa", empresaSelecionada);
                                 startActivity( i );
                             }

                             @Override
                             public void onLongItemClick(View view, int position) {

                             }

                             @Override
                             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                             }
                         }
                 )
         );

     }

     private void pesquisarEmpresas(String pesquisa){

         DatabaseReference empresasRef = firebaseRef
                 .child("empresas");
         Query query = empresasRef.orderByChild("nome")
                 .startAt(pesquisa)
                 .endAt(pesquisa + "\uf8ff" );

         query.addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(DataSnapshot dataSnapshot) {

                 empresas.clear();

                 for (DataSnapshot ds: dataSnapshot.getChildren()){
                     empresas.add( ds.getValue(Empresa.class) );
                 }

                 adapterEmpresa.notifyDataSetChanged();

             }

             @Override
             public void onCancelled(DatabaseError databaseError) {

             }
         });
     }

     private void recuperarEmpresas(){

         DatabaseReference empresaRef = firebaseRef.child("empresas");
         empresaRef.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(DataSnapshot dataSnapshot) {

                 empresas.clear();

                 for (DataSnapshot ds: dataSnapshot.getChildren()){
                     empresas.add( ds.getValue(Empresa.class) );
                 }

                 adapterEmpresa.notifyDataSetChanged();

             }

             @Override
             public void onCancelled(DatabaseError databaseError) {

             }
         });

     }

     @Override
     public boolean onCreateOptionsMenu(Menu menu) {

         MenuInflater inflater = getMenuInflater();
         inflater.inflate(R.menu.menu_usuario, menu);

         //Configurar botao de pesquisa
         MenuItem item = menu.findItem(R.id.menuPesquisa);
         searchView.setMenuItem(item);

         return super.onCreateOptionsMenu(menu);
     }

     @Override
     public boolean onOptionsItemSelected(MenuItem item) {

         switch (item.getItemId()){
             case R.id.menuSair :
                 deslogarUsuario();
                 break;
             case R.id.menuConfiguracoes :
                 abrirConfiguracoes();
                 break;
         }

         return super.onOptionsItemSelected(item);
     }

     private void inicializarComponentes(){
         searchView = findViewById(R.id.materialSearchView);
         recyclerEmpresa = findViewById(R.id.recyclerEmpresa);
     }

     private void deslogarUsuario(){
         try {
             autenticacao.signOut();
             finish();
         }catch (Exception e){
             e.printStackTrace();
         }
     }

     private void abrirConfiguracoes(){
         startActivity(new Intent(HomeActivity.this, ConfiguracoesUsuarioActivity.class));
     }

 }
