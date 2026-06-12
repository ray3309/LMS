package util;

public class StringUtil {

    public static String cutString(
        String str,
        int maxLength
    ) {

        if(str == null){

            return "";
        }

        str = str.trim();

        if(str.length() > maxLength){

            return str.substring(
                0,
                maxLength
            );
        }

        return str;
    }
}