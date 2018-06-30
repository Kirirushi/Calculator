package com.kirirushi.calculator;

import android.app.FragmentTransaction;
import android.arch.core.internal.FastSafeIterableMap;
import android.content.ClipData;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class MainActivity extends AppCompatActivity implements OnClickListener{

    private static final String TAG = "MainActivity";

    FragmentNotHex fragmentNotHex;
    FragmentHex fragmentHex;
    FragmentFunctions fragmentFunctions;
    FragmentTransaction fTrans;

    public static final int BIN_NOTATION = 2;
    public static final int OCT_NOTATION = 8;
    public static final int DEC_NOTATION = 10;
    public static final int HEX_NOTATION = 16;

    private int notation = DEC_NOTATION;
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
            DecimalFormatSymbols s = new DecimalFormatSymbols();
            s.setDecimalSeparator('.');
            DecimalFormat f;
            String resStr;
            switch (notation)
            {
                case BIN_NOTATION:
                    result = Double.parseDouble(Parser.dec2bin(Double.toString(result)));
                    f = new DecimalFormat("###0", s);
                    resStr = f.format(result);
                    break;
                case OCT_NOTATION:
                    result = Double.parseDouble(Parser.dec2oct(Double.toString(result)));
                    f = new DecimalFormat("###0", s);
                    resStr = f.format(result);
                    break;
                case HEX_NOTATION:
                    resStr = Parser.dec2hex(Double.toString(result));
                    break;
                default:
                    f = new DecimalFormat("#,##0.0##############", s);
                    resStr = f.format(result);
                    break;
            }
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        fTrans = getFragmentManager().beginTransaction();
        TextView textView = findViewById(R.id.text_view);
        TextView historyTextView = findViewById(R.id.history);
        historyTextView.setText("");
        String tempString="";
        Button button0;
        Button button1;
        Button button2;
        Button button3;
        Button button4;
        Button button5;
        Button button6;
        Button button7;
        Button button8;
        Button button9;
        if(changedFnKeyboard){
            return true;
        }
        switch (item.getItemId()){
            case R.id.bin:
                switch (notation){
                    case OCT_NOTATION:
                        if(!textView.getText().toString().isEmpty())
                            tempString = Parser.dec2bin(Parser.oct2dec(textView.getText().toString()));
                        break;
                    case DEC_NOTATION:
                        if(!textView.getText().toString().isEmpty())
                            tempString = Parser.dec2bin(textView.getText().toString());
                        break;
                    case HEX_NOTATION:
                        if(!textView.getText().toString().isEmpty())
                            tempString = Parser.dec2bin(Parser.hex2dec(textView.getText().toString()));
                        fTrans.replace(R.id.keyboard,fragmentNotHex);
                        fTrans.commit();
                        break;
                    default:
                        if(!textView.getText().toString().isEmpty())
                            tempString = textView.getText().toString();
                }
                button0 = findViewById(R.id.button_0);
                button1 = findViewById(R.id.button_1);
                button2 = findViewById(R.id.button_2);
                button3 = findViewById(R.id.button_3);
                button4 = findViewById(R.id.button_4);
                button5 = findViewById(R.id.button_5);
                button6 = findViewById(R.id.button_6);
                button7 = findViewById(R.id.button_7);
                button8 = findViewById(R.id.button_8);
                button9 = findViewById(R.id.button_9);
                button0.setEnabled(true);
                button1.setEnabled(true);
                button2.setEnabled(false);
                button3.setEnabled(false);
                button4.setEnabled(false);
                button5.setEnabled(false);
                button6.setEnabled(false);
                button7.setEnabled(false);
                button8.setEnabled(false);
                button9.setEnabled(false);
                textView.setText(tempString);
                notation = BIN_NOTATION;
                return true;
            case R.id.oct:
                    switch (notation){
                        case BIN_NOTATION:
                            if(!textView.getText().toString().isEmpty())
                                tempString = Parser.dec2oct(Parser.bin2dec(textView.getText().toString()));
                            break;
                        case DEC_NOTATION:
                            if(!textView.getText().toString().isEmpty())
                                tempString = Parser.dec2oct(textView.getText().toString());
                            break;
                        case HEX_NOTATION:
                            if(!textView.getText().toString().isEmpty())
                                tempString = Parser.dec2oct(Parser.hex2dec(textView.getText().toString()));
                            fTrans.replace(R.id.keyboard,fragmentNotHex);
                            fTrans.commit();
                            break;
                        default:
                            tempString = textView.getText().toString();
                    }
                button0 = findViewById(R.id.button_0);
                button1 = findViewById(R.id.button_1);
                button2 = findViewById(R.id.button_2);
                button3 = findViewById(R.id.button_3);
                button4 = findViewById(R.id.button_4);
                button5 = findViewById(R.id.button_5);
                button6 = findViewById(R.id.button_6);
                button7 = findViewById(R.id.button_7);
                button8 = findViewById(R.id.button_8);
                button9 = findViewById(R.id.button_9);
                button0.setEnabled(true);
                button1.setEnabled(true);
                button2.setEnabled(true);
                button3.setEnabled(true);
                button4.setEnabled(true);
                button5.setEnabled(true);
                button6.setEnabled(true);
                button7.setEnabled(true);
                button8.setEnabled(false);
                button9.setEnabled(false);
                textView.setText(tempString);
                notation = OCT_NOTATION;
                return true;
            case R.id.dec:
                    switch (notation){
                        case BIN_NOTATION:
                            if(!textView.getText().toString().isEmpty())
                                tempString = Parser.bin2dec(textView.getText().toString());
                            break;
                        case OCT_NOTATION:
                            if(!textView.getText().toString().isEmpty())
                                tempString = Parser.oct2dec(textView.getText().toString());
                            break;
                        case HEX_NOTATION:
                            if(!textView.getText().toString().isEmpty())
                                tempString = Parser.hex2dec(textView.getText().toString());
                            fTrans.replace(R.id.keyboard,fragmentNotHex);
                            fTrans.commit();
                            break;
                        default:
                            tempString = textView.getText().toString();
                    }
                button0 = findViewById(R.id.button_0);
                button1 = findViewById(R.id.button_1);
                button2 = findViewById(R.id.button_2);
                button3 = findViewById(R.id.button_3);
                button4 = findViewById(R.id.button_4);
                button5 = findViewById(R.id.button_5);
                button6 = findViewById(R.id.button_6);
                button7 = findViewById(R.id.button_7);
                button8 = findViewById(R.id.button_8);
                button9 = findViewById(R.id.button_9);
                button0.setEnabled(true);
                button1.setEnabled(true);
                button2.setEnabled(true);
                button3.setEnabled(true);
                button4.setEnabled(true);
                button5.setEnabled(true);
                button6.setEnabled(true);
                button7.setEnabled(true);
                button8.setEnabled(true);
                button9.setEnabled(true);
                textView.setText(tempString);
                notation = DEC_NOTATION;
                return true;
            case R.id.hex:
                switch (notation){
                    case BIN_NOTATION:
                        if(!textView.getText().toString().isEmpty())
                            tempString = Parser.dec2hex(Parser.bin2dec(textView.getText().toString()));
                        fTrans.replace(R.id.keyboard,fragmentHex);
                        fTrans.commit();
                        break;
                    case OCT_NOTATION:
                        if(!textView.getText().toString().isEmpty())
                            tempString = Parser.dec2hex(Parser.oct2dec(textView.getText().toString()));
                        fTrans.replace(R.id.keyboard,fragmentHex);
                        fTrans.commit();
                        break;
                    case DEC_NOTATION:
                        if(!textView.getText().toString().isEmpty())
                            tempString = Parser.dec2hex(textView.getText().toString());
                        fTrans.replace(R.id.keyboard,fragmentHex);
                        fTrans.commit();
                        break;
                    default:
                        tempString = textView.getText().toString();
                    }
                button0 = findViewById(R.id.button_0);
                button1 = findViewById(R.id.button_1);
                button2 = findViewById(R.id.button_2);
                button3 = findViewById(R.id.button_3);
                button4 = findViewById(R.id.button_4);
                button5 = findViewById(R.id.button_5);
                button6 = findViewById(R.id.button_6);
                button7 = findViewById(R.id.button_7);
                button8 = findViewById(R.id.button_8);
                button9 = findViewById(R.id.button_9);
                button0.setEnabled(true);
                button1.setEnabled(true);
                button2.setEnabled(true);
                button3.setEnabled(true);
                button4.setEnabled(true);
                button5.setEnabled(true);
                button6.setEnabled(true);
                button7.setEnabled(true);
                button8.setEnabled(true);
                button9.setEnabled(true);
                textView.setText(tempString);
                notation = HEX_NOTATION;
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void onClickOptionFn(MenuItem item) {
        fTrans = getFragmentManager().beginTransaction();
        if (!changedFnKeyboard) {
            fTrans.replace(R.id.keyboard, fragmentFunctions);
            changedFnKeyboard = true;
        }
        else {
            if (notation == HEX_NOTATION){
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
    public void onClickFunction(View view){
        TextView textView = findViewById(R.id.text_view);
        TextView historyTextView = findViewById(R.id.history);
        String resStr="";
        if (!textView.getText().toString().isEmpty()){
            switch (view.getId()){
                case R.id.button_sin:
                    switch (notation){
                        case BIN_NOTATION:
                            resStr = Double.toString(Math.sin(Double.parseDouble(Parser.bin2dec(textView.getText().toString()))));
                            if(!historyTextView.getText().toString().isEmpty())
                                historyTextView.append('\n'+"sin("+textView.getText().toString()+")="+resStr);
                            else
                                historyTextView.append("sin("+textView.getText().toString()+")="+resStr);
                            break;
                        case OCT_NOTATION:
                            resStr = Double.toString(Math.sin(Double.parseDouble(Parser.oct2dec(textView.getText().toString()))));
                            if(!historyTextView.getText().toString().isEmpty())
                                historyTextView.append('\n'+"sin("+textView.getText().toString()+")="+resStr);
                            else
                                historyTextView.append("sin("+textView.getText().toString()+")="+resStr);
                            break;
                        case DEC_NOTATION:
                            resStr = Double.toString(Math.sin(Double.parseDouble(textView.getText().toString())));
                            if(!historyTextView.getText().toString().isEmpty())
                                historyTextView.append('\n'+"sin("+textView.getText().toString()+")="+resStr);
                            else
                                historyTextView.append("sin("+textView.getText().toString()+")="+resStr);
                            break;
                        case HEX_NOTATION:
                            resStr = Double.toString(Math.sin(Double.parseDouble(Parser.hex2dec(textView.getText().toString()))));
                            if(!historyTextView.getText().toString().isEmpty())
                                historyTextView.append('\n'+"sin("+textView.getText().toString()+")="+resStr);
                            else
                                historyTextView.append("sin("+textView.getText().toString()+")="+resStr);
                            break;
                    }
                    break;
                case R.id.button_cos:
                    switch (notation){
                        case BIN_NOTATION:
                            resStr = Double.toString(Math.cos(Double.parseDouble(Parser.bin2dec(textView.getText().toString()))));
                            if(!historyTextView.getText().toString().isEmpty())
                                historyTextView.append('\n'+"cos("+textView.getText().toString()+")="+resStr);
                            else
                                historyTextView.append("cos("+textView.getText().toString()+")="+resStr);
                            break;
                        case OCT_NOTATION:
                            resStr = Double.toString(Math.cos(Double.parseDouble(Parser.oct2dec(textView.getText().toString()))));
                            if(!historyTextView.getText().toString().isEmpty())
                                historyTextView.append('\n'+"cos("+textView.getText().toString()+")="+resStr);
                            else
                                historyTextView.append("sin("+textView.getText().toString()+")="+resStr);
                            break;
                        case DEC_NOTATION:
                            resStr = Double.toString(Math.cos(Double.parseDouble(textView.getText().toString())));
                            if(!historyTextView.getText().toString().isEmpty())
                                historyTextView.append('\n'+"cos("+textView.getText().toString()+")="+resStr);
                            else
                                historyTextView.append("cos("+textView.getText().toString()+")="+resStr);
                            break;
                        case HEX_NOTATION:
                            resStr = Double.toString(Math.cos(Double.parseDouble(Parser.hex2dec(textView.getText().toString()))));
                            if(!historyTextView.getText().toString().isEmpty())
                                historyTextView.append('\n'+"cos("+textView.getText().toString()+")="+resStr);
                            else
                                historyTextView.append("cos("+textView.getText().toString()+")="+resStr);
                            break;
                    }
                    break;
                case R.id.button_ctg:
                    switch (notation){
                        case BIN_NOTATION:
                            resStr = Double.toString(1/Math.tan(Double.parseDouble(Parser.bin2dec(textView.getText().toString()))));
                            if(!historyTextView.getText().toString().isEmpty())
                                historyTextView.append('\n'+"ctg("+textView.getText().toString()+")="+resStr);
                            else
                                historyTextView.append("ctg("+textView.getText().toString()+")="+resStr);
                            break;
                        case OCT_NOTATION:
                            resStr = Double.toString(1/Math.tan(Double.parseDouble(Parser.oct2dec(textView.getText().toString()))));
                            if(!historyTextView.getText().toString().isEmpty())
                                historyTextView.append('\n'+"ctg("+textView.getText().toString()+")="+resStr);
                            else
                                historyTextView.append("ctg("+textView.getText().toString()+")="+resStr);
                            break;
                        case DEC_NOTATION:
                            resStr = Double.toString(1/Math.tan(Double.parseDouble(textView.getText().toString())));
                            if(!historyTextView.getText().toString().isEmpty())
                                historyTextView.append('\n'+"ctg("+textView.getText().toString()+")="+resStr);
                            else
                                historyTextView.append("ctg("+textView.getText().toString()+")="+resStr);
                            break;
                        case HEX_NOTATION:
                            resStr = Double.toString(1/Math.tan(Double.parseDouble(Parser.hex2dec(textView.getText().toString()))));
                            if(!historyTextView.getText().toString().isEmpty())
                                historyTextView.append('\n'+"ctg("+textView.getText().toString()+")="+resStr);
                            else
                                historyTextView.append("ctg("+textView.getText().toString()+")="+resStr);
                            break;
                    }
                    break;
                case R.id.button_cube:
                    switch (notation){
                        case BIN_NOTATION:
                            resStr = Double.toString(Math.pow(Double.parseDouble(Parser.bin2dec(textView.getText().toString())),3));
                            if(!historyTextView.getText().toString().isEmpty())
                                historyTextView.append('\n'+textView.getText().toString()+"^3="+resStr);
                            else
                                historyTextView.append(textView.getText().toString()+"^3="+resStr);
                            break;
                        case OCT_NOTATION:
                            resStr = Double.toString(Math.pow(Double.parseDouble(Parser.oct2dec(textView.getText().toString())),3));
                            if(!historyTextView.getText().toString().isEmpty())
                                historyTextView.append('\n'+textView.getText().toString()+"^3="+resStr);
                            else
                                historyTextView.append(textView.getText().toString()+"^3="+resStr);
                            break;
                        case DEC_NOTATION:
                            resStr = Double.toString(Math.pow(Double.parseDouble(textView.getText().toString()),3));
                            if(!historyTextView.getText().toString().isEmpty())
                                historyTextView.append('\n'+textView.getText().toString()+"^3="+resStr);
                            else
                                historyTextView.append(textView.getText().toString()+"^3="+resStr);
                            break;
                        case HEX_NOTATION:
                            resStr = Double.toString(Math.pow(Double.parseDouble(Parser.hex2dec(textView.getText().toString())),3));
                            if(!historyTextView.getText().toString().isEmpty())
                                historyTextView.append('\n'+textView.getText().toString()+"^3="+resStr);
                            else
                                historyTextView.append(textView.getText().toString()+"^3="+resStr);
                            break;
                    }
                    break;
                case R.id.button_tan:
                    switch (notation){
                        case BIN_NOTATION:
                            resStr = Double.toString(Math.tan(Double.parseDouble(Parser.bin2dec(textView.getText().toString()))));
                            if(!historyTextView.getText().toString().isEmpty())
                                historyTextView.append('\n'+"tg("+textView.getText().toString()+")="+resStr);
                            else
                                historyTextView.append("tg("+textView.getText().toString()+")="+resStr);
                            break;
                        case OCT_NOTATION:
                            resStr = Double.toString(Math.tan(Double.parseDouble(Parser.oct2dec(textView.getText().toString()))));
                            if(!historyTextView.getText().toString().isEmpty())
                                historyTextView.append('\n'+"tg("+textView.getText().toString()+")="+resStr);
                            else
                                historyTextView.append("tg("+textView.getText().toString()+")="+resStr);
                            break;
                        case DEC_NOTATION:
                            resStr = Double.toString(Math.tan(Double.parseDouble(textView.getText().toString())));
                            if(!historyTextView.getText().toString().isEmpty())
                                historyTextView.append('\n'+"tg("+textView.getText().toString()+")="+resStr);
                            else
                                historyTextView.append("tg("+textView.getText().toString()+")="+resStr);
                            break;
                        case HEX_NOTATION:
                            resStr = Double.toString(Math.tan(Double.parseDouble(Parser.hex2dec(textView.getText().toString()))));
                            if(!historyTextView.getText().toString().isEmpty())
                                historyTextView.append('\n'+"tg("+textView.getText().toString()+")="+resStr);
                            else
                                historyTextView.append("tg("+textView.getText().toString()+")="+resStr);
                            break;
                    }
                    break;
                case R.id.button_exp:
                    switch (notation){
                        case BIN_NOTATION:
                            resStr = Double.toString(Math.exp(Double.parseDouble(Parser.bin2dec(textView.getText().toString()))));
                            if(!historyTextView.getText().toString().isEmpty())
                                historyTextView.append('\n'+"exp("+textView.getText().toString()+")="+resStr);
                            else
                                historyTextView.append("exp("+textView.getText().toString()+")="+resStr);
                            break;
                        case OCT_NOTATION:
                            resStr = Double.toString(Math.exp(Double.parseDouble(Parser.oct2dec(textView.getText().toString()))));
                            if(!historyTextView.getText().toString().isEmpty())
                                historyTextView.append('\n'+"exp("+textView.getText().toString()+")="+resStr);
                            else
                                historyTextView.append("ctg("+textView.getText().toString()+")="+resStr);
                            break;
                        case DEC_NOTATION:
                            resStr = Double.toString(Math.exp(Double.parseDouble(textView.getText().toString())));
                            if(!historyTextView.getText().toString().isEmpty())
                                historyTextView.append('\n'+"exp("+textView.getText().toString()+")="+resStr);
                            else
                                historyTextView.append("exp("+textView.getText().toString()+")="+resStr);
                            break;
                        case HEX_NOTATION:
                            resStr = Double.toString(Math.exp(Double.parseDouble(Parser.hex2dec(textView.getText().toString()))));
                            if(!historyTextView.getText().toString().isEmpty())
                                historyTextView.append('\n'+"exp("+textView.getText().toString()+")="+resStr);
                            else
                                historyTextView.append("exp("+textView.getText().toString()+")="+resStr);
                            break;
                    }
                    break;
                case R.id.button_ln:
                    switch (notation){
                        case BIN_NOTATION:
                            resStr = Double.toString(Math.log(Double.parseDouble(Parser.bin2dec(textView.getText().toString()))));
                            if(!historyTextView.getText().toString().isEmpty())
                                historyTextView.append('\n'+"ln("+textView.getText().toString()+")="+resStr);
                            else
                                historyTextView.append("ln("+textView.getText().toString()+")="+resStr);
                            break;
                        case OCT_NOTATION:
                            resStr = Double.toString(Math.log(Double.parseDouble(Parser.oct2dec(textView.getText().toString()))));
                            if(!historyTextView.getText().toString().isEmpty())
                                historyTextView.append('\n'+"ln("+textView.getText().toString()+")="+resStr);
                            else
                                historyTextView.append("ln("+textView.getText().toString()+")="+resStr);
                            break;
                        case DEC_NOTATION:
                            resStr = Double.toString(Math.log(Double.parseDouble(textView.getText().toString())));
                            if(!historyTextView.getText().toString().isEmpty())
                                historyTextView.append('\n'+"ln("+textView.getText().toString()+")="+resStr);
                            else
                                historyTextView.append("ln("+textView.getText().toString()+")="+resStr);
                            break;
                        case HEX_NOTATION:
                            resStr = Double.toString(Math.log(Double.parseDouble(Parser.hex2dec(textView.getText().toString()))));
                            if(!historyTextView.getText().toString().isEmpty())
                                historyTextView.append('\n'+"ln("+textView.getText().toString()+")="+resStr);
                            else
                                historyTextView.append("ln("+textView.getText().toString()+")="+resStr);
                            break;
                    }
                    break;
                case R.id.button_log:
                    switch (notation){
                        case BIN_NOTATION:
                            resStr = Double.toString(Math.log10(Double.parseDouble(Parser.bin2dec(textView.getText().toString()))));
                            if(!historyTextView.getText().toString().isEmpty())
                                historyTextView.append('\n'+"log("+textView.getText().toString()+")="+resStr);
                            else
                                historyTextView.append("log("+textView.getText().toString()+")="+resStr);
                            break;
                        case OCT_NOTATION:
                            resStr = Double.toString(Math.log10(Double.parseDouble(Parser.oct2dec(textView.getText().toString()))));
                            if(!historyTextView.getText().toString().isEmpty())
                                historyTextView.append('\n'+"log("+textView.getText().toString()+")="+resStr);
                            else
                                historyTextView.append("log("+textView.getText().toString()+")="+resStr);
                            break;
                        case DEC_NOTATION:
                            resStr = Double.toString(Math.log10(Double.parseDouble(textView.getText().toString())));
                            if(!historyTextView.getText().toString().isEmpty())
                                historyTextView.append('\n'+"log("+textView.getText().toString()+")="+resStr);
                            else
                                historyTextView.append("log("+textView.getText().toString()+")="+resStr);
                            break;
                        case HEX_NOTATION:
                            resStr = Double.toString(Math.log10(Double.parseDouble(Parser.hex2dec(textView.getText().toString()))));
                            if(!historyTextView.getText().toString().isEmpty())
                                historyTextView.append('\n'+"log("+textView.getText().toString()+")="+resStr);
                            else
                                historyTextView.append("log("+textView.getText().toString()+")="+resStr);
                            break;
                    }
                    break;
                case R.id.button_minus1:
                    switch (notation){
                        case BIN_NOTATION:
                            resStr = Double.toString(1/Double.parseDouble(Parser.bin2dec(textView.getText().toString())));
                            if(!historyTextView.getText().toString().isEmpty())
                                historyTextView.append('\n'+"1/"+textView.getText().toString()+"="+resStr);
                            else
                                historyTextView.append("1/"+textView.getText().toString()+")="+resStr);
                            break;
                        case OCT_NOTATION:
                            resStr = Double.toString(1/Double.parseDouble(Parser.oct2dec(textView.getText().toString())));
                            if(!historyTextView.getText().toString().isEmpty())
                                historyTextView.append('\n'+"1/"+textView.getText().toString()+"="+resStr);
                            else
                                historyTextView.append("1/"+textView.getText().toString()+"="+resStr);
                            break;
                        case DEC_NOTATION:
                            resStr = Double.toString(1/Double.parseDouble(textView.getText().toString()));
                            if(!historyTextView.getText().toString().isEmpty())
                                historyTextView.append('\n'+"1/"+textView.getText().toString()+"="+resStr);
                            else
                                historyTextView.append("1/"+textView.getText().toString()+"="+resStr);
                            break;
                        case HEX_NOTATION:
                            resStr = Double.toString(1/Double.parseDouble(Parser.hex2dec(textView.getText().toString())));
                            if(!historyTextView.getText().toString().isEmpty())
                                historyTextView.append('\n'+"1/"+textView.getText().toString()+"="+resStr);
                            else
                                historyTextView.append("1/"+textView.getText().toString()+"="+resStr);
                            break;
                    }
                    break;
                case R.id.button_square:
                    switch (notation){
                        case BIN_NOTATION:
                            resStr = Double.toString(Math.pow(Double.parseDouble(Parser.bin2dec(textView.getText().toString())),2));
                            if(!historyTextView.getText().toString().isEmpty())
                                historyTextView.append('\n'+textView.getText().toString()+"^2="+resStr);
                            else
                                historyTextView.append(textView.getText().toString()+"^2="+resStr);
                            break;
                        case OCT_NOTATION:
                            resStr = Double.toString(Math.pow(Double.parseDouble(Parser.oct2dec(textView.getText().toString())),2));
                            if(!historyTextView.getText().toString().isEmpty())
                                historyTextView.append('\n'+textView.getText().toString()+"^2="+resStr);
                            else
                                historyTextView.append(textView.getText().toString()+"^2="+resStr);
                            break;
                        case DEC_NOTATION:
                            resStr = Double.toString(Math.pow(Double.parseDouble(textView.getText().toString()),2));
                            if(!historyTextView.getText().toString().isEmpty())
                                historyTextView.append('\n'+textView.getText().toString()+"^2="+resStr);
                            else
                                historyTextView.append(textView.getText().toString()+"^2="+resStr);
                            break;
                        case HEX_NOTATION:
                            resStr = Double.toString(Math.pow(Double.parseDouble(Parser.hex2dec(textView.getText().toString())),2));
                            if(!historyTextView.getText().toString().isEmpty())
                                historyTextView.append('\n'+textView.getText().toString()+"^2="+resStr);
                            else
                                historyTextView.append(textView.getText().toString()+"^2="+resStr);
                            break;
                    }
                    break;
                case R.id.button_two_power:
                    switch (notation){
                        case BIN_NOTATION:
                            resStr = Double.toString(Math.pow(2,Double.parseDouble(Parser.bin2dec(textView.getText().toString()))));
                            if(!historyTextView.getText().toString().isEmpty())
                                historyTextView.append('\n'+"2^"+textView.getText().toString()+"="+resStr);
                            else
                                historyTextView.append("2^"+textView.getText().toString()+"="+resStr);
                            break;
                        case OCT_NOTATION:
                            resStr = Double.toString(Math.pow(2,Double.parseDouble(Parser.oct2dec(textView.getText().toString()))));
                            if(!historyTextView.getText().toString().isEmpty())
                                historyTextView.append('\n'+"2^"+textView.getText().toString()+"="+resStr);
                            else
                                historyTextView.append("2^"+textView.getText().toString()+"="+resStr);
                            break;
                        case DEC_NOTATION:
                            resStr = Double.toString(Math.pow(2,Double.parseDouble(textView.getText().toString())));
                            if(!historyTextView.getText().toString().isEmpty())
                                historyTextView.append('\n'+"2^"+textView.getText().toString()+"="+resStr);
                            else
                                historyTextView.append("2^"+textView.getText().toString()+"="+resStr);
                            break;
                        case HEX_NOTATION:
                            resStr = Double.toString(Math.pow(2,Double.parseDouble(Parser.hex2dec(textView.getText().toString()))));
                            if(!historyTextView.getText().toString().isEmpty())
                                historyTextView.append('\n'+"2^"+textView.getText().toString()+"="+resStr);
                            else
                                historyTextView.append("2^"+textView.getText().toString()+"="+resStr);
                            break;
                    }
                    break;
            }
        }
        if (view.getId()==R.id.button_random)
            textView.setText(Double.toString(Math.random()));
        final ScrollView scrollTextView = findViewById(R.id.scroll_text_view);
        new CountDownTimer(100,100) {
            public void onTick(long millisUntilFinished) {
            }
            public void onFinish() {
                scrollTextView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        }.start();
    }
}
