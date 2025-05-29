import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rs.jamie.HeadUtil;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class Test {

    private static final Logger log = LoggerFactory.getLogger(Test.class);

    public static void main(String[] args) {
        HeadUtil headUtil = new HeadUtil.Builder()
                .enableCapes(true)
                .setCacheType(HeadUtil.SkinCacheType.CAFFEINE)
                .build();
        UUID uuid = UUID.fromString("0bfea1b6-8ed9-4eb9-93da-c4273bfa0a09");
        System.out.println("Starting Test: Request player's skin and cape");
        System.out.println("\n");
        headUtil.getPlayerTextures(uuid).thenAcceptAsync((profile) -> {
            System.out.println("Skin: "+profile.skin());
            System.out.println("Head: "+profile.head());
            System.out.println("Cape: "+profile.cape());
            System.out.println("\n");
        }).join();
        long time1 = loopTest(headUtil, uuid, "Caffeine");
        headUtil = new HeadUtil.Builder().enableCapes(true).setCacheType(HeadUtil.SkinCacheType.REDIS).build();
        long time2 = loopTest(headUtil, uuid, "Redis");
        if(time1 > time2) {
            long time = time1 - time2;
            System.out.println("\nRedis was "+time+"ms faster! ("+(time/100000)+"ms per request)");
        } else {
            long time = time2 - time1;
            System.out.println("\nCaffeine was "+time+"ms faster! ("+((double)time/(double)100000)+"ms faster per request)");
        }

    }

    private static long loopTest(HeadUtil headUtil, UUID uuid, String test) {
        int count = 100000;
        AtomicLong totalTime = new AtomicLong(0);
        for(int i=0; i<5000; i++) {
            headUtil.getPlayerTextures(uuid).join();
        }

        for(int i=0; i<count; i++) {
            long ms = System.currentTimeMillis();
            headUtil.getPlayerTextures(uuid).thenAcceptAsync((profile) -> {
                long time = System.currentTimeMillis() - ms;
                totalTime.addAndGet(time);
            }).join();
        }

        System.out.println("\nFinished "+test+" test with a total time of "+totalTime.get()+"ms and "+count+" requests");
        System.out.println("Averaged "+((double)totalTime.get() / (double)count)+"ms per request");
        return totalTime.get();
    }

}
