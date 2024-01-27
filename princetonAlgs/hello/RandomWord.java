/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;

public class RandomWord {
    public static void main(String[] args) {
        String champion = StdIn.readString(); // first word set as champion
        int i = 2; // set to 2 so the odds of the next word are 1/2

        while (!StdIn.isEmpty()) {
            String challenger = StdIn.readString();
            double p = 1.0 / i;
            if (StdRandom.bernoulli(p)) {
                champion = challenger;
            }
            i++;
        }
        System.out.println(champion);
    }
}
