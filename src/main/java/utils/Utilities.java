package utils;

import java.util.List;
import java.util.Random;

public class Utilities {

    /**
     * This method takes in a decimal point number and truncates it to two decimal places.  Note
     * that the method does NOT round when truncating; the numbers after the two decimal places are
     * just removed.
     * <p>
     * The method does the truncating in this manner:
     * - multiply the number by 100 e.g. 16.543235523 * 100 = 1654.3235523
     * - cast the multiplied number as an in e.g. 1654.3235523 = 1654
     * - finally, the multiplied and casted number is divided by 100 and returned e.g. 1654 = 16.54
     *
     * @param number Number to be truncated to two decimal places
     * @return the number, passed as a parameter, truncated to two decimal places (note: not rounded)
     */
    public static double toTwoDecimalPlaces(double number) {
        return (double) ((number * 100) / 100.0);
    }

    /**
     * This method returns Y if the booleanToConvert value is true. Returns N otherwise.
     *
     * @param booleanToConvert The boolean value that will be used to determine Y/N
     * @return Returns Y if the booleanToConvert value is true. Returns N otherwise.
     */
    public static char booleanToYN(boolean booleanToConvert) {
        return booleanToConvert ? 'Y' : 'N';
    }

    /**
     * This method returns true if the charToConvert value is Y or y. Returns false in all other cases.
     *
     * @param charToConvert The char value that will be used to determine true/false.
     * @return Returns true if the charToConvert value is Y or y. Returns false otherwise.
     */
    public static boolean YNtoBoolean(char charToConvert) {
        return ((charToConvert == 'y') || (charToConvert == 'Y'));
    }


    /**
     * This method returns true if the numberToCheck is between min and max (both inclusive)
     *
     * @param numberToCheck The number whose range is being checked.
     * @param min The minimum range number to check against (inclusive)
     * @param max The maximum range number to check against (inclusive)
     * @return Returns true if the numberToCheck is between min and max (both inclusive), false otherwise.
     */
    public static boolean validRange(int numberToCheck, int min, int max) {
        return ((numberToCheck >= min) && (numberToCheck <= max));
    }

    public static boolean validRange(double numbertoCheck, double min, double max, double delta) {
        return ((numbertoCheck >= (min-delta)) && (numbertoCheck <= (max+delta)));

    }

    public static String truncateString(String stringToTruncate, int length){
        if (stringToTruncate != null) {
            if (stringToTruncate.length() <= length) {
                return stringToTruncate;
            } else {
                return stringToTruncate.substring(0, length);
            }
        }
        else
            return null;
    }

    public static boolean validStringlength(String strToCheck, int maxLength){
        if (strToCheck != null ){
            return strToCheck.length() <= maxLength;
        }
        return false;
    }

    public static boolean isValidIndex(List list, int indexToCheck){
        return ((indexToCheck >= 0) && (indexToCheck < list.size()));
    }

    public static String uniqueCodeGenerator() {
        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String numbers = "0123456789";
        StringBuilder code = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 3; i++) {
            int randomUpper = random.nextInt(upper.length());
            char randomChar = upper.charAt(randomUpper);
            code.append(randomChar);
        }

        for (int i = 0; i < 3; i++) {
            int randomNumber = random.nextInt(numbers.length());
            char randomChar = numbers.charAt(randomNumber);
            code.append(randomChar);
        }

        for (int i = 0; i < 3; i++) {
            int randomUpper = random.nextInt(upper.length());
            char randomChar = upper.charAt(randomUpper);
            code.append(randomChar);
        }

        return code.toString();
    }

    public static String[] countries = {"Afghanistan", "Albania", "Algeria", "Andorra", "Angola", "Antigua and Barbuda",
            "Argentina", "Armenia", "Australia", "Austria", "Azerbaijan", "Bahamas", "Bahrain",
            "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin", "Bhutan",
            "Bolivia", "Bosnia and Herzegovina", "Botswana", "Brazil", "Brunei", "Bulgaria",
            "Burkina Faso", "Burundi", "Cabo Verde", "Cambodia", "Cameroon", "Canada",
            "Central African Republic", "Chad", "Chile", "China", "Colombia", "Comoros",
            "Congo (Congo-Brazzaville)", "Costa Rica", "Cote d'Ivoire", "Croatia", "Cuba",
            "Cyprus", "Czechia", "Democratic Republic of the Congo (Congo-Kinshasa)", "Denmark",
            "Djibouti", "Dominica", "Dominican Republic", "Ecuador", "Egypt", "El Salvador",
            "Equatorial Guinea", "Eritrea", "Estonia", "Eswatini", "Ethiopia", "Fiji",
            "Finland", "France", "Gabon", "Gambia", "Georgia", "Germany", "Ghana",
            "Greece", "Grenada", "Guatemala", "Guinea", "Guinea-Bissau", "Guyana", "Haiti",
            "Holy See", "Honduras", "Hungary", "Iceland", "India", "Indonesia", "Iran", "Iraq",
            "Ireland", "Israel", "Italy", "Jamaica", "Japan", "Jordan", "Kazakhstan",
            "Kenya", "Kiribati", "Kuwait", "Kyrgyzstan", "Laos", "Latvia", "Lebanon", "Lesotho",
            "Liberia", "Libya", "Liechtenstein", "Lithuania", "Luxembourg", "Macedonia (North Macedonia)",
            "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands",
            "Mauritania", "Mauritius", "Mexico", "Micronesia", "Moldova", "Monaco", "Mongolia",
            "Montenegro", "Morocco", "Mozambique", "Myanmar (formerly Burma)", "Namibia", "Nauru",
            "Nepal", "Netherlands", "New Zealand", "Nicaragua", "Niger", "Nigeria", "North Korea",
            "Norway", "Oman", "Pakistan", "Palau", "Palestine State", "Panama", "Papua New Guinea",
            "Paraguay", "Peru", "Philippines", "Poland", "Portugal", "Qatar", "Romania",
            "Russia", "Rwanda", "Saint Kitts and Nevis", "Saint Lucia", "Saint Vincent and the Grenadines",
            "Samoa", "San Marino", "Sao Tome and Principe", "Saudi Arabia","Sealand", "Senegal", "Serbia",
            "Seychelles", "Sierra Leone", "Singapore", "Slovakia", "Slovenia", "Solomon Islands",
            "Somalia", "South Africa", "South Korea", "South Sudan", "Spain", "Sri Lanka", "Sudan",
            "Suriname", "Sweden", "Switzerland", "Syria", "Taiwan", "Tajikistan", "Tanzania",
            "Thailand", "Timor-Leste", "Togo", "Tonga", "Trinidad and Tobago", "Tunisia",
            "Turkey", "Turkmenistan", "Tuvalu", "Uganda", "Ukraine", "United Arab Emirates",
            "United Kingdom", "United States of America", "Uruguay", "Uzbekistan", "Vanuatu",
            "Venezuela", "Vietnam", "Yemen", "Zambia", "Zimbabwe"};


}
