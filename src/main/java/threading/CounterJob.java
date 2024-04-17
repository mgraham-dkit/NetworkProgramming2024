package threading;

public class CounterJob implements Runnable {
    private int value;
    private int id;

    public CounterJob(int id, int value){
        this.id = id;
        this.value = value;
    }

    public void run(){
        System.out.println("Thread " + id + " starting!");
        for(int i = 0; i < value; i++){
            System.out.println(id + ") " + i);
        }
    }
}
