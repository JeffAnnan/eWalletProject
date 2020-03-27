package com.example.cardupdate;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;
import android.graphics.Bitmap;
import java.util.Hashtable;

//bibliotheques zxing pour la gestion du scanner
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.Writer;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;


public class CardViewActivity extends AppCompatActivity {
    //classe qui affiche une carte selectionnee
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_view_details);

        Context context=this;
        //cle pour l'intent et recuperer les donnes de la vue precedente
        final String EXTRA_TEXT = "card_to_display";

        //les zones d'affichages du nom de la carte et de son numero de code barre
        final TextView cardName=findViewById(R.id.cardName);
        final TextView barcodeNumber=findViewById(R.id.barcodeNumber);

        //zones d'affichages du logo de l'enseinge et du code barre
        final ImageView cardLogo=findViewById(R.id.img_logoCard);
        final ImageView bareCodeGenerator=findViewById(R.id.img_barecode);

        //zones d'affichages de bouton de suppression de la carte et de retour au menu
        final ImageView removeButton=findViewById(R.id.img_remove);
        final ImageView homeButton=findViewById(R.id.img_home);


        //on récupère l'intent
        Intent intent = getIntent();
        //on récupère la carte correspondante grâce à l'ID en BDD pour avoir ses infos
        final int cardId=intent.getIntExtra(EXTRA_TEXT, 0);
        final Card correspondingCard=MainActivity.cardBdd.getCardWithId(cardId);


        //mettre le nom de la carte
        cardName.setText(correspondingCard.getCardName());
        //mettre l'image de la carte
        int id = context.getResources().getIdentifier(correspondingCard.getCardName().toLowerCase().replaceAll(" ",""), "drawable", context.getPackageName());
        //si le nom de la carte ne correpond pas à un logo (son id = 0) connu on met une image par défaut
        if(id==0){
            //on met une image par défaut à la carte
            int idDefault = context.getResources().getIdentifier("other", "drawable", context.getPackageName());
            cardLogo.setImageResource(idDefault);
        }else {
            cardLogo.setImageResource(id);
        }
        //mettre le code bare format EAN 128
        try {
            //mettre les caracteres du nemero de la carte en majuscule et pas d'espaces
            String productId = correspondingCard.getBarcodeNumber().toUpperCase().replaceAll(" ","");
            Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            Writer codeWriter;
            codeWriter = new Code128Writer();
            BitMatrix byteMatrix = codeWriter.encode(productId, BarcodeFormat.CODE_128,400, 200, hintMap);
            int width = byteMatrix.getWidth();
            int height = byteMatrix.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    bitmap.setPixel(i, j, byteMatrix.get(i, j) ? Color.BLACK : Color.WHITE);
                }
            }
            bareCodeGenerator.setImageBitmap(bitmap);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
        //mettre le numéro de la carte
        barcodeNumber.setText(correspondingCard.getBarcodeNumber());

        //supprimer une carte avec un dialogue de confirmation
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CardViewActivity.this);
                alertDialogBuilder.setMessage("Are you sure, You wanted to deleted your favorite "+correspondingCard.getCardName()+" card ?");
                alertDialogBuilder.setPositiveButton("yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                //si yes on supprime la carte
                                MainActivity.cardBdd.removeCardWithID(cardId);
                                //on retourne au menu par un Intent
                                Intent intent = new Intent(CardViewActivity.this, MainActivity.class);
                                startActivity(intent);

                            }
                        });

                alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        //retourner au menu
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //on retourne au menu par le bouton Maison relie au listener par android:onClick="sendThisCard" dans card_view_details
                Intent intent = new Intent(CardViewActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }


    //envoyer la carte
    public void sendThisCard(View view) {
        final String EXTRA_TEXT = "card_to_display";
        //on récupère l'intent
        Intent intent = getIntent();
        //on récupère la carte correspondante grâce à l'ID en BDD, ainsi on peut recuperer le numero de code barre par correspondingCard.getCardName()
        final int cardId=intent.getIntExtra(EXTRA_TEXT, 0);
        final Card correspondingCard=MainActivity.cardBdd.getCardWithId(cardId);

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Voici le code de votre carte de fidélité "+ correspondingCard.getCardName() + " : "+correspondingCard.getBarcodeNumber());
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }



}
