package programm.helper_classes;

import java.time.LocalDate;

/**
 * Created by Artem Siatchinov on 8/4/2016.
 */
public class FormatDate {


    /**
     * Converts date to format YYYY MM DD. Used in Working date class
     * @param value
     * @return
     */
    public static int dateToWorkingDay(LocalDate value) {
        //Format YYYY MM DD
        int year = value.getYear();
        int month = value.getMonthValue();
        int day = value.getDayOfMonth();

        //2016*100 =201600 month(6) = 201606 * 100 = 20160600+ day(17) = 20160617
        int returnValue = (year * 100 + month) * 100 + day;

        return returnValue;
    }


    /**
     * Creates String representation for the day  Пон 12 Апреля 2016 г.
     * @param input
     * @return
     */
    public static String formatWorkingDay(int input){

        //input = YYYY MM DD
        //output = Пон 12 Апреля 2016 г.

        String day;

        int thisYear = input/10000;
        int thisMonth = (input/100)%100;
        int thisDay = input%100;

        LocalDate localDate = LocalDate.of(thisYear, thisMonth, thisDay);

        String month = getMonth(thisMonth);
        String dayOfWeek = getDayOfWeek(localDate.getDayOfWeek().getValue());

        day = thisDay + " " + month + " " + thisYear + " г. " + dayOfWeek;
        return day;
    }

    private static String getMonth(int number){
        //indexes are 2-4
        //String number = digits.substring(2,4);

        String month;
        switch (number){
            case 1:
                month = "Января";
                break;
            case 2:
                month = "Февраля";
                break;
            case  3:
                month = "Марта";
                break;
            case 4:
                month = "Апреля";
                break;
            case 5:
                month = "Мая";
                break;
            case 6:
                month = "Июня";
                break;
            case 7:
                month = "Июля";
                break;
            case 8:
                month = "Августа";
                break;
            case 9:
                month = "Сентября";
                break;
            case 10:
                month = "Октября";
                break;
            case 11:
                month = "Ноября";
                break;
            case 12:
                month = "Декабря";
                break;
            default:
                month = "н/д.";
        }
        return month;
    }
    private static String getDayOfWeek(int value) {
        String dayOfWeek;
        switch (value){
            //Short Form
/*            case 1:
                dayOfWeek = "Пон.";
                break;
            case 2:
                dayOfWeek = "Втр.";
                break;
            case  3:
                dayOfWeek = "Срд.";
                break;
            case 4:
                dayOfWeek = "Чет.";
                break;
            case 5:
                dayOfWeek = "Пят.";
                break;
            case 6:
                dayOfWeek = "Суб.";
                break;
            case 7:
                dayOfWeek = "Вос.";
                break;*/
            //Full form
            case 1:
                dayOfWeek = "Понедельник";
                break;
            case 2:
                dayOfWeek = "Вторник";
                break;
            case  3:
                dayOfWeek = "Среда";
                break;
            case 4:
                dayOfWeek = "Четверг";
                break;
            case 5:
                dayOfWeek = "Пятница";
                break;
            case 6:
                dayOfWeek = "Суббота";
                break;
            case 7:
                dayOfWeek = "Воскресенье";
                break;

            default:
                dayOfWeek = "н/д.";
        }
        return dayOfWeek;
    }

    /**
     * output = Пон 12 Апреля 2016 г.
     */
    public static String convertDateToMyFormat(LocalDate localDate){

        String dayOfWeek = getDayOfWeek(localDate.getDayOfWeek().getValue());
        String month = getMonth(localDate.getMonthValue());

        String myFormat = dayOfWeek + " " + localDate.getDayOfMonth() + " " + month + " " + localDate.getYear() + " г.";

        return myFormat;
    }

}
