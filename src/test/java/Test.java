import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rs.jamie.HeadUtil;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class Test {

    private static final Logger log = LoggerFactory.getLogger(Test.class);

    public static void main(String[] args) {
        HeadUtil headUtil = new HeadUtil.Builder().enableCapes(true).setCacheManager(HeadUtil.SkinCacheType.CAFFEINE.get()).build();
        UUID uuid = UUID.fromString("3d5a16c7-708c-4dfc-b0f5-e8d7bc790a33");
        System.out.println("Starting Test: Request player's skin and cape");
        System.out.println("\n");
        headUtil.getPlayerSkin(uuid).thenAcceptAsync((profile) -> {
            System.out.println("Skin: "+profile.skin());
            System.out.println("Head: "+profile.head());
            System.out.println("Cape: "+profile.cape());
            System.out.println("\n");
        }).join();
        loopTest(headUtil, uuid, "Caffeine");
        headUtil = new HeadUtil.Builder().enableCapes(true).setCacheManager(HeadUtil.SkinCacheType.REDIS.get()).build();
        loopTest(headUtil, uuid, "Redis");
    }

    private static void loopTest(HeadUtil headUtil, UUID uuid, String test) {
        int count = 100000;
        AtomicLong totalTime = new AtomicLong(0);
        for(int i=0; i<5000; i++) {
            headUtil.getPlayerSkin(uuid).join();
        }

        for(int i=0; i<count; i++) {
            long ms = System.currentTimeMillis();
            headUtil.getPlayerSkin(uuid).thenAcceptAsync((profile) -> {
                long time = System.currentTimeMillis() - ms;
                totalTime.addAndGet(time);
            }).join();
        }

        System.out.println("\nFinished "+test+" test with a total time of "+totalTime.get()+"ms and "+count+" requests");
        System.out.println("Averaged "+((double)totalTime.get() / (double)count)+"ms per request");
    }

}
