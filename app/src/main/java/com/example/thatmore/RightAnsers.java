package com.example.thatmore;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class RightAnsers extends AppCompatActivity {
    int rightAnswers, dbSize = 0;
    Integer topRecord = 0;
    ImageView ivCup;
    TextView tvAnswers;
    Button gotoMain;
    Record record;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_right_ansers);
        Bundle bundle = getIntent().getExtras();
        initialize(bundle);
        setfonts();
        record = new Record();
        record.init(getBaseContext());
        setText(OptionsActivity.getOptionGameMode());
    }

    private void setfonts() { //применяем шрифт
        String fontPath = "fonts/Inform Cyr Bold.ttf";
        Typeface typeface = Typeface.createFromAsset(getAssets(), fontPath);
        tvAnswers.setTypeface(typeface);
        gotoMain.setTypeface(typeface);
    }

    private void initialize(Bundle bundle) { //инициализируем текстовые поля, кнопочки, принимаем данные из бундла
        if(bundle.getString("rightAnswers")!= null)
        {
            rightAnswers = Integer.parseInt(bundle.getString("rightAnswers"));
        }
        if(bundle.getString("dbSize")!= null)
        {
            dbSize = Integer.parseInt(bundle.getString("dbSize"));
        }
        tvAnswers = (TextView) findViewById(R.id.tvAnswers);
        gotoMain = (Button) findViewById(R.id.gotoMain);
        gotoMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent startMain = new Intent(getBaseContext(), MainActivity.class);
                startMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //флаг удаления активити...
                startActivity(startMain);
            }
        });
        ivCup = (ImageView) findViewById(R.id.ivCup);
    }

    public void setText(int gameMode) {
        switch (gameMode){
            case 20:
                if (rightAnswers == dbSize){
                    tvAnswers.setText(getString(R.string.wonderful)+ "\n" +getString(R.string.question)+ dbSize +"\n"+ getString(R.string.correct_answers) + rightAnswers + " ! ! !");
                } else{
                    tvAnswers.setText(getString(R.string.question)+ dbSize +"\n"+ getString(R.string.correct_answers) + rightAnswers + " !");
                }
                break;
            case 0:
                if (rightAnswers > Record.getRecord("Record")){
                    Record.setRecord("Record", rightAnswers);
                    tvAnswers.setText(getString(R.string.new_record) + "" + "\n"   + getString(R.string.correct_answers) + rightAnswers +" !");
                    // <-----------добавить звук победы)
                    ivCup.setImageResource(R.drawable.goldcup);
                    recordSound();

                } else{
                    tvAnswers.setText(getString(R.string.correct_answers) + rightAnswers +"");
                }
                break;
        }
    }

    private void recordSound() {
        Intent musicIntent = new Intent().putExtra("record", true);
        musicIntent.setClass(this, MusicService.class);
        startService(musicIntent);
        stopService(musicIntent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent ma = new Intent(getBaseContext(),MainActivity.class);
        startActivity(ma);
    }
}
