package com.example.rvadddeleteupdate;

import android.content.Context;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.rvadddeleteupdate.MainActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.Writer;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.Hashtable;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;




public class CardViewActivity extends AppCompatActivity {
    ImageView screenShot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_view_details);

        Context context=this;
        final String EXTRA_TEXT = "card_to_display";
        final TextView cardName=findViewById(R.id.cardName);
        final TextView barcodeNumber=findViewById(R.id.barcodeNumber);
        final ImageView cardLogo=findViewById(R.id.img_logoCard);
        final ImageView removeButton=findViewById(R.id.img_remove);
        final ImageView homeButton=findViewById(R.id.img_home);
        final ImageView bareCodeGenerator=findViewById(R.id.img_barecode);





        //on récupère l'intent
        Intent intent = getIntent();
        //on récupère la carte correspondante grâce à l'ID en BDD
        final int cardId=intent.getIntExtra(EXTRA_TEXT, 0);
        final Card correspondingCard=MainActivity.cardBdd.getCardWithId(cardId);

        //System.out.println(correspondingCard.toString());

        //mettre le nom de la carte
        cardName.setText(correspondingCard.getCardName());

        //mettre l'image de la carte
        int id = context.getResources().getIdentifier(correspondingCard.getCardName().toLowerCase().replaceAll(" ",""), "drawable", context.getPackageName());
        //si le nom de la carte ne correpond pas à un logo connu on met une image par défaut
        if(id==0){
            //on met une image par défault à la carte
            int idDefault = context.getResources().getIdentifier("other", "drawable", context.getPackageName());
            cardLogo.setImageResource(idDefault);
        }else {
            cardLogo.setImageResource(id);
        }

        //mettre le code bare format EAN 128
        try {
            //lettres en majuscule et pas d'espaces
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
                                MainActivity.cardBdd.removeCardWithID(cardId);

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
                Intent intent = new Intent(CardViewActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });










/*
        final EditText saisie = findViewById(R.id.editText);
        final TextView vue = findViewById(R.id.textView);
        final String EXTRA_TEXT="text_to_display";


        button = (Button)findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                vue.setText(saisie.getText());
            }

        });


        next = (Button)findViewById(R.id.button2);

        next.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View v){

                Intent intent = new Intent(MainActivity.this, TextDisplayActivity.class);
                intent.putExtra(EXTRA_TEXT, saisie.getText().toString());
                startActivity(intent);

            }


        });

*/

    }
    public void sendThisCard(View view) {
        final String EXTRA_TEXT = "card_to_display";
        //on récupère l'intent
        Intent intent = getIntent();
        //on récupère la carte correspondante grâce à l'ID en BDD
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
