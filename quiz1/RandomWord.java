import java.util.*;
import edu.princeton.cs.introcs.StdRandom;

public class RandomWord {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Scanner line = new Scanner(scanner.nextLine());

        int i = 0;
        String champion = null;
        while (line.hasNext()) {
            String tmp = line.next();
            i++;
            if(i==1 || StdRandom.bernoulli(1/(double) i)){
                champion = tmp;
            }
        }
        System.out.println(champion);
    }
}
