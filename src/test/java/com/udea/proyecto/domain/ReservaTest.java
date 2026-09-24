package com.udea.proyecto.domain;

import static com.udea.proyecto.domain.PasajeroTestSamples.*;
import static com.udea.proyecto.domain.ReservaTestSamples.*;
import static com.udea.proyecto.domain.VueloTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.udea.proyecto.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReservaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Reserva.class);
        Reserva reserva1 = getReservaSample1();
        Reserva reserva2 = new Reserva();
        assertThat(reserva1).isNotEqualTo(reserva2);

        reserva2.setId(reserva1.getId());
        assertThat(reserva1).isEqualTo(reserva2);

        reserva2 = getReservaSample2();
        assertThat(reserva1).isNotEqualTo(reserva2);
    }

    @Test
    void pasajeroTest() {
        Reserva reserva = getReservaRandomSampleGenerator();
        Pasajero pasajeroBack = getPasajeroRandomSampleGenerator();

        reserva.setPasajero(pasajeroBack);
        assertThat(reserva.getPasajero()).isEqualTo(pasajeroBack);

        reserva.pasajero(null);
        assertThat(reserva.getPasajero()).isNull();
    }

    @Test
    void vueloTest() {
        Reserva reserva = getReservaRandomSampleGenerator();
        Vuelo vueloBack = getVueloRandomSampleGenerator();

        reserva.setVuelo(vueloBack);
        assertThat(reserva.getVuelo()).isEqualTo(vueloBack);

        reserva.vuelo(null);
        assertThat(reserva.getVuelo()).isNull();
    }
}
