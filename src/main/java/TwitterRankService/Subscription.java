package TwitterRankService;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Subscription {
    private enum STATUS_VALUES {TRACKING, FAILED, STARTING};
    private STATUS_VALUES status;
    private String topic;

    public Subscription(String topic) {
        this.topic = topic;
    }

    public void startTracking(){
        status = STATUS_VALUES.STARTING;
        try {
            trackInterest(topic);
        } catch (InterruptedException e) {
            status = STATUS_VALUES.FAILED;
            e.printStackTrace();
        }
    }
    @JsonIgnore
    public void trackInterest(String interest) throws InterruptedException {
        String consumerKey = " ",
                consumerSecret = " ",
                token = "-",
                secret = " ";
        Runnable r = () -> {
            try {
                new TwitterProducer().run(consumerKey, consumerSecret, token, secret, interest);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.submit(r);
        status = STATUS_VALUES.TRACKING;
    }
}
