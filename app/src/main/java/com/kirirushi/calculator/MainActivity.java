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
    public void onClickEqual(View view){
        TextView textView = findViewById(R.id.text_view);
        TextView historyTextView = findViewById(R.id.history);
        final ScrollView scrollTextView = findViewById(R.id.scroll_text_view);
        Parser parser = new Parser();
        try {
            double result = parser.evaluate(textView.getText().toString(), notation);
            String resStr;
            resStr = NotationChanger.changeNotation(Double.toString(result),10,notation);

            if(!historyTextView.getText().toString().isEmpty())
                historyTextView.append('\n'+textView.getText().toString()+"="+resStr);
            else
                historyTextView.append(textView.getText().toString()+"="+resStr);
        } catch (ParserException e){
            Toast toast = Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT);
            toast.show();
        }
        textView.setText("");
        new CountDownTimer(100,100) {
            public void onTick(long millisUntilFinished) {
            }
            public void onFinish() {
                scrollTextView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        }.start();
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
        TextView historyTextView = findViewById(R.id.history);
        String resStr;
        Parser parser = new Parser();
        if (!textView.getText().toString().isEmpty()){
            switch (view.getId()){
                case R.id.button_sin:
                    resStr = NotationChanger.changeNotation(Double.toString(Math.sin(parser.evaluate(textView.getText().toString(),notation))),10,notation);

                    if(!historyTextView.getText().toString().isEmpty())
                        historyTextView.append('\n'+"sin("+textView.getText().toString()+")="+resStr);
                    else
                        historyTextView.append("sin("+textView.getText().toString()+")="+resStr);
                    break;
                case R.id.button_cos:
                    resStr = NotationChanger.changeNotation(Double.toString(Math.cos(parser.evaluate(textView.getText().toString(),notation))),10,notation);
                    if(!historyTextView.getText().toString().isEmpty())
                        historyTextView.append('\n'+"cos("+textView.getText().toString()+")="+resStr);
                    else
                        historyTextView.append("cos("+textView.getText().toString()+")="+resStr);
                    break;
                case R.id.button_ctg:
                    resStr = NotationChanger.changeNotation(Double.toString(1/Math.tan(parser.evaluate(textView.getText().toString(),notation))),10,notation);
                    if(!historyTextView.getText().toString().isEmpty())
                        historyTextView.append('\n'+"ctg("+textView.getText().toString()+")="+resStr);
                    else
                        historyTextView.append("ctg("+textView.getText().toString()+")="+resStr);
                    break;
                case R.id.button_cube:
                    resStr = NotationChanger.changeNotation(Double.toString(Math.pow(parser.evaluate(textView.getText().toString(),notation),3)),10,notation);
                    if(!historyTextView.getText().toString().isEmpty())
                        historyTextView.append('\n'+textView.getText().toString()+"^3="+resStr);
                    else
                        historyTextView.append(textView.getText().toString()+"^3="+resStr);
                    break;
                case R.id.button_tan:
                    resStr = NotationChanger.changeNotation(Double.toString(Math.tan(parser.evaluate(textView.getText().toString(),notation))),10,notation);
                    if(!historyTextView.getText().toString().isEmpty())
                        historyTextView.append('\n'+"tg("+textView.getText().toString()+")="+resStr);
                    else
                        historyTextView.append("tg("+textView.getText().toString()+")="+resStr);
                    break;
                case R.id.button_exp:
                    resStr = NotationChanger.changeNotation(Double.toString(Math.exp(parser.evaluate(textView.getText().toString(),notation))),10,notation);
                    if(!historyTextView.getText().toString().isEmpty())
                        historyTextView.append('\n'+"exp("+textView.getText().toString()+")="+resStr);
                    else
                        historyTextView.append("exp("+textView.getText().toString()+")="+resStr);
                    break;
                case R.id.button_ln:
                    resStr = NotationChanger.changeNotation(Double.toString(Math.log(parser.evaluate(textView.getText().toString(),notation))),10,notation);
                    if(!historyTextView.getText().toString().isEmpty())
                        historyTextView.append('\n'+"ln("+textView.getText().toString()+")="+resStr);
                    else
                        historyTextView.append("ln("+textView.getText().toString()+")="+resStr);
                    break;
                case R.id.button_log:
                    resStr = NotationChanger.changeNotation(Double.toString(Math.log10(parser.evaluate(textView.getText().toString(),notation))),10,notation);
                    if(!historyTextView.getText().toString().isEmpty())
                        historyTextView.append('\n'+"log("+textView.getText().toString()+")="+resStr);
                    else
                        historyTextView.append("log("+textView.getText().toString()+")="+resStr);
                    break;
                case R.id.button_minus1:
                    resStr = NotationChanger.changeNotation(Double.toString(1/(parser.evaluate(textView.getText().toString(),notation))),10,notation);
                    if(!historyTextView.getText().toString().isEmpty())
                        historyTextView.append('\n'+"1/"+textView.getText().toString()+"="+resStr);
                    else
                        historyTextView.append("1/"+textView.getText().toString()+")="+resStr);
                    break;
                case R.id.button_square:
                    resStr = NotationChanger.changeNotation(Double.toString(Math.pow(parser.evaluate(textView.getText().toString(),notation),2)),10,notation);
                    if(!historyTextView.getText().toString().isEmpty())
                        historyTextView.append('\n'+textView.getText().toString()+"^2="+resStr);
                    else
                        historyTextView.append(textView.getText().toString()+"^2="+resStr);
                    break;
                case R.id.button_two_power:
                    resStr = NotationChanger.changeNotation(Double.toString(Math.pow(2, parser.evaluate(textView.getText().toString(),notation))),10,notation);
                    if(!historyTextView.getText().toString().isEmpty())
                        historyTextView.append('\n'+"2^"+textView.getText().toString()+"="+resStr);
                    else
                        historyTextView.append("2^"+textView.getText().toString()+"="+resStr);
                    break;
            }
        }
        if (view.getId()==R.id.button_random)
            textView.setText(NotationChanger.changeNotation(Double.toString(Math.random()),10,notation));
        final ScrollView scrollTextView = findViewById(R.id.scroll_text_view);
        new CountDownTimer(100,100) {
            public void onTick(long millisUntilFinished) {
            }
            public void onFinish() {
                scrollTextView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        }.start();
    }
    public boolean onOptionsItemSelected(MenuItem item){
        fTrans = getFragmentManager().beginTransaction();
        Parser parser = new Parser();
        TextView textView = findViewById(R.id.text_view);
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
        for (Button button:buttons)
            button.setEnabled(false);
        if(changedFnKeyboard)
            return true;
        switch (item.getItemId()){
            case R.id.bin:
                if(!textView.getText().toString().isEmpty()) {
                    try {
                        tempString = NotationChanger.changeNotation(Double.toString(parser.evaluate(textView.getText().toString(), notation)), notation, 2);
                    } catch (ParserException e){
                        Toast toast = Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT);
                        toast.show();
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
                        tempString = NotationChanger.changeNotation(Double.toString(parser.evaluate(textView.getText().toString(), notation)), notation, 8);
                    } catch (ParserException e){
                        Toast toast = Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT);
                        toast.show();
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
                        tempString = NotationChanger.changeNotation(Double.toString(parser.evaluate(textView.getText().toString(), notation)), notation, 10);
                    } catch (ParserException e){
                        Toast toast = Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT);
                        toast.show();
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
                        tempString = NotationChanger.changeNotation(Double.toString(parser.evaluate(textView.getText().toString(), notation)), notation, 16);
                    } catch (ParserException e){
                        Toast toast = Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT);
                        toast.show();
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
        if(nButtons>aLButtons.size())
            for (Button button:aLButtons)
                button.setEnabled(true);
        else
            for(int i=0;i<nButtons;i++)
                aLButtons.get(i).setEnabled(true);
    }
}
