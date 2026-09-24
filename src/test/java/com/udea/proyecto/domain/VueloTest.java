package com.udea.proyecto.domain;

import static com.udea.proyecto.domain.AvionTestSamples.*;
import static com.udea.proyecto.domain.VueloTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.udea.proyecto.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VueloTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Vuelo.class);
        Vuelo vuelo1 = getVueloSample1();
        Vuelo vuelo2 = new Vuelo();
        assertThat(vuelo1).isNotEqualTo(vuelo2);

        vuelo2.setId(vuelo1.getId());
        assertThat(vuelo1).isEqualTo(vuelo2);

        vuelo2 = getVueloSample2();
        assertThat(vuelo1).isNotEqualTo(vuelo2);
    }

    @Test
    void avionTest() {
        Vuelo vuelo = getVueloRandomSampleGenerator();
        Avion avionBack = getAvionRandomSampleGenerator();

        vuelo.setAvion(avionBack);
        assertThat(vuelo.getAvion()).isEqualTo(avionBack);

        vuelo.avion(null);
        assertThat(vuelo.getAvion()).isNull();
    }
}
