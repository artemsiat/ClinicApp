package programm.helper_classes;

/**
 * Created by Artem Siatchinov on 7/15/2016.
 */
public class FormatPhone {

    public static String formatPhone(String text) {

        String number = getDigits(text);

        if (number.length() < 3){
            return "(" + number;
        }

        String phoneNumber;
        if (number.length() == 10){

            phoneNumber = ("(" + number.substring(0,3)+ ") " + number.substring(3,6) + "-" + number.substring(6,8) + "-" + number.substring(8,10));
            return phoneNumber;
        }
        else if (number.length() == 11){

            phoneNumber = (number.substring(0,1) + " (" + number.substring(1,4)+ ") " + number.substring(4,7) + "-" + number.substring(7,9) + "-" + number.substring(9,11));
            return phoneNumber;
        }
        else if(number.length() > 11){
            //build String in format (123) 123-12-12
            int index = number.length();
            String lastPart = (" (" + number.substring(index-10,index-7)+ ") " + number.substring(index-7,index-4) + "-" + number.substring(index-4,index-2) + "-" + number.substring(index-2,index));
            phoneNumber = number.substring(0,index-10) + lastPart;
            return phoneNumber;
        }
        else {
            phoneNumber = ("(" + number.substring(0,3)+ ") ");
            int firstDigit = 3;
            int lastDigit = 5;
            while (lastDigit<number.length() ){
                phoneNumber += number.substring(firstDigit, lastDigit) + "-";
                firstDigit += 2;
                lastDigit += 2;
            }
            if (firstDigit < number.length()){
                phoneNumber += number.substring(firstDigit , number.length());
            }
        }

        return phoneNumber;

    }
    private static String getDigits(String text) {
        String digits = "";

        for (int index = 0 ; index < text.length() ; index ++){
            char currChar = text.charAt(index);
            if (Character.isDigit(currChar)){
                digits += currChar;
            }
        }
        return digits;
    }
}
