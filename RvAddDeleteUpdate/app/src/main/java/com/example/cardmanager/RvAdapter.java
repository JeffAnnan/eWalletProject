package com.example.cardmanager;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;


public class RvAdapter extends RecyclerView.Adapter<RvAdapter.RvViewHolder> {
    Context context;
    ArrayList<Model> models;
    Onclick onclick;

    public interface Onclick {
        void onEvent(Model model,int pos);
    }

    public RvAdapter(Context context, ArrayList<Model> models, Onclick onclick) {
        this.context = context;
        this.models = models;
        this.onclick = onclick;
    }

    View view;

    @Override
    public RvAdapter.RvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        view = inflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        RvViewHolder rvViewHolder = new RvViewHolder(view);
        return rvViewHolder;
    }

    @Override
    public void onBindViewHolder(RvAdapter.RvViewHolder holder, final int position) {
        final Model model = models.get(position);

        if (model.getName() != null) {
            //afficher les données de la carte dans le menu : nom + logo
            holder.itemName.setText(model.getName());

            //affecter les logos connus aux cartes (voir le dossier drawable pour voir les logo connus qui se mettront)
            //recherche de l'identifiant de l'image par le nom de la carte
            int id = context.getResources().getIdentifier(model.getLogoName().toLowerCase().replaceAll(" ",""), "drawable", context.getPackageName());
            //si le nom de la carte ne correpond pas à un logo connu on met une image par défaut
            if(id==0){
                //on met une image par défault à la carte
                int idDefault = context.getResources().getIdentifier("other", "drawable", context.getPackageName());
                holder.logoName.setImageResource(idDefault);
            }else {
                holder.logoName.setImageResource(id);
            }


        }

        //listener sur une carte pour pouvoir clicer dessus et voir ses infos sur CardViewActivity
        holder.llItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onclick.onEvent(model,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    //RecyclerView pour pouvoir scroller er emppiler cartes
    public class RvViewHolder extends RecyclerView.ViewHolder {
        TextView itemName;
        ImageView logoName;
        ImageView removeImg;
        LinearLayout llItem;

        public RvViewHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.tv_name);
            logoName = itemView.findViewById(R.id.img_logoCard);
            //removeImg = itemView.findViewById(R.id.img_remove);
            llItem = itemView.findViewById(R.id.ll_item);
        }
    }
}



