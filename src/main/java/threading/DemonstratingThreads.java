package threading;

import java.util.Random;
import java.util.Scanner;

public class DemonstratingThreads {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("How many counters do you want?");
        int numJobs = input.nextInt();

        for(int i = 0; i < numJobs; i++){
            Random generator = new Random();
            int value = generator.nextInt(100);
            CounterJob counter = new CounterJob(i, value);
            Thread worker = new Thread(counter);
            worker.start();
        }
    }
}
