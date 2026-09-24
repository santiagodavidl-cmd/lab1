package com.udea.proyecto.domain;

import static com.udea.proyecto.domain.AvionTestSamples.*;
import static com.udea.proyecto.domain.VueloTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.udea.proyecto.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AvionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Avion.class);
        Avion avion1 = getAvionSample1();
        Avion avion2 = new Avion();
        assertThat(avion1).isNotEqualTo(avion2);

        avion2.setId(avion1.getId());
        assertThat(avion1).isEqualTo(avion2);

        avion2 = getAvionSample2();
        assertThat(avion1).isNotEqualTo(avion2);
    }

    @Test
    void vuelosTest() {
        Avion avion = getAvionRandomSampleGenerator();
        Vuelo vueloBack = getVueloRandomSampleGenerator();

        avion.addVuelos(vueloBack);
        assertThat(avion.getVueloses()).containsOnly(vueloBack);
        assertThat(vueloBack.getAvion()).isEqualTo(avion);

        avion.removeVuelos(vueloBack);
        assertThat(avion.getVueloses()).doesNotContain(vueloBack);
        assertThat(vueloBack.getAvion()).isNull();

        avion.vueloses(new HashSet<>(Set.of(vueloBack)));
        assertThat(avion.getVueloses()).containsOnly(vueloBack);
        assertThat(vueloBack.getAvion()).isEqualTo(avion);

        avion.setVueloses(new HashSet<>());
        assertThat(avion.getVueloses()).doesNotContain(vueloBack);
        assertThat(vueloBack.getAvion()).isNull();
    }
}
