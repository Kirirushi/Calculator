package com.kirirushi.calculator;


public class NotationChanger {

    private static final String ALL_DIGITS = "0123456789ABCDEFGHJKLMNOPQRSTUVWXYZ";
    private static final char decimalDot = '.';
    private static final char minusChar = '-';

    public static String changeNotation(String inputString, int inputNotation, int outputNotation){
        boolean isNegative = false;
        String iString = inputString;
        String oString;
        double result = 0.0;
        if(inputNotation<=ALL_DIGITS.length()&&outputNotation<=ALL_DIGITS.length()){
            String inputDigits = ALL_DIGITS.substring(0,inputNotation);
            String outputDigits = ALL_DIGITS.substring(0,outputNotation);
            if(iString.indexOf(minusChar)==0){
                isNegative = true;
                iString = iString.substring(1);
            }
        //перевод из inputNotation в десятичную
            int counterDot = iString.indexOf(decimalDot);
            if(counterDot>=0){
                iString = iString.substring(0,counterDot)+iString.substring(counterDot+1);
                int count = 0;
                for(int i = counterDot-1;i>=counterDot-iString.length();i--){
                    char c = iString.charAt(count);
                    int d = inputDigits.indexOf(c);
                    result+=d*Math.pow(inputNotation,i);
                    count++;
                }
            }else
            {
                for(int i = iString.length();i>0;i--){
                    char c = iString.charAt(iString.length()-i);
                    int d = inputDigits.indexOf(c);
                    result+=d*Math.pow(inputNotation,i-1);
                }
            }
        //перевод из десятичной в outputNotation
            oString = Double.toString(result);
            counterDot = oString.indexOf(decimalDot);
            String outString = "";
            if(counterDot>0) {
                String oStringTrunc = oString.substring(0,counterDot);
                int intOStringTrunc = Integer.parseInt(oStringTrunc);
                while (intOStringTrunc > 0){
                    int outDigit = intOStringTrunc % outputNotation;
                    intOStringTrunc = intOStringTrunc / outputNotation;
                    outString = outputDigits.charAt(outDigit) + outString;
                }
                String oStringNotTrunc = "0" + oString.substring(counterDot);
                String tString = "";
                double notTrunc = Double.parseDouble(oStringNotTrunc);
                int counter = 0;
                while(counter<6){
                    notTrunc*=outputNotation;
                    int d = (int) notTrunc;
                    tString += outputDigits.charAt(d);
                    notTrunc -= d;
                    counter++;
                }
                outString = outString + decimalDot +tString;
            }else{
                int intOString = Integer.parseInt(oString);
                while (intOString > 0){
                    int outDigit = intOString % outputNotation;
                    intOString = intOString / outputNotation;
                    outString = outputDigits.charAt(outDigit) + outString;
                }
            }
            if (isNegative)
                return minusChar + outString;
            return outString;
        }else{
            return null;
        }
    }
}
