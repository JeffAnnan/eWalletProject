package com.example.cardupdate;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;



public class AddCardActivity extends AppCompatActivity {
    public final int CUSTOMIZED_REQUEST_CODE = 0x0000ffff;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_card);

        //clé de passage entre les vues pour l'intent
        final String EXTRA_TEXT = "card_to_display";

        //les bouton annuler et enregistrer
        ImageView cancel = (ImageView) findViewById(R.id.img_cancel);
        ImageView submit = (ImageView) findViewById(R.id.img_submit);

        //les zones de saisies manuelles du numero du code barre et du nom de la carte
        final EditText barCodeNumber = (EditText) findViewById(R.id.inputBarcode);
        final EditText cardName = (EditText) findViewById(R.id.inputCardName);

        //le bouton camera pour commencer le scannne
        final ImageView takeAScan= (ImageView) findViewById(R.id.img_camera);
        takeAScan.setBackgroundColor(Color.parseColor("#008577"));

        //annulation de la saisie d'une carte retour au menu
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddCardActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        //enregistrer la carte
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //on valide si les deux champs sont remplis
                if (!barCodeNumber.getText().toString().replaceAll(" ","").isEmpty()){
                    if (!cardName.getText().toString().replaceAll(" ","").isEmpty()){
                        //on génère une nouvelle carte
                        Card newCard = new Card(barCodeNumber.getText().toString().replaceAll(" ",""), cardName.getText().toString(), cardName.getText().toString().replaceAll(" ",""));
                        MainActivity.cardBdd.insertCard(newCard);

                        //on se dirige vers la vue de consultation de la carte
                        //on passe l'id de BD de la carte en paramètre de l'intent, on cherche donc dans la table le dernier ID qui a ete insere comme cette atrribut se fait en autoincrement
                        int lastInsertedId =  MainActivity.cardBdd.getHighestID();

                        //intent pour passer a la vue de consultation
                        Intent intent = new Intent(AddCardActivity.this, CardViewActivity.class);
                        intent.putExtra(EXTRA_TEXT, lastInsertedId);
                        startActivity(intent);


                    }
                }

            }
        });

    }
    //relie a l'ImageView de l'appareil photo par android:onClick="scanBarcode" dans le layout add_card
    public void scanBarcode(View view) {
        //la camera s'ouvre
        new IntentIntegrator(this).initiateScan();
    }


    //traitement du resultat
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != CUSTOMIZED_REQUEST_CODE && requestCode != IntentIntegrator.REQUEST_CODE) {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
        switch (requestCode) {
            case CUSTOMIZED_REQUEST_CODE: {
                Toast.makeText(this, "REQUEST_CODE = " + requestCode, Toast.LENGTH_LONG).show();
                break;
            }
            default:
                break;
        }

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if(result.getContents() == null) {
            IntentResult originalIntent = result;
            if (originalIntent == null) {
                Log.d("MainActivity", "Cancelled scan");
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            }
        } else {
            Log.d("MainActivity", "Scanned");

            //remplir la zone de saisie manuelle du numéro de codeBarre
            final EditText barCodeNumber = (EditText) findViewById(R.id.inputBarcode);
            barCodeNumber.setText(result.getContents());
        }
    }
}
