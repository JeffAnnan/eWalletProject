package com.example.rvadddeleteupdate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //Création d'une instance de la classe CardBDD
    public static CardBDD cardBdd;

    ArrayList<Model> models = new ArrayList<Model>();
    RecyclerView rvTechSolPoint;
    RvAdapter rvAdapter;
    TextView tvAdd, tvUpdate;
    EditText etEnterName;
    int position;
    final String EXTRA_TEXT="card_to_display";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //boutton ajouter carte
        final ImageView addButton=findViewById(R.id.img_add);


        rvTechSolPoint = findViewById(R.id.rv_list_item);
        /*
        tvAdd = findViewById(R.id.tv_add);
        etEnterName = findViewById(R.id.et_enter_name);
        tvUpdate = findViewById(R.id.tv_update);

         */

        rvTechSolPoint.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rvTechSolPoint.setLayoutManager(layoutManager);
        rvAdapter = new RvAdapter(getApplicationContext(), models,
                new RvAdapter.Onclick() {
                    @Override
                    public void onEvent(Model model, int pos) {

                        position = pos;
                        //tvUpdate.setVisibility(View.VISIBLE);
                        //etEnterName.setText(model.logoName);

                        //pour passer à la consultation d'une carte. On passe dans l'Intent l'ID de la carte en BDD
                        Intent intent = new Intent(MainActivity.this, CardViewActivity.class);
                        intent.putExtra(EXTRA_TEXT,model.getBddIdCard());
                        startActivity(intent);

                    }
                });
        rvTechSolPoint.setAdapter(rvAdapter);

        //tvAdd.setOnClickListener(this);
        //tvUpdate.setOnClickListener(this);


        //////// G E S T I O N   B D D //////B E L O W/////////

        cardBdd= new CardBDD(this);
        cardBdd.open();

/*
        //Création d'une carte

        Card carteMcDo = new Card("123456789", "Mc Donalds", "mcdonalds");
        Card carteAuchan = new Card("33333333", "Auchan", "auchan");
        Card carteCarrefour = new Card("445566787", "Carrefour", "carrefour");
        Card carteZara = new Card("987654321", "Zara", "zara");

        //On ouvre la base de données pour écrire dedans
        cardBdd.open();

        //On insère la carte que l'on vient de créer
        cardBdd.insertCard(carteMcDo);
        cardBdd.insertCard(carteAuchan);
        cardBdd.insertCard(carteCarrefour);
        cardBdd.insertCard(carteZara);

 */

        /*
        Card myOldCard = cardBdd.getCardWithBarcodeNumber("111111111");
        //Si aucune carte n'est retournée, c'est le cas à la première exécution de l'application
        if(myOldCard == null){
            //On affiche un message indiquant que la carte n'existe pas dans la BDD
            Toast.makeText(this, "l'ancienne carte n'existe pas", Toast.LENGTH_LONG).show();
            //Si la carte  existe, c'est le cas à partir de la deuxième exécuton de l'application
        }
        else{
            //on affiche un message indiquant que la carte existe dans la BDD
            Toast.makeText(this, "l'ancienne carte existe", Toast.LENGTH_LONG).show();
        }


        //On insère la carte que l'on vient de créer
        cardBdd.insertCard(carte);

        //Pour vérifier que l'on a bien créé notre carte dans la BDD
        //on extrait la carte de la BDD grâce au numéro de code barre de la carte que l'on a crée précédemment
        Card cardFromBdd = cardBdd.getCardWithId(carte.getBarcodeNumber());

        //Si une carte est retournée (donc si une carte à bien été ajoutée à la BDD)
        if(cardFromBdd != null){
            //On affiche les infos de la carte dans un Toast
            Toast.makeText(this, cardFromBdd.toString(), Toast.LENGTH_LONG).show();
            //On modifie le numéro de la carte
            cardFromBdd.setBarcodeNumber("00000000000");
            //Puis on met à jour la BDD
            cardBdd.updateCard(cardFromBdd.getIdCard(), cardFromBdd);
        }

        //On extrait la carte de la BDD
        cardFromBdd = cardBdd.getCardWithBarcodeNumber("00000000000");
        //S'il existe une carte possédant ce titre dans la BDD
        if(cardFromBdd != null){
            //On affiche les nouvelles informations de la carte pour vérifier que le numéro de la carte a bien été mis à jour
            Toast.makeText(this, cardFromBdd.toString(), Toast.LENGTH_LONG).show();
            //on supprime le livre de la BDD grâce à son ID
            // cardBdd.removeCardWithID(cardFromBdd.getIdCard());
        }
        //On essaye d'extraire de nouveau la crate de la BDD toujours grâce à son nouveau titre
        cardFromBdd = cardBdd.getCardWithBarcodeNumber("00000000000");
        //Si aucun livre n'est retourné
        if(cardFromBdd == null){
            //On affiche un message indiquant que la carte n'existe pas dans la BDD
            Toast.makeText(this, "Cette carte n'existe pas dans la BDD", Toast.LENGTH_LONG).show();
        }
        //Si la carte existe (mais normalement il ne devrait pas)
        else{
            //on affiche un message indiquant que la carte existe dans la BDD
            Toast.makeText(this, "Cette carte existe dans la BDD", Toast.LENGTH_LONG).show();
        }
        //on crée une carte que l'on voudra retrouver à la prochaine exécution de l'application
        Card carte2 = new Card("123456790", "Adidas", "adidadLogo.jpg");
        cardBdd.insertCard(carte2);
        cardBdd.close();

        */





        //afficher toutes les cartes

        final ImageView logoPicture = findViewById(R.id.img_logoCard);


        final int maxNumberCards = 30;
        for (int i=1; i<maxNumberCards;i++){
            if (null!=cardBdd.getCardWithId(i) ){

                insertMethod(String.valueOf(cardBdd.getCardWithId(i).getCardName()), cardBdd.getCardWithId(i).getAdrLogo(), cardBdd.getCardWithId(i).getIdCard());

            }
        }

        //fermer la Bdd
        //cardBdd.close();

        //////// G E S T I O N   B D D //////A B O V E/////////



        //aller sur la page ajout de carte
        //supprimer une carte avec un dialogue de confirmation
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //pour passer à la consultation d'une carte. On passe en Intent l'ID de la carte en BDD
                Intent intent = new Intent(MainActivity.this, AddCardActivity.class);
                startActivity(intent);
            }
        });

}

    @Override
    public void onClick(View view) {
        /*
        switch (view.getId()) {
            case R.id.tv_add: {
                //insertMethod(String.valueOf(etEnterName.getText()));
            }
            break;
            case R.id.tv_update: {
                models.get(position).setName(etEnterName.getText().toString());
                rvAdapter.notifyDataSetChanged();
                tvUpdate.setVisibility(View.GONE);
            }
            break;
        }

         */
    }




    private void insertMethod(String name, String logoName, Integer bddIdCard) {
        Gson gson = new Gson();
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", name);
            jsonObject.put("logoName", logoName);
            jsonObject.put("bddIdCard", bddIdCard);
            Model model = gson.fromJson(String.valueOf(jsonObject), Model.class);
            models.add(model);
            rvAdapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
