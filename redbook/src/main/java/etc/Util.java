package etc;

import java.util.Scanner;
import java.util.TreeSet;

public class Util {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(Util.class.getResourceAsStream("redbook.txt"));
        TreeSet<String> set = new TreeSet<String>();
        while (scanner.hasNext()) {
            set.add(scanner.next());
        }
        for (String item : set) {
            System.out.println("<option value='" + item + "' selected>" + item);
        }
    }

}
