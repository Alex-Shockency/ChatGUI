package com.quizlyworks.quizapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class QuizBody extends AppCompatActivity {

    static List<QuestionObject> questionBank;
    QuestionManager manager;
    ViewPager pager;
    Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_body);
        questionBank = new ArrayList<QuestionObject>();

        manager = new QuestionManager(getSupportFragmentManager());
        pager = (ViewPager)findViewById(R.id.pager);
        pager.setAdapter(manager);
        pager.setOffscreenPageLimit(QuestionManager.NUMBER_OF_QUESTIONS);
        ((Button)findViewById(R.id.submit_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),ResultPage.class);
                Bundle b = new Bundle();
                b.putInt("score",QuestionManager.numberOfCorrectAnswers);
                b.putInt("attempted",QuestionManager.numberOfAttemptedAnswers);
                intent.putExtras(b);
                startActivity(intent);
                finish();
            }
        });
        try{
            parseJSON();
            Collections.shuffle(questionBank);
        }catch(Exception e) {
            Log.e("QuizBody", e.toString());
        }
    }

    void parseJSON() throws JSONException, IOException {
        InputStream inStream = getResources().openRawResource(R.raw.questions);
        byte[] b = new byte[inStream.available()];
        inStream.read(b);

        JSONObject jsonObject = new JSONObject(new String(b));
        JSONArray questions = jsonObject.getJSONArray("questions");
        for(int i = 0; i < questions.length(); i++){
            JSONObject question = questions.getJSONObject(i);
            String title = question.getString("question");
            String correct = question.getString("correct");
            String[] incorrect = new String[3];
            incorrect[0] = question.getString("incorrect1");
            incorrect[1] = question.getString("incorrect2");
            incorrect[2] = question.getString("incorrect3");
            QuestionObject q = new QuestionObject(title, correct, incorrect);
            questionBank.add(q);
        }
    }

    private static class QuestionManager extends FragmentPagerAdapter {

        private static final int NUMBER_OF_QUESTIONS = 10;
        static int numberOfCorrectAnswers;
        static int numberOfAttemptedAnswers;

        public QuestionManager(FragmentManager fm) {
            super(fm);
            numberOfCorrectAnswers = 0;
            numberOfAttemptedAnswers = 0;
        }

        @Override
        public Fragment getItem(int position) {
            return Question.newInstance(questionBank.get(position), position);
        }

        @Override
        public int getCount() {
            return NUMBER_OF_QUESTIONS;
        }

        public static class Question extends Fragment implements View.OnClickListener{
            static Question newInstance(QuestionObject qo, int num){
                Question q = new Question();
                Bundle args = new Bundle();
                args.putInt("index", num);
                args.putString("title", qo.title);
                args.putStringArray("options", qo.getOptions());
                args.putInt("correctIndex", qo.correctIndex);
                q.setArguments(args);
                return q;
            }

            public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
                View v = inflater.inflate(R.layout.fragment_quiz_question, container, false);
                int totalQuestions = NUMBER_OF_QUESTIONS;
                int currentQuestion = (getArguments().getInt("index") + 1);
                String title = (getArguments().getString("title"));
                String[] options = (getArguments().getStringArray("options"));
                String progress = String.format(getString(R.string.progress_label), currentQuestion, totalQuestions);
                ((TextView) v.findViewById(R.id.progress_text)).setText(progress);
                ((TextView) v.findViewById(R.id.question_text)).setText(title);
                ((ProgressBar) v.findViewById(R.id.progress_bar)).setProgress((int) (100.0 * currentQuestion / totalQuestions));
                Button[] buttons = new Button[4];
                buttons[0] = ((Button) v.findViewById(R.id.option_1));
                buttons[1] = ((Button) v.findViewById(R.id.option_2));
                buttons[2] = ((Button) v.findViewById(R.id.option_3));
                buttons[3] = ((Button) v.findViewById(R.id.option_4));
                for(int i = 0; i < buttons.length; i++){
                    buttons[i].setText(options[i]);
                    buttons[i].setOnClickListener(this);
                }
                return v;
            }

            public void showResults(){
                Intent intent = new Intent(getContext(),ResultPage.class);
                Bundle b = new Bundle();
                b.putInt("score",numberOfCorrectAnswers);
                b.putInt("attempted",numberOfAttemptedAnswers);
                intent.putExtras(b);
                startActivity(intent);
                getActivity().finish();
            }

            public void onClick(View v) {
                int guessIndex;
                switch (v.getId()) {
                    case R.id.option_1:
                        guessIndex = 0;
                        break;
                    case R.id.option_2:
                        guessIndex = 1;
                        break;
                    case R.id.option_3:
                        guessIndex = 2;
                        break;
                    case R.id.option_4:
                        guessIndex = 3;
                        break;
                    default:
                        guessIndex = -1;
                        break;
                }
                this.disableButtons();
                numberOfAttemptedAnswers++;
                if(getArguments().getInt("correctIndex") == guessIndex){
                    ((Button)v).setText("Correct");
                    ((Button)v).setTextColor(Color.parseColor("#006400"));
                    numberOfCorrectAnswers++;
                }else{
                    ((Button)v).setText("Incorrect");
                    ((Button)v).setTextColor(Color.parseColor("#B22222"));
                }
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        advanceQuestion();
                    }
                },500);
                if(numberOfAttemptedAnswers == NUMBER_OF_QUESTIONS){
                    showResults();
                }
            }

            public void disableButtons(){
                View v = getView();
                v.findViewById(R.id.option_1).setEnabled(false);
                v.findViewById(R.id.option_2).setEnabled(false);
                v.findViewById(R.id.option_3).setEnabled(false);
                v.findViewById(R.id.option_4).setEnabled(false);
            }

            public void advanceQuestion(){
                QuizBody a = ((QuizBody)this.getActivity());
                if(a.pager.getCurrentItem() == NUMBER_OF_QUESTIONS - 1){
                   return;
                }
                a.pager.setCurrentItem(a.pager.getCurrentItem()+1);
            }
        }
    }

    private class QuestionObject {
        public String title;
        public String correct;
        public int correctIndex;
        public List<String> options;

        public QuestionObject(String _title, String _correct, String[] _incorrect){
            title = _title;
            correct = _correct;
            options = new ArrayList<String>(Arrays.asList(_incorrect));
            options.add(correct);
            Collections.shuffle(options);
            correctIndex = options.indexOf(correct);
        }

        public String[] getOptions(){
            return (String[])options.toArray(new String[options.size()]);
        }
    }
}
