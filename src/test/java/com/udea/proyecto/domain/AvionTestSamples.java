package com.udea.proyecto.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class AvionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Avion getAvionSample1() {
        return new Avion().id(1L).modelo("modelo1").capacidad(1).matricula("matricula1");
    }

    public static Avion getAvionSample2() {
        return new Avion().id(2L).modelo("modelo2").capacidad(2).matricula("matricula2");
    }

    public static Avion getAvionRandomSampleGenerator() {
        return new Avion()
            .id(longCount.incrementAndGet())
            .modelo(UUID.randomUUID().toString())
            .capacidad(intCount.incrementAndGet())
            .matricula(UUID.randomUUID().toString());
    }
}
