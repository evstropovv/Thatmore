package com.example.thatmore;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Locale;

public class OptionsActivity extends AppCompatActivity{
    private static boolean optionMusic = true;
    private static int optionGameMode = 0;
    private static SharedPreferences preferences;
    private static int textSizeCof = 10;
    private int seekBarStep = 1;
    private int seekBarMax = 20;
    private int seekBarMin = 1;
    Switch swGameType, swSound, swRecord;
    TextView tvTextSize;
    Button btnSaveExit;
    SeekBar seekBarTextSize;
    Record record;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        swGameType = (Switch) findViewById(R.id.swGameType);
        swSound = (Switch) findViewById(R.id.swSound);
        btnSaveExit = (Button)findViewById(R.id.btnSaveExit);
        swRecord = (Switch) findViewById(R.id.swRecord);
        tvTextSize = (TextView) findViewById(R.id.tvTextSize);
        seekBarTextSize = (SeekBar) findViewById(R.id.pbTextSize);

        record = new Record();
        record.init(getBaseContext());

        btnSaveExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (swGameType.isChecked()){
                    optionGameMode = 20;
                }else{
                    optionGameMode = 0;
                }
                if (swSound.isChecked()) {
                    optionMusic  = true;
                } else{
                    optionMusic = false;
                }

                if (swRecord.isChecked()) record.setRecord("Record", 0);

                setOptionsPreferences();
                Intent intent = new Intent(getBaseContext(), MainActivity. class);
                startActivity(intent);
            }
        });
        seekBarTextSize.setMax((seekBarMax - seekBarMin) / seekBarStep);
        seekBarTextSize.setProgress(textSizeCof);
        seekBarTextSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { //дерганье ползунком. Изменение размера текста.
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int value = seekBarMin + (progress * seekBarStep);
                setTextSize(value);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {


            }
        });


        //adapter для выпадающего списка доступных языков
        setfonts();
        setOptions();
        loadOptionsPreferences();
    }

    private void setOptions() {
        swSound.setChecked(optionMusic);
        if (optionGameMode == 20) {
            swGameType.setChecked(true);
        }else {
            swGameType.setChecked(false);
        }



    }

    public static boolean getOptionMusic(){
        return optionMusic;
    }
    public static int getOptionGameMode(){
        return optionGameMode;
    }



    private void loadOptionsPreferences(){
        preferences = getPreferences(MODE_PRIVATE);
        optionGameMode = preferences.getInt("optionGameMode", 0);
        optionMusic = preferences.getBoolean("optionMusic",true );
        textSizeCof = preferences.getInt("textSizeCof", textSizeCof);
    }
    private  void setOptionsPreferences(){
        preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = preferences.edit();
        ed.putInt("optionGameMode", optionGameMode);
        ed.putBoolean("optionMusic", optionMusic);
        ed.putInt("textSizeCof", textSizeCof);
        ed.apply();
    }

    private void setfonts() {
        String fontPath = "fonts/Inform Cyr Bold.ttf";
        Typeface typeface = Typeface.createFromAsset(getAssets(), fontPath);
        swGameType.setTypeface(typeface);
        swGameType.setTextSize(OptionsActivity.getTextSize()*2*getResources().getDisplayMetrics().density);
        swSound.setTypeface(typeface);
        swSound.setTextSize(OptionsActivity.getTextSize()*2*getResources().getDisplayMetrics().density);
        btnSaveExit.setTypeface(typeface);
        btnSaveExit.setTextSize(OptionsActivity.getTextSize()*1*getResources().getDisplayMetrics().density);
        swRecord.setTypeface(typeface);
        swRecord.setTextSize(OptionsActivity.getTextSize()*2*getResources().getDisplayMetrics().density);
        tvTextSize.setTypeface(typeface);
        tvTextSize.setTextSize(OptionsActivity.getTextSize()*2*getResources().getDisplayMetrics().density);
    }
    private static void setTextSize(int textSizeCof){
        OptionsActivity.textSizeCof = textSizeCof;
    }
    public static int getTextSize(){

        return textSizeCof;
    }

}
