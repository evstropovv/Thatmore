package com.example.thatmore;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thatmore.database.DatabaseAccess;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import android.os.Handler;

public class GameActivity extends AppCompatActivity  {

    TextView tvQuestion, tvScore, tvVar1, tvVar2, tvRecord;
    ImageView ivVar1, ivVar2, ivAnswerIcon1, ivAnswerIcon2;
    int number = 1; //номер вопроса по порядку
    int rightAnswers = 0; //правильные ответы
    int answer, rQuestion, rQuestion2;  //правильный ответ. 1 - первй варриант, 2 - второй варриант
    int dbSize = 194; //количество стран
    int questionCount = 20; //количество вопросов
    int areaCountry1, areaCountry2;

    private boolean optionMusic = true;
    private int optionGameMode = 0;

    private final Integer COUNTRY_ID = 0;
    private final Integer COUNTRY_NAME_RU = 1;
    private final Integer COUNTRY_AREA = 2;
    private final Integer COUNTRY_NAME_EN = 3;

    private final Integer NEXT_QUESTION = 0;
    private final Integer ANSWER_ONE_RIGHT = 1;
    private final Integer ANSWER_ONE_WRONG = 2;
    private final Integer ANSWER_TWO_RIGHT = 6;
    private final Integer ANSWER_TWO_WRONG = 7;

    ArrayList<String> qList = new ArrayList<>();
    ArrayList<String> qList2 = new ArrayList<>();
    Handler handler;
    Record record;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState!=null && savedInstanceState.containsKey("number") && savedInstanceState.containsKey("rightAnswer")){// Сохранение при повороте экрана.
            number = savedInstanceState.getInt("number");
            rightAnswers = savedInstanceState.getInt("rightAnswer");
            rQuestion = savedInstanceState.getInt("rQuestion");
            rQuestion2 = savedInstanceState.getInt("rQuestion2");
        } else {
            randomQuestions();
        }
        record = new Record();
        record.init(getBaseContext());
        getOptions();
        setContentView(R.layout.activity_game);
        initialize(); //инициализируем все текствью
        nextQuestion(number, rQuestion, rQuestion2); // следующий вопрос по _id

        handler = new Handler(){
          public void handleMessage(android.os.Message msg){
              switch (msg.what){
                  case 0:
                      ivAnswerIcon1.setImageResource(0);
                      ivAnswerIcon2.setImageResource(0);
                      if (optionGameMode == 20 && number < questionCount) {
                          randomQuestions();
                          nextQuestion(number, rQuestion, rQuestion2);
                      } else if (optionGameMode == 20 && questionCount == number){
                          startRightAnswers();
                      } else if (optionGameMode != 20) {
                          randomQuestions();
                          nextQuestion(number, rQuestion, rQuestion2);
                      }
                      break;
                  case 1:
                      ivAnswerIcon1.setImageResource(R.drawable.right_icon);
                      break;
                  case 2:
                      ivAnswerIcon1.setImageResource(R.drawable.not_right_icon);
                      if (optionGameMode != 20) startRightAnswers();
                      break;
                  case 6:
                      ivAnswerIcon2.setImageResource(R.drawable.right_icon);
                      break;
                  case 7:
                      ivAnswerIcon2.setImageResource(R.drawable.not_right_icon);
                      if (optionGameMode != 20) startRightAnswers();
                      break;
                  default:
                      break;
              }
          }
        };
    }

    private void setfonts() {
        String fontPath = "fonts/Inform Cyr Bold.ttf";
        Typeface typeface = Typeface.createFromAsset(getAssets(), fontPath);
        tvScore.setTypeface(typeface);
        tvQuestion.setTypeface(typeface);
        tvVar1.setTypeface(typeface);
        tvVar2.setTypeface(typeface);
        tvRecord.setTypeface(typeface);

        tvQuestion.setTextSize(OptionsActivity.getTextSize()*2*getResources().getDisplayMetrics().density);
        tvVar1.setTextSize(OptionsActivity.getTextSize()*2*getResources().getDisplayMetrics().density);
        tvVar2.setTextSize(OptionsActivity.getTextSize()*2*getResources().getDisplayMetrics().density);
        tvScore.setTextSize(OptionsActivity.getTextSize()*1*getResources().getDisplayMetrics().density);
        tvRecord.setTextSize(OptionsActivity.getTextSize()*1* getResources().getDisplayMetrics().density);
    }

    private void nextQuestion(Integer number, Integer rQuestion, Integer rQuestion2) {

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();

        qList = databaseAccess.getQuestion(rQuestion); //список первого варрианта, ИД(0), СТРАНА(1), ПЛОЩАДЬ(2)
        qList2 = databaseAccess.getQuestion(rQuestion2); //список второго варрианта, ИД(0), СТРАНА(1), ПЛОЩАДЬ(2)

        if (optionGameMode ==20) {
            tvScore.setText(number+getString(R.string.of)+ questionCount);
        } else{
            tvScore.setText(getString(R.string.score)+(number-1)+"");
        }

        if (getResources().getConfiguration().locale.toString().equals("ru_ru")||
                getResources().getConfiguration().locale.toString().equals("ru")||
                getResources().getConfiguration().locale.toString().equals("ru_RU")||
                getResources().getConfiguration().locale.toString().equals("uk_UA")||
                getResources().getConfiguration().locale.toString().equals("ua")) {
            tvQuestion.setText(getString(R.string.area_of_country) + qList.get(COUNTRY_NAME_RU) + "\n" + getString(R.string.or) + " " + qList2.get(COUNTRY_NAME_RU) + " ?"); // 1- это НАЗВАНИЕ страны, 2 - ПЛОЩАДЬ страны

            tvVar1.setText(qList.get(COUNTRY_NAME_RU)); //название страны
            tvVar2.setText(qList2.get(COUNTRY_NAME_RU)); //устанавливаем название страны.
        }else{
            tvQuestion.setText(getString(R.string.area_of_country) + qList.get(COUNTRY_NAME_EN) + "\n"+ getString(R.string.or) +" "+ qList2.get(COUNTRY_NAME_EN) + " ?"); // 1- это НАЗВАНИЕ страны, 2 - ПЛОЩАДЬ страны
            tvVar1.setText(qList.get(COUNTRY_NAME_EN)); //название страны
            tvVar2.setText(qList2.get(COUNTRY_NAME_EN)); //устанавливаем название страны.
        }


        areaCountry1 = Integer.parseInt(qList.get(COUNTRY_AREA));
        areaCountry2 = Integer.parseInt(qList2.get(COUNTRY_AREA));
        if (areaCountry1<areaCountry2){
            answer = 2;
        }else {
            answer = 1;
        }

        ivVar1.setImageResource(getResources().getIdentifier("a"+qList.get(COUNTRY_ID),"drawable", getPackageName())); //устанавливаем картинку на 1 варриант. Имедж-реусрс берется с R.drawable + _id
        ivVar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectVar1();
            }
        });
        ivVar2.setImageResource(getResources().getIdentifier("a"+qList2.get(COUNTRY_ID),"drawable", getPackageName())); //устанавливаем картинку на 2 варриант. Имедж-реусрс берется с R.drawable + _id элемента
        ivVar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectVar2();
            }
        });
        databaseAccess.close();
        if (optionGameMode ==0 ){
            tvRecord.setText(getString(R.string.record)+ Record.getRecord("Record")+"");
        } else{
            tvRecord.setText("");
        }
    }

    private void initialize() {
        Ads.showBanner(this); //показ баннера
        ivVar1 = (ImageView) findViewById(R.id.ivVar1);
        ivVar2 = (ImageView) findViewById(R.id.ivVar2);
        tvQuestion = (TextView) findViewById(R.id.tvQuestion);
        tvScore = (TextView) findViewById(R.id.tvScore);
        tvVar1 = (TextView) findViewById(R.id.tvVar1);
        tvVar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               selectVar1();
            }
        });
        tvVar2 = (TextView) findViewById(R.id.tvVar2);
        tvVar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectVar2();
            }
        });

        ivAnswerIcon1 = (ImageView) findViewById(R.id.ivAnswerIcon1);
        ivAnswerIcon2 = (ImageView) findViewById(R.id.ivAnswerIcon2);

        tvRecord = (TextView) findViewById(R.id.tvRecord);

        setfonts();
    }
    private void setClicable(Boolean bool){
        ivVar1.setClickable(bool);
        tvVar1.setClickable(bool);
        ivVar2.setClickable(bool);
        tvVar2.setClickable(bool);
    }

    private void selectVar1(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    number++;
                    if (answer==1) {
                        if (optionMusic) playSound(true);
                        handler.sendEmptyMessage(ANSWER_ONE_RIGHT);
                        rightAnswers++;
                    } else if (answer==2){
                        if  (optionMusic && optionGameMode == 20) playSound(false);
                        handler.sendEmptyMessage(ANSWER_ONE_WRONG);
                    }
                    pause();
                    handler.sendEmptyMessage(NEXT_QUESTION);

                } catch (Exception e){}
            }
        });
        thread.start();

    }

    private void selectVar2(){

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    number++;

                    if (answer==2) {
                        if (optionMusic) playSound(true);
                        handler.sendEmptyMessage(ANSWER_TWO_RIGHT);
                        rightAnswers++;
                    } else if (answer==1){
                        if (optionMusic && optionGameMode == 20) playSound(false);
                        handler.sendEmptyMessage(ANSWER_TWO_WRONG);
                    }
                    pause();
                    handler.sendEmptyMessage(NEXT_QUESTION);
                } catch (Exception e){}
            }
        });
        thread.start();

    }

    private void startRightAnswers(){ //

        Intent startScore = new Intent(getBaseContext(), RightAnsers.class);
        startScore.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //флаг удаления активити...
        startScore.putExtra("rightAnswers", "" + rightAnswers);
        startScore.putExtra("dbSize", "" + questionCount);
        startActivity(startScore);
    }

    private void randomQuestions(){   //рендомные страны . rQuestion и rQuestion2  - _id в БД.
        Random random = new Random();
        rQuestion = random.nextInt(dbSize - 1) + 1;
        rQuestion2 = random.nextInt(dbSize - 1) + 1;
        if (rQuestion == rQuestion2){
            rQuestion = dbSize - rQuestion2 +1;
        }
    }

    private void pause(){
        try {
           setClicable(false);
            TimeUnit.MILLISECONDS.sleep(500);
           setClicable(true);
        }catch (Exception E){}
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("number", number);
        outState.putInt("rightAnswer", rightAnswers);
        outState.putInt("rQuestion", rQuestion);
        outState.putInt("rQuestion2", rQuestion2);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        onSaveInstanceState(new Bundle());
        super.onPause();

    }
    private void playSound(Boolean sound){
        Intent musicIntent = new Intent().putExtra("answer", sound);
        musicIntent.setClass(this, MusicService.class);
        startService(musicIntent);
        stopService(musicIntent);
    }

    public void getOptions() {
        optionMusic = OptionsActivity.getOptionMusic();
        optionGameMode = OptionsActivity.getOptionGameMode();
  }
}
