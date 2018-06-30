package com.kirirushi.calculator;

import android.support.annotation.NonNull;

public class Parser {

    //  Объявление лексем
    private final int NONE = 0;         //  FAIL
    private final int DELIMITER = 1;    //  Разделитель(+-*/^=, ")", "(" )
    private final int NUMBER = 3;       //  Число

    //  Объявление констант синтаксических ошибок
    private final int SYNTAXERROR = 0;  //  Синтаксическая ошибка (10 + 5 6 / 1)
    private final int UNBALPARENS = 1;  //  Несовпадение количества открытых и закрытых скобок
    private final int NOEXP = 2;        //  Отсутствует выражение при запуске анализатора
    private final int DIVBYZERO = 3;    //  Ошибка деления на ноль

    //  Лексема, определяющая конец выражения
    private final String EOF = "\0";

    private String exp;     //  Ссылка на строку с выражением
    private int explds;     //  Текущий индекс в выражении
    private String token;   //  Сохранение текущей лексемы
    private int tokType;    //  Сохранение типа лексемы


    public String toString(){
        return String.format("Exp = {0}\nexplds = {1}\nToken = {2}\nTokType = {3}", exp, explds,
                token, tokType);
    }

    //  Получить следующую лексему
    private void getToken(){
        tokType = NONE;
        token = "";

        //  Проверка на окончание выражения
        if(explds == exp.length()){
            token = EOF;
            return;
        }
        //  Проверка на пробелы, если есть пробел - игнорируем его.
        while(explds < exp.length() && Character.isWhitespace(exp.charAt(explds)))
            ++ explds;
        //  Проверка на окончание выражения
        if(explds == exp.length()){
            token = EOF;
            return;
        }
        if(isDelim(exp.charAt(explds))){
            token += exp.charAt(explds);
            explds++;
            tokType = DELIMITER;
        }
        else if (Character.isLetterOrDigit(exp.charAt(explds))) {
            while(!isDelim(exp.charAt(explds))){
                token += exp.charAt(explds);
                explds++;
                if(explds >= exp.length())
                    break;
            }
            tokType = NUMBER;
        }
        else {
            token = EOF;
        }
    }

    private boolean isDelim(char charAt) {
        return (" +-/*%^=()".indexOf(charAt)) != -1;
    }

    //  Точка входа анализатора
    public double evaluate(String expStr,int notation) throws ParserException{

        double result;

        exp = expStr;
        explds = 0;
        getToken();

        if(token.equals(EOF))
            handleErr(NOEXP);   //  Нет выражения

        //  Анализ и вычисление выражения
        result = evalExp2(notation);

        if(!token.equals(EOF))
            handleErr(SYNTAXERROR);

        return result;
    }

    //  Сложить или вычислить два терма
    private double evalExp2(int notation) throws ParserException{

        char op;
        double result;
        double partialResult;
        result = evalExp3(notation);
        while((op = token.charAt(0)) == '+' ||
                op == '-'){
            getToken();
            partialResult = evalExp3(notation);
            switch(op){
                case '-':
                    result -= partialResult;
                    break;
                case '+':
                    result += partialResult;
                    break;
            }
        }
        return result;
    }

    //  Умножить или разделить два фактора
    private double evalExp3(int notation) throws ParserException{

        char op;
        double result;
        double partialResult;

        result = evalExp4(notation);
        while((op = token.charAt(0)) == '*' ||
                op == '/' | op == '%'){
            getToken();
            partialResult = evalExp4(notation);
            switch(op){
                case '*':
                    result *= partialResult;
                    break;
                case '/':
                    if(partialResult == 0.0)
                        handleErr(DIVBYZERO);
                    result /= partialResult;
                    break;
                case '%':
                    if(partialResult == 0.0)
                        handleErr(DIVBYZERO);
                    result %= partialResult;
                    break;
            }
        }
        return result;
    }

    //  Выполнить возведение в степень
    private double evalExp4(int notation) throws ParserException{

        double result;
        double partialResult;
        double ex;
        int t;
        result = evalExp5(notation);
        if(token.equals("^")){
            getToken();
            partialResult = evalExp4(notation);
            ex = result;
            if(partialResult == 0.0){
                result = 1.0;
            }else
                for(t = (int)partialResult - 1; t >  0; t--)
                    result *= ex;
        }
        return result;
    }

    //  Определить унарные + или -
    private double evalExp5(int notation) throws ParserException{
        double result;

        String op;
        op = " ";

        if((tokType == DELIMITER) && token.equals("+") ||
                token.equals("-")){
            op = token;
            getToken();
        }
        result = evalExp6(notation);
        if(op.equals("-"))
            result =  -result;
        return result;
    }

    //  Обработать выражение в скобках
    private double evalExp6(int notaion) throws ParserException{
        double result;

        if(token.equals("(")){
            getToken();
            result = evalExp2(notaion);
            if(!token.equals(")"))
                handleErr(UNBALPARENS);
            getToken();
        }
        else
            result = atom(notaion);
        return result;
    }

    //  Получить значение числа
    private double atom(int notation) throws ParserException{

        double result = 0.0;
        switch(tokType){
            case NUMBER:
                try{
                    switch (notation){
                        case MainActivity.BIN_NOTATION:
                            result = Double.parseDouble(bin2dec(token));
                            break;
                        case MainActivity.OCT_NOTATION:
                            result = Double.parseDouble(oct2dec(token));
                            break;
                        case MainActivity.DEC_NOTATION:
                            result = Double.parseDouble(token);
                            break;
                        case MainActivity.HEX_NOTATION:
                            result = Double.parseDouble(hex2dec(token));
                            break;
                    }
                }
                catch(NumberFormatException exc){
                    handleErr(SYNTAXERROR);
                }
                getToken();

                break;
            default:
                handleErr(SYNTAXERROR);
                break;
        }
        return result;
    }

    //  Кинуть ошибку
    private void handleErr(int nOEXP2) throws ParserException{

        String[] err  = {
                "Ошибка синтаксиса",
                "Несбалансированные скобки",
                "Пустая строка",
                "Деление на ноль"
        };
        throw new ParserException(err[nOEXP2]);
    }
@NonNull
    public static String bin2dec(@NonNull String binNumber) {
        String digits = "01";
        double result = 0;
        for (int i = 0; i < binNumber.length(); i++) {
            char c = binNumber.charAt(i);
            int d = digits.indexOf(c);
            result = 2*result + d;
        }
        return Double.toString(result);
    }
    @NonNull
    public static String oct2dec(@NonNull String octNumber) {
        String digits = "01234567";
        double result = 0;
        for (int i = 0; i < octNumber.length(); i++) {
            char c = octNumber.charAt(i);
            int d = digits.indexOf(c);
            result = 8*result + d;
        }
        return Double.toString(result);
    }
    public static String hex2dec(String hexNumber) {
        String digits = "0123456789ABCDEF";
        hexNumber = hexNumber.toUpperCase();
        double result = 0;
        for (int i = 0; i < hexNumber.length(); i++) {
            char c = hexNumber.charAt(i);
            int d = digits.indexOf(c);
            result = 16*result + d;
        }
        return Double.toString(result);
    }
    public static String dec2bin(String decNumber) {
        return Long.toBinaryString(Math.round(Double.parseDouble(decNumber)));
    }
    public static String dec2oct(String decNumber) {
        return Long.toOctalString(Math.round(Double.parseDouble(decNumber)));
    }
    public static String dec2hex(String decNumber) {
        return Long.toHexString(Math.round(Double.parseDouble(decNumber))).toUpperCase();
    }
}

