package com.udea.proyecto.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ReservaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static Reserva getReservaSample1() {
        return new Reserva().id(1L).codigoReserva("codigoReserva1").asiento("asiento1");
    }

    public static Reserva getReservaSample2() {
        return new Reserva().id(2L).codigoReserva("codigoReserva2").asiento("asiento2");
    }

    public static Reserva getReservaRandomSampleGenerator() {
        return new Reserva()
            .id(longCount.incrementAndGet())
            .codigoReserva(UUID.randomUUID().toString())
            .asiento(UUID.randomUUID().toString());
    }
}
