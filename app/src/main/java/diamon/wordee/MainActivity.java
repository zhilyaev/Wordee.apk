/*
@author Zhilyaev Dmitriy
@ email zhilyaev.dmitriy@gmail.com
@date 30.06.2017
@for kwork.ru
 */
package diamon.wordee;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    TextView textView,textScore;
    Button newGame;
    LinearLayout layout; // слой
    Random random = new Random();
    LayoutInflater inflater;
    char secret; // Загаданная буква
    int index; // Индекс secret
    String wordee; // слово
    int SCORE = 0; // Количество угаданных слов
    String[] words; // выборка слов
    final char slash = '_';

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InputStream is = getResources().openRawResource(R.raw.db);
        Scanner sc = new Scanner(is);
        List<String> lines = new ArrayList<String>();
        while (sc.hasNextLine()) {
            lines.add(sc.nextLine());
        }
        words = lines.toArray(new String[0]);

        layout = (LinearLayout) findViewById(R.id.bottomlayout);
        inflater = getLayoutInflater();
        textView = (TextView) findViewById(R.id.textView);
        textScore = (TextView) findViewById(R.id.textScore);
        newGame = (Button) findViewById(R.id.button);
        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newGame(true);
            }
        });
        newGame(true);
    }

    void genButton(int count){
        // Генерируем строку
        char[] chars = new char[count];
        for(int i = 0;count>i;i++)
        chars[i] = (char)(random.nextInt(26) + 'A');
        chars[random.nextInt(count)] = secret;
        // Пихаем кнопки
        for(int i = 0; count>i;i++){
            final Button button = (Button) inflater.inflate(R.layout.button, null);
            button.setText(String.valueOf(chars[i]));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Не/очевидная ссылка на button
                    if(String.valueOf(secret).equals(String.valueOf(button.getText()))){
                        SCORE++;
                        newGame();
                    } else gameOver();
                }
            });
            layout.addView(button);
        }
    }

    // Ну вот опять
    void genButton(){
        genButton(wordee.length());
    }

    void newGame(boolean defaultSCORE){
        if(defaultSCORE){
            SCORE = 0;
            layout.setVisibility(View.VISIBLE);
            newGame.setVisibility(View.INVISIBLE);
        }
        wordee = words[random.nextInt(words.length)].toUpperCase();
        char[] chars = wordee.toCharArray();
        index = random.nextInt(wordee.length());
        secret = chars[index];
        chars[index]=slash;
        wordee = String.valueOf(chars);
        textView.setText(wordee);
        textScore.setText(String.valueOf(SCORE));
        layout.removeAllViews(); // Убираем уже созданные кнопки
        genButton(); // Создаем новые кнопки
    }

    // Зачем играть с перезагрузкой?
    void newGame(){
        newGame(false);
    }

    void gameOver(){
        textView.setText("");
        textScore.setText("Game Over");
        layout.setVisibility(View.INVISIBLE);
        newGame.setVisibility(View.VISIBLE);
    }
}
