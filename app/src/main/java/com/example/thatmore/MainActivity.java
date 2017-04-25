package com.example.thatmore;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Button newGame, endGame, btnOptions;
    TextView tvRecord;
    Record record;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        newGame = (Button) findViewById(R.id.newGame);
        endGame = (Button) findViewById(R.id.exit);
        btnOptions = (Button) findViewById(R.id.btnOption);
        tvRecord = (TextView) findViewById(R.id.tvRecord);
        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startGame = new Intent(v.getContext(), GameActivity.class);
                startActivity(startGame);
            }
        });
        record = new Record();
        record.init(getBaseContext());
        btnOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startOptions = new Intent(v.getContext(), OptionsActivity.class);
                startOptions.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(startOptions);
                finish();
            }
        });

        endGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setfonts();
        try{
            tvRecord.setText(getString(R.string.record) + Record.getRecord("Record")+"");
        }catch (NullPointerException e){
            tvRecord.setText(getString(R.string.record) + "0"+"");
        }
    }

    private void setfonts() {
        String fontPath = "fonts/Inform Cyr Bold.ttf";
        Typeface typeface = Typeface.createFromAsset(getAssets(), fontPath);
        newGame.setTypeface(typeface);
        endGame.setTypeface(typeface);
        btnOptions.setTypeface(typeface);
        tvRecord.setTypeface(typeface);

        newGame.setTextSize(OptionsActivity.getTextSize()* 3 * getResources().getDisplayMetrics().density);
        endGame.setTextSize(OptionsActivity.getTextSize()* 3 * getResources().getDisplayMetrics().density);
        btnOptions.setTextSize(OptionsActivity.getTextSize()* 3 * getResources().getDisplayMetrics().density);
        tvRecord.setTextSize(OptionsActivity.getTextSize()* 1 * getResources().getDisplayMetrics().density);
    }
}
