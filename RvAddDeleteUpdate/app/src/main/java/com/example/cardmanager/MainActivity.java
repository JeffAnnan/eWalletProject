package com.example.cardmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //Création d'une instance de la classe CardBDD accessbile depuis les autres Activities
    public static CardBDD cardBdd;

    //arrayList stockant les cartes pour la vue en scroll
    ArrayList<Model> models = new ArrayList<Model>();
    RecyclerView rvTechSolPoint;
    RvAdapter rvAdapter;
    TextView tvAdd, tvUpdate;
    EditText etEnterName;
    int position;
    //cle pour l'intent et communiquer des donnees avec les autres activities
    final String EXTRA_TEXT="card_to_display";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //boutton ajouter carte
        final ImageView addButton=findViewById(R.id.img_add);
        rvTechSolPoint = findViewById(R.id.rv_list_item);
        rvTechSolPoint.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rvTechSolPoint.setLayoutManager(layoutManager);
        rvAdapter = new RvAdapter(getApplicationContext(), models,
                new RvAdapter.Onclick() {
                    @Override
                    public void onEvent(Model model, int pos) {
                        position = pos;
                        //pour passer à la consultation d'une carte. On passe dans l'Intent l'ID de la carte en BDD
                        Intent intent = new Intent(MainActivity.this, CardViewActivity.class);
                        intent.putExtra(EXTRA_TEXT,model.getBddIdCard());
                        startActivity(intent);

                    }
                });
        //on utilise un RecyclerView avec un Adapter
        rvTechSolPoint.setAdapter(rvAdapter);


        //////// G E S T I O N   B D D //////B E L O W/////////
        cardBdd= new CardBDD(this);
        //On ouvre la base de données pour écrire dedans
        cardBdd.open();

        //afficher toutes les cartes

        //emplacement des logos des enseignes
        final ImageView logoPicture = findViewById(R.id.img_logoCard);

        //passage avec un increment pour savoir si la carte existe puis on l'affiche
        final int maxNumberCards = 30;
        for (int i=1; i<maxNumberCards;i++){
            if (null!=cardBdd.getCardWithId(i) ){
                //on l'insert
                insertMethod(String.valueOf(cardBdd.getCardWithId(i).getCardName()), cardBdd.getCardWithId(i).getAdrLogo(), cardBdd.getCardWithId(i).getIdCard());
            }
        }
        //////// G E S T I O N   B D D //////A B O V E/////////


        //aller sur la page ajout de carte
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //on utilise in Intent pour passer a l'Activity AddCardActivity
                Intent intent = new Intent(MainActivity.this, AddCardActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View view) {

    }

    private void insertMethod(String name, String logoName, Integer bddIdCard) {
        Gson gson = new Gson();
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", name);
            jsonObject.put("logoName", logoName);
            jsonObject.put("bddIdCard", bddIdCard);
            Model model = gson.fromJson(String.valueOf(jsonObject), Model.class);
            //on stock ces cartes pour pouvoir les afficher et les adapter en "CardView" par la classe Adapter
            models.add(model);
            rvAdapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
