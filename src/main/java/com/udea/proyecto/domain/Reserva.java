package com.udea.proyecto.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Reserva.
 */
@Entity
@Table(name = "reserva")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Reserva implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "codigo_reserva", nullable = false, unique = true)
    private String codigoReserva;

    @NotNull
    @Column(name = "fecha_reserva", nullable = false)
    private Instant fechaReserva;

    @Column(name = "asiento")
    private String asiento;

    @ManyToOne(fetch = FetchType.LAZY)
    private Pasajero pasajero;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "avion" }, allowSetters = true)
    private Vuelo vuelo;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Reserva id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigoReserva() {
        return this.codigoReserva;
    }

    public Reserva codigoReserva(String codigoReserva) {
        this.setCodigoReserva(codigoReserva);
        return this;
    }

    public void setCodigoReserva(String codigoReserva) {
        this.codigoReserva = codigoReserva;
    }

    public Instant getFechaReserva() {
        return this.fechaReserva;
    }

    public Reserva fechaReserva(Instant fechaReserva) {
        this.setFechaReserva(fechaReserva);
        return this;
    }

    public void setFechaReserva(Instant fechaReserva) {
        this.fechaReserva = fechaReserva;
    }

    public String getAsiento() {
        return this.asiento;
    }

    public Reserva asiento(String asiento) {
        this.setAsiento(asiento);
        return this;
    }

    public void setAsiento(String asiento) {
        this.asiento = asiento;
    }

    public Pasajero getPasajero() {
        return this.pasajero;
    }

    public void setPasajero(Pasajero pasajero) {
        this.pasajero = pasajero;
    }

    public Reserva pasajero(Pasajero pasajero) {
        this.setPasajero(pasajero);
        return this;
    }

    public Vuelo getVuelo() {
        return this.vuelo;
    }

    public void setVuelo(Vuelo vuelo) {
        this.vuelo = vuelo;
    }

    public Reserva vuelo(Vuelo vuelo) {
        this.setVuelo(vuelo);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Reserva)) {
            return false;
        }
        return getId() != null && getId().equals(((Reserva) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Reserva{" +
            "id=" + getId() +
            ", codigoReserva='" + getCodigoReserva() + "'" +
            ", fechaReserva='" + getFechaReserva() + "'" +
            ", asiento='" + getAsiento() + "'" +
            "}";
    }
}
