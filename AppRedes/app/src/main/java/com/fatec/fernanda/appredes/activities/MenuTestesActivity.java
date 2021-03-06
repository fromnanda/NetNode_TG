package com.fatec.fernanda.appredes.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.fatec.fernanda.appredes.R;
import com.fatec.fernanda.appredes.fragments.MenuTestesFragment;
import com.fatec.fernanda.appredes.models.Conteudo;
import com.fatec.fernanda.appredes.models.DataWrapper;
import com.fatec.fernanda.appredes.models.Teste;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MenuTestesActivity extends AppCompatActivity {

    DatabaseReference db;
    DatabaseReference usuarioRef;

    ArrayList<Conteudo> conteudos;

    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_conteudos);

        linearLayout = (LinearLayout) findViewById(R.id.linLayoutMenuConteudos);

        conteudos = new ArrayList<>();

        //SETUP FIREBASE
        db = FirebaseDatabase.getInstance().getReference("conteudos");
        usuarioRef = FirebaseDatabase.getInstance().getReference().child("usuarios")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("conteudosConcluidos");

        db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final Conteudo novoConteudo = new Conteudo();

                novoConteudo.setTitulo(dataSnapshot.child("titulo").getValue(String.class));
                novoConteudo.setId(dataSnapshot.getKey());

                final MenuTestesFragment child = new MenuTestesFragment(MenuTestesActivity.this);
                child.setCheckedTextView(novoConteudo.getTitulo());

                //VERIFICAR SE JÁ TERMINOU
                usuarioRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        //se já terminou o conteudo
                        if (novoConteudo.getId().equals(dataSnapshot.getKey()) ) {
                            if (dataSnapshot.getValue(int.class) >= 5) {
                                child.setChecked();
                                child.setClickable(Boolean.FALSE);
                            } else{ //se não terminou o conteudo
                                child.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        for (Conteudo c : conteudos) {
                                            if (c.getTitulo().equals(child.getCheckedTextView())) {
                                                Intent testeIntent = new Intent(MenuTestesActivity.this, TesteActivity.class);

                                                //Enviando id do Conteudo para a nova Activity
                                                testeIntent.putExtra("idConteudo",Integer.parseInt(c.getId().substring(8)));
                                                testeIntent.putExtra("idQuestao", 0);
                                                testeIntent.putExtra("numQuestoes", 0);

                                                ArrayList<Teste> arrayMeuTeste = new ArrayList<>();

                                                DataWrapper dataWrapper = new DataWrapper();
                                                dataWrapper.setArrayTeste(arrayMeuTeste);
                                                testeIntent.putExtra("data", dataWrapper);

                                                MenuTestesActivity.this.startActivity(testeIntent);
                                            }
                                        }
                                    }
                                });
                            }
                            conteudos.add(novoConteudo);

                            try {
                                linearLayout.addView(child);
                            }catch (Exception e){

                            }

                        }



                    }

                @Override
                public void onChildChanged (DataSnapshot dataSnapshot, String s){

                }

                @Override
                public void onChildRemoved (DataSnapshot dataSnapshot){

                }

                @Override
                public void onChildMoved (DataSnapshot dataSnapshot, String s){

                }

                @Override
                public void onCancelled (DatabaseError databaseError){

                }
            }

            );


        }

        @Override
        public void onChildChanged (DataSnapshot dataSnapshot, String s){

        }

        @Override
        public void onChildRemoved (DataSnapshot dataSnapshot){

        }

        @Override
        public void onChildMoved (DataSnapshot dataSnapshot, String s){

        }

        @Override
        public void onCancelled (DatabaseError databaseError){

        }
    }

    );


}

    /*

    ArrayAdapter<String> adapterConteudosTestes;
    ListView conteudosList;
    DatabaseReference db;
    FirebaseHelper helper;

    DatabaseReference questoes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_testes);

        conteudosList = (ListView) findViewById(R.id.menuConteudosTestes);

        //SETUP FIREBASE TODO contar os topicos/conteudos concluidos
        //TODO contar os testes já feitos
        db = FirebaseDatabase.getInstance().getReference("conteudos");
        helper = new FirebaseHelper(db);


        //ADAPTER
        adapterConteudosTestes = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, (List<String>) helper.retrieve());
        conteudosList.setAdapter(adapterConteudosTestes);


        conteudosList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent testeIntent = new Intent(MenuTestesActivity.this, TesteActivity.class);

                //Enviando id do Conteudo para a nova Activity
                testeIntent.putExtra("idConteudo", i + 1);
                testeIntent.putExtra("idQuestao", 1);

                ArrayList<Teste> arrayMeuTeste = new ArrayList<>();

                DataWrapper dataWrapper = new DataWrapper();
                dataWrapper.setArrayTeste(arrayMeuTeste);
                testeIntent.putExtra("data", dataWrapper);

                //TODO Mandar no intent o numero da primeira questão do teste

                MenuTestesActivity.this.startActivity(testeIntent);
            }
        });



    }
    */

    @Override
    public void onBackPressed() {
        startActivity(new Intent(MenuTestesActivity.this, MenuActivity.class));
    }
}
