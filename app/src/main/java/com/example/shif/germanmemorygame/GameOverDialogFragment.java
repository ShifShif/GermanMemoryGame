package com.example.shif.germanmemorygame;

import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class GameOverDialogFragment extends DialogFragment implements View.OnClickListener {
    Button quit, stay, playAgain;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.game_over_dialog, null);
        quit = (Button) view.findViewById(R.id.quit_button);
        stay = (Button) view.findViewById(R.id.stay_button);
        playAgain = (Button) view.findViewById(R.id.play_again_button);
        quit.setOnClickListener(this);
        stay.setOnClickListener(this);
        playAgain.setOnClickListener(this);
        setCancelable(false);
        return view;
    }

    @Override
    public void onClick(View view) {

        final String LOG_TAG = "within GameOverFragment";
        Log.v(LOG_TAG, getActivity().toString());

        if (view.getId() == R.id.quit_button) {
            getActivity().finish();//demolish activity
        }
        else if (view.getId() == R.id.stay_button) {
            dismiss();
        }
        else { //view.getId() == R.id.play_again_button
            dismiss();
            getActivity().recreate();
        }
    }
}
