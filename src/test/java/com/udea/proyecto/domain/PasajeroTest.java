package com.udea.proyecto.domain;

import static com.udea.proyecto.domain.PasajeroTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.udea.proyecto.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PasajeroTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pasajero.class);
        Pasajero pasajero1 = getPasajeroSample1();
        Pasajero pasajero2 = new Pasajero();
        assertThat(pasajero1).isNotEqualTo(pasajero2);

        pasajero2.setId(pasajero1.getId());
        assertThat(pasajero1).isEqualTo(pasajero2);

        pasajero2 = getPasajeroSample2();
        assertThat(pasajero1).isNotEqualTo(pasajero2);
    }
}
