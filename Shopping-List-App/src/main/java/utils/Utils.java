package utils;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Utils {

    public static String getDateAndTime(String format) {
        // Define the ZoneId for Colombo (GMT+5:30)
        ZoneId colomboZone = ZoneId.of("Asia/Colombo");

        // Get current date and time in Colombo timezone
        ZonedDateTime colomboDateTime = ZonedDateTime.now(colomboZone);

        // Format the date and time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return colomboDateTime.format(formatter);
    }

    public static String getDateAndTime() {
        // Default format: yyyy-MM-dd HH:mm:ss
        return getDateAndTime("yyyy-MM-dd HH:mm:ss");
    }

    public static int generateRandomNumber() {
        // Define the range for 9-digit numbers
        int min = 100000000;
        int max = 999999999;

        return min + (int)(Math.random() * ((max - min) + 1));
    }

    public static int generateRandomNumber4() {
        // Define the range for 4-digit numbers
        int min = 1000;
        int max = 9999;

        return min + (int)(Math.random() * ((max - min) + 1));
    }

    public static String generateRandomID() {

        return UUID.randomUUID().toString();

    }

    public static String nextId(String currentId){

        String[] idParts = currentId.split("-");
        int numLength = idParts[1].length();
        int idNum = Integer.parseInt(idParts[1]);
        idNum++;

        StringBuilder idNumStr = new StringBuilder(String.valueOf(idNum));

        for(int i = 1; i < numLength; i++){

            if (idNum < Math.pow(10, i)){
                idNumStr.insert(0, "0");
            }

        }

        return idParts[0] + "-" + idNumStr;

    }

}
