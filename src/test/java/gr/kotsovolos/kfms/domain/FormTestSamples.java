package gr.kotsovolos.kfms.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class FormTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Form getFormSample1() {
        return new Form().id(1L).formName("formName1").version("version1");
    }

    public static Form getFormSample2() {
        return new Form().id(2L).formName("formName2").version("version2");
    }

    public static Form getFormRandomSampleGenerator() {
        return new Form().id(longCount.incrementAndGet()).formName(UUID.randomUUID().toString()).version(UUID.randomUUID().toString());
    }
}
