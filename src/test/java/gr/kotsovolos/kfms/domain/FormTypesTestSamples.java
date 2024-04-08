package gr.kotsovolos.kfms.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class FormTypesTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static FormTypes getFormTypesSample1() {
        return new FormTypes().id(1L).type("type1").description("description1");
    }

    public static FormTypes getFormTypesSample2() {
        return new FormTypes().id(2L).type("type2").description("description2");
    }

    public static FormTypes getFormTypesRandomSampleGenerator() {
        return new FormTypes().id(longCount.incrementAndGet()).type(UUID.randomUUID().toString()).description(UUID.randomUUID().toString());
    }
}
