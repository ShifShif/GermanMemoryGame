package com.example.shif.germanmemorygame;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.activity_main_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));

        CardGridAdapter adapter = new CardGridAdapter();
        recyclerView.setAdapter(adapter);
    }

    public void showGameOverDialog() {
        FragmentManager manager = getFragmentManager();
        GameOverDialogFragment gameOverDialog = new GameOverDialogFragment();
        gameOverDialog.show(manager, "game over dialog");
    }

    private class CardGridAdapter extends RecyclerView.Adapter<CardGridAdapter.ItemViewHolder> {

        int stateOfTheGame = 0;

        private List<Integer> words;
        int[] wordDrawableID = {
                R.drawable.das_haus,
                R.drawable.das_haus_text,
                R.drawable.die_banane,
                R.drawable.die_banane_text,
                R.drawable.der_schnee,
                R.drawable.der_schnee_text,
                R.drawable.die_katze,
                R.drawable.die_katze_text,
                R.drawable.die_gurke,
                R.drawable.die_gurke_text,
                R.drawable.die_tomate,
                R.drawable.die_tomate_text,
                R.drawable.der_kopf,
                R.drawable.der_kopf_text,
                R.drawable.die_sonne,
                R.drawable.die_sonne_text
        };

        //map to match between a picture and its respective word
        HashMap<Integer, Integer > cardsPairs = new HashMap<Integer, Integer>(){{
            for (int i =0; (i + 1) < wordDrawableID.length; i+=2) {
                put(wordDrawableID[i], wordDrawableID[i + 1]);
                put(wordDrawableID[i + 1], wordDrawableID[i]);
            }
        }};

        private ItemViewHolder firstCard;
        private ItemViewHolder secondCard;
        private int pairsFound = 0;

        public CardGridAdapter() {

            words = new ArrayList<>();
            for(int drawableId : wordDrawableID) {
                words.add(drawableId);
            }
            Collections.shuffle(words);
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_card, parent, false);
            ItemViewHolder itemViewHolder = new ItemViewHolder(view);
            return itemViewHolder;
        }

        //@Override
        public void onBindViewHolder(final ItemViewHolder holder, int position) {
            holder.opencardpic.setImageResource(words.get(position));
            holder.wordID = words.get(position);
        }

        @Override
        public int getItemCount() {
            return words.size();
        }

        public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            private ImageView coverpic;
            public ImageView opencardpic;
            public int wordID;
            public boolean active = true;

            public ItemViewHolder(View itemView) {
                super(itemView);
                opencardpic = (ImageView) itemView.findViewById(R.id.open_card);
                coverpic = (ImageView) itemView.findViewById(R.id.closed_card);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {

                if(!active) {
                    return;
                }
                if(stateOfTheGame == 0) { //flipping the first card
                    flipOpen(this);
                    firstCard = this;
                    stateOfTheGame = 1;
                    return;
                }

                if(stateOfTheGame == 1) { //flipping a second card
                    flipOpen(this);

                    if(cardsPairs.get(firstCard.wordID) == this.wordID) { //checking if the pair match
                        stateOfTheGame = 0;
                        pairsFound++;
                        Toast.makeText(getApplicationContext(),"Good job!",Toast.LENGTH_SHORT).show();
                        if(pairsFound == 8) {
                            MainActivity.this.showGameOverDialog();
                        }
                    }
                    else { //the cards don't match
                        secondCard = this;
                        stateOfTheGame = 2;
                    }
                    return;
                }

                if(stateOfTheGame == 2) { //flipping a third card
                    flipClose(firstCard);
                    flipClose(secondCard);
                    flipOpen(this);
                    firstCard = this;
                    stateOfTheGame = 1;
                    return;
                }
            }

            private void flipOpen(ItemViewHolder holder) {
                ViewCompat.setAlpha(holder.coverpic, 0);
                holder.opencardpic.animate().alpha(1);
                active = false;
            }

            private void flipClose(ItemViewHolder holder) {
                holder.coverpic.animate().alpha(1);
                holder.opencardpic.animate().alpha(0);
                holder.active = true;
            }
        }
    }
}
