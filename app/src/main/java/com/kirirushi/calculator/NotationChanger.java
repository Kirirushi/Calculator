package com.kirirushi.calculator;


public class NotationChanger {

    private static final String ALL_DIGITS = "0123456789ABCDEFGHJKLMNOPQRSTUVWXYZ";
    private static final char decimalDot = '.';
    private static final char minusChar = '-';
    public static final String INFINITY = "Infinity";

    public static String changeNotation(String inputString, int inputNotation, int outputNotation) {
        boolean isNegative = false;
        String iString;
        String oString;
        double result = 0.0;
        if (inputString.equals(INFINITY))
            return INFINITY;
        if (inputNotation <= ALL_DIGITS.length() && outputNotation <= ALL_DIGITS.length()) {
            String inputDigits = ALL_DIGITS.substring(0, inputNotation);
            String outputDigits = ALL_DIGITS.substring(0, outputNotation);
            iString = translateNumberFromMantis(inputString);
            if (iString.equals(INFINITY))
                return INFINITY;
            int counterDot = iString.indexOf(decimalDot);
            //перевод из inputNotation в десятичную
            if(iString.indexOf(minusChar)==0){
                isNegative = true;
                iString = iString.substring(1);
            }
            if (counterDot >= 0) {
                iString = iString.substring(0, counterDot) + iString.substring(counterDot + 1);
                int count = 0;
                for (int i = counterDot - 1; i >= counterDot - iString.length(); i--) {
                    char c = iString.charAt(count);
                    int d = inputDigits.indexOf(c);
                    result += d * Math.pow(inputNotation, i);
                    count++;
                }
            } else {
                for (int i = iString.length(); i > 0; i--) {
                    char c = iString.charAt(iString.length() - i);
                    int d = inputDigits.indexOf(c);
                    result += d * Math.pow(inputNotation, i - 1);
                }
            }
            oString = translateNumberFromMantis(Double.toString(result));
            if (oString.equals(INFINITY))
                return INFINITY;
            //перевод из десятичной в outputNotation
            counterDot = oString.indexOf(decimalDot);
            String outString = "";
            if (counterDot > 0) {
                String oStringTrunc = oString.substring(0, counterDot);
                long intOStringTrunc = Long.parseLong(oStringTrunc);
                while (intOStringTrunc > 0) {
                    long outDigit = intOStringTrunc % outputNotation;
                    intOStringTrunc = intOStringTrunc / outputNotation;
                    outString = outputDigits.charAt((int) outDigit) + outString;
                }

                String oStringNotTrunc = "0" + oString.substring(counterDot);
                String tString = "";
                double notTrunc = Double.parseDouble(oStringNotTrunc);
                int counter = 0;
                while (counter < 9) {
                    notTrunc *= outputNotation;
                    int d = (int) notTrunc;
                    tString += outputDigits.charAt(d);
                    notTrunc -= d;
                    counter++;
                }
                if (outString.isEmpty())
                    outString = "0";
                outString = outString + decimalDot + tString;
            } else {
                long intOString = Long.parseLong(oString);
                while (intOString > 0) {
                    long outDigit = intOString % outputNotation;
                    intOString = intOString / outputNotation;
                    outString = outputDigits.charAt((int) outDigit) + outString;
                }
            }
            if (isNegative)
                return minusChar + outString;
            return outString;
        } else {
            return null;
        }
    }

    private static String translateNumberFromMantis(String string) {
        int counterDot;
        int indexOfMantis = string.indexOf("E");
        if (indexOfMantis > 0) {
            int powerOfMantis = Integer.parseInt(string.substring(indexOfMantis + 1));
            counterDot = string.indexOf(decimalDot);
            String numberWithoutMantis = string.substring(0, counterDot) +
                    string.substring(counterDot + 1, indexOfMantis);
            if(powerOfMantis>=17)
                return INFINITY;
            if (powerOfMantis > 0)
                if (numberWithoutMantis.indexOf(minusChar) == 0)
                    for (int i = numberWithoutMantis.length() + 1; i <= powerOfMantis + 1; i++)
                        numberWithoutMantis = numberWithoutMantis + "0";
                else
                    for (int i = numberWithoutMantis.length(); i <= powerOfMantis; i++)
                        numberWithoutMantis = numberWithoutMantis + "0";
            else if (numberWithoutMantis.indexOf(minusChar) == 0) {
                numberWithoutMantis = numberWithoutMantis.substring(1);
                for (int i = 0; i < Math.abs(powerOfMantis); i++)
                    numberWithoutMantis = "0" + numberWithoutMantis;
                numberWithoutMantis = minusChar + numberWithoutMantis;
                numberWithoutMantis = numberWithoutMantis.substring(0, 2)
                        + decimalDot + numberWithoutMantis.substring(2);
            } else {
                for (int i = 0; i < Math.abs(powerOfMantis); i++)
                    numberWithoutMantis = "0" + numberWithoutMantis;
                numberWithoutMantis = numberWithoutMantis.substring(0, 1)
                    + decimalDot + numberWithoutMantis.substring(1);
            }
            return numberWithoutMantis;
        }
        return string;
    }
}
