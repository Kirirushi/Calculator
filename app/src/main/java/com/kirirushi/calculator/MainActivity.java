package com.kirirushi.calculator;

import android.app.FragmentTransaction;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnClickListener{

    FragmentNotHex fragmentNotHex;
    FragmentHex fragmentHex;
    FragmentFunctions fragmentFunctions;
    FragmentTransaction fTrans;

    private int notation = 10;
    private boolean changedFnKeyboard = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentNotHex = new FragmentNotHex();
        fragmentHex = new FragmentHex();
        fragmentFunctions = new FragmentFunctions();

        fTrans = getFragmentManager().beginTransaction();
        fTrans.add(R.id.keyboard, fragmentNotHex);
        fTrans.commit();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        TextView textView = findViewById(R.id.text_view);
        TextView historyTextView = findViewById(R.id.history);
        String textViewString = textView.getText().toString();
        String historyTextViewString = historyTextView.getText().toString();
        outState.putString("textViewString",textViewString);
        outState.putString("historyTextViewString",historyTextViewString);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        TextView textView = findViewById(R.id.text_view);
        TextView historyTextView = findViewById(R.id.history);
        textView.setText(savedInstanceState.getString("textViewString"));
        historyTextView.setText(savedInstanceState.getString("historyTextViewString"));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public void onClick(View view){
        TextView textView =  findViewById(R.id.text_view);
        Button button = (Button)view;
        String buttonText = button.getText().toString();
        textView.append(buttonText);
    }
    public void onClickCancel(View view) {
        TextView textView =  findViewById(R.id.text_view);
        textView.setText("");
    }
    private void pasteStringInTextView(TextView textView,String string){
        if(!textView.getText().toString().isEmpty())
            textView.append('\n'+string);
        else
            textView.append(string);
    }
    private void createTimerForScrolling(final ScrollView scrollView){
        new CountDownTimer(100,100) {
            public void onTick(long millisUntilFinished) {
            }
            public void onFinish() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        }.start();
    }
    public void onClickEqual(View view){
        TextView textView = findViewById(R.id.text_view);
        TextView historyTextView = findViewById(R.id.history);
        final ScrollView scrollTextView = findViewById(R.id.scroll_text_view);
        Parser parser = new Parser();
        try {
            double result = parser.evaluate(textView.getText().toString(), notation);
            String resStr;
            resStr = NotationChanger.changeNotation(
                    Double.toString(result),
                    10,
                    notation);
            pasteStringInTextView(historyTextView,textView.getText().toString()+"="+resStr);
        } catch (ParserException e){
             showToast(e.toString());
        }
        textView.setText("");
        createTimerForScrolling(scrollTextView);
    }
    public void onClickDelete(View view) {
        TextView textView = findViewById(R.id.text_view);
        if (!textView.getText().toString().isEmpty())
            textView.setText(textView.getText().toString().subSequence(0,textView.length()-1));
    }
    public void onClickOptionFn(MenuItem item) {
        fTrans = getFragmentManager().beginTransaction();
        if (!changedFnKeyboard) {
            fTrans.replace(R.id.keyboard, fragmentFunctions);
            changedFnKeyboard = true;
        }
        else {
            if (notation > 10){
                fTrans.remove(fragmentFunctions);
                fTrans.add(R.id.keyboard, fragmentHex);
            }else{
                fTrans.remove(fragmentFunctions);
                fTrans.add(R.id.keyboard, fragmentNotHex);
            }
            changedFnKeyboard = false;
        }
        fTrans.commit();
    }
    public void onClickFunction(View view) throws ParserException {
        TextView textView = findViewById(R.id.text_view);
        String textViewString = textView.getText().toString();
        TextView historyTextView = findViewById(R.id.history);
        String resStr;
        Parser parser = new Parser();
        if (!textView.getText().toString().isEmpty()){
            try{
                parser.evaluate(textViewString,notation);
            }catch (ParserException e){
                showToast(e.toString());
                return;
            }
            switch (view.getId()){
                case R.id.button_sin:
                    resStr = NotationChanger.changeNotation(
                            Double.toString(Math.sin(parser.evaluate(textViewString,notation))),
                            10,
                            notation);
                    pasteStringInTextView(historyTextView,"sin("+textViewString+")="+resStr);
                    break;
                case R.id.button_cos:
                    resStr = NotationChanger.changeNotation(
                            Double.toString(Math.cos(parser.evaluate(textViewString,notation))),
                            10,
                            notation);
                    pasteStringInTextView(historyTextView,"cos("+textViewString+")="+resStr);
                    break;
                case R.id.button_ctg:
                    resStr = NotationChanger.changeNotation(
                            Double.toString(1/Math.tan(parser.evaluate(textViewString,notation))),
                            10,
                            notation);
                    pasteStringInTextView(historyTextView,"ctg("+textViewString+")="+resStr);
                    break;
                case R.id.button_cube:
                    resStr = NotationChanger.changeNotation(
                            Double.toString(Math.pow(parser.evaluate(textViewString,notation),3)),
                            10,
                            notation);
                    pasteStringInTextView(historyTextView,textViewString+"^3="+resStr);
                    break;
                case R.id.button_tan:
                    resStr = NotationChanger.changeNotation(
                            Double.toString(Math.tan(parser.evaluate(textViewString,notation))),
                            10,
                            notation);
                    pasteStringInTextView(historyTextView,"tg("+textViewString+")="+resStr);
                    break;
                case R.id.button_exp:
                    resStr = NotationChanger.changeNotation(
                            Double.toString(Math.exp(parser.evaluate(textViewString,notation))),
                            10,
                            notation);
                    pasteStringInTextView(historyTextView,"exp("+textViewString+")="+resStr);
                    break;
                case R.id.button_ln:
                    resStr = NotationChanger.changeNotation(
                            Double.toString(Math.log(parser.evaluate(textViewString,notation))),
                            10,
                            notation);
                    pasteStringInTextView(historyTextView,"ln("+textViewString+")="+resStr);
                    break;
                case R.id.button_log:
                    resStr = NotationChanger.changeNotation(
                            Double.toString(Math.log10(parser.evaluate(textViewString,notation))),
                            10,
                            notation);
                    pasteStringInTextView(historyTextView,"log("+textViewString+")="+resStr);
                    break;
                case R.id.button_minus1:
                    resStr = NotationChanger.changeNotation(
                            Double.toString(1/parser.evaluate(textViewString,notation)),
                            10,
                            notation);
                    pasteStringInTextView(historyTextView,"1/"+textViewString+"="+resStr);
                    break;
                case R.id.button_square:
                    resStr = NotationChanger.changeNotation(
                            Double.toString(Math.pow(parser.evaluate(textViewString,notation),2)),
                            10,
                            notation);
                    pasteStringInTextView(historyTextView,textViewString+"^2="+resStr);
                    break;
                case R.id.button_two_power:
                    resStr = NotationChanger.changeNotation(
                            Double.toString(Math.pow(2, parser.evaluate(textViewString,notation))),
                            10,
                            notation);
                    pasteStringInTextView(historyTextView,"2^"+textViewString+"="+resStr);
                    break;
            }
        }
        if (view.getId()==R.id.button_random)
            textView.setText(NotationChanger.changeNotation(
                    Double.toString(Math.random()),
                    10,
                    notation));
        final ScrollView scrollTextView = findViewById(R.id.scroll_text_view);
        createTimerForScrolling(scrollTextView);
    }
    public boolean onOptionsItemSelected(MenuItem item){
        fTrans = getFragmentManager().beginTransaction();
        Parser parser = new Parser();
        TextView textView = findViewById(R.id.text_view);
        String textViewString = textView.getText().toString();
        TextView historyTextView = findViewById(R.id.history);
        historyTextView.setText("");
        String tempString="";
        ArrayList<Button> buttons = new ArrayList<>(10);
        buttons.add((Button) findViewById(R.id.button_0));
        buttons.add((Button) findViewById(R.id.button_1));
        buttons.add((Button) findViewById(R.id.button_2));
        buttons.add((Button) findViewById(R.id.button_3));
        buttons.add((Button) findViewById(R.id.button_4));
        buttons.add((Button) findViewById(R.id.button_5));
        buttons.add((Button) findViewById(R.id.button_6));
        buttons.add((Button) findViewById(R.id.button_7));
        buttons.add((Button) findViewById(R.id.button_8));
        buttons.add((Button) findViewById(R.id.button_9));
        if(changedFnKeyboard)
            return true;
        for (Button button:buttons)
            button.setEnabled(false);
        switch (item.getItemId()){
            case R.id.bin:
                if(!textView.getText().toString().isEmpty()) {
                    try {
                        tempString = NotationChanger.changeNotation(
                                Double.toString(parser.evaluate(textViewString, notation)),
                                10,
                                2);
                    } catch (ParserException e){
                        showToast(e.toString());
                        tempString = "";
                    }
                }
                fTrans.replace(R.id.keyboard,fragmentNotHex);
                fTrans.commit();
                textView.setText(tempString);
                notation = 2;
                enableNButtons(buttons,2);
                return true;
            case R.id.oct:
                if(!textView.getText().toString().isEmpty()) {
                    try {
                        tempString = NotationChanger.changeNotation(
                                Double.toString(parser.evaluate(textViewString, notation)),
                                10,
                                8);
                    } catch (ParserException e){
                        showToast(e.toString());
                        tempString = "";
                    }
                }
                fTrans.replace(R.id.keyboard,fragmentNotHex);
                fTrans.commit();
                textView.setText(tempString);
                notation = 8;
                enableNButtons(buttons,8);
                return true;
            case R.id.dec:
                if(!textView.getText().toString().isEmpty()) {
                    try {
                        tempString = NotationChanger.changeNotation(
                                Double.toString(parser.evaluate(textViewString, notation)),
                                10,
                                10);
                    } catch (ParserException e){
                        showToast(e.toString());
                        tempString = "";
                    }
                }
                fTrans.replace(R.id.keyboard,fragmentNotHex);
                fTrans.commit();
                textView.setText(tempString);
                notation = 10;
                enableNButtons(buttons,10);
                return true;
            case R.id.hex:
                if(!textView.getText().toString().isEmpty()) {
                    try {
                        tempString = NotationChanger.changeNotation(
                                Double.toString(parser.evaluate(textViewString, notation)),
                                10,
                                16);
                    } catch (ParserException e){
                        showToast(e.toString());
                        tempString = "";
                    }
                }
                fTrans.replace(R.id.keyboard,fragmentHex);
                fTrans.commit();
                textView.setText(tempString);
                notation = 16;
                enableNButtons(buttons,16);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void enableNButtons(ArrayList<Button> aLButtons,int nButtons){
        if(nButtons>=10)
            for (Button button:aLButtons)
                button.setEnabled(true);
        else
            for(int i=0;i<nButtons;i++)
                aLButtons.get(i).setEnabled(true);
    }
    private void showToast(String string){
        Toast toast = Toast.makeText(this, string ,Toast.LENGTH_SHORT);
        toast.show();
    }
}
