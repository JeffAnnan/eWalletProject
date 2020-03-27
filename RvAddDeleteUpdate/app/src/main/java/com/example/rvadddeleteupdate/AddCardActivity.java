package com.example.rvadddeleteupdate;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.zxing.client.android.Intents;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.Fragment;


public class AddCardActivity extends AppCompatActivity {
    public final int CUSTOMIZED_REQUEST_CODE = 0x0000ffff;
    private Button scanBtn;
    private TextView formatTxt, contentTxt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_card);

        //clé de passage pour l'intent
        final String EXTRA_TEXT = "card_to_display";


        //les bouton annuler et enregistrer
        ImageView cancel = (ImageView) findViewById(R.id.img_cancel);
        ImageView submit = (ImageView) findViewById(R.id.img_submit);

        //les zones de saisie
        final EditText barCodeNumber = (EditText) findViewById(R.id.inputBarcode);
        final EditText cardName = (EditText) findViewById(R.id.inputCardName);
        //le bouton caméra
        final ImageView takeAScan= (ImageView) findViewById(R.id.img_camera);
        takeAScan.setBackgroundColor(Color.parseColor("#008577"));



        //annulation de la saisie d'une carte
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
                        //on passe l'id de BD de la carte en paramètre de l'intent
                        int lastInsertedId =  MainActivity.cardBdd.getHighestID();

                        //System.out.println(lastInsertedId);

                        Intent intent = new Intent(AddCardActivity.this, CardViewActivity.class);
                        intent.putExtra(EXTRA_TEXT, lastInsertedId);
                        startActivity(intent);


                    }
                }

            }
        });

    }
    public void scanBarcode(View view) {
        new IntentIntegrator(this).initiateScan();
    }



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

    /*
    @Override
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

        IntentResult result = IntentIntegrator.parseActivityResult(resultCode, data);

        if(result.getContents() == null) {
            Intent originalIntent = result.getOriginalIntent();
            if (originalIntent == null) {
                Log.d("MainActivity", "Cancelled scan");
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else if(originalIntent.hasExtra(Intents.Scan.MISSING_CAMERA_PERMISSION)) {
                Log.d("MainActivity", "Cancelled scan due to missing camera permission");
                Toast.makeText(this, "Cancelled due to missing camera permission", Toast.LENGTH_LONG).show();
            }
        } else {
            Log.d("MainActivity", "Scanned");
            Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
        }
    }


    public static class ScanFragment extends Fragment {
        private String toast;

        public ScanFragment() {
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            displayToast();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_scan, container, false);
            Button scan = view.findViewById(R.id.scan_from_fragment);
            scan.setOnClickListener(v -> scanFromFragment());
            return view;
        }

        public void scanFromFragment() {
            IntentIntegrator.forSupportFragment(this).initiateScan();
        }

        private void displayToast() {
            if(getActivity() != null && toast != null) {
                Toast.makeText(getActivity(), toast, Toast.LENGTH_LONG).show();
                toast = null;
            }
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if(result != null) {
                if(result.getContents() == null) {
                    toast = "Cancelled from fragment";
                } else {
                    toast = "Scanned from fragment: " + result.getContents();
                }

                // At this point we may or may not have a reference to the activity
                displayToast();
            }
        }
    }

     */

}
