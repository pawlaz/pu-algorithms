/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

public class HelloGoodbye {
    public static void main(String[] args) {
        String firstName = args[0];
        String secondName = args[1];
        System.out.printf("Hello %s and %s.%n", firstName, secondName);
        System.out.printf("Goodbye %s and %s.%n", secondName, firstName);
    }
}
