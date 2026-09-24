package com.udea.proyecto.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Avion.
 */
@Entity
@Table(name = "avion")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Avion implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "modelo", nullable = false)
    private String modelo;

    @NotNull
    @Min(value = 10)
    @Max(value = 500)
    @Column(name = "capacidad", nullable = false)
    private Integer capacidad;

    @NotNull
    @Column(name = "matricula", nullable = false, unique = true)
    private String matricula;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "avion")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "avion" }, allowSetters = true)
    private Set<Vuelo> vueloses = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Avion id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModelo() {
        return this.modelo;
    }

    public Avion modelo(String modelo) {
        this.setModelo(modelo);
        return this;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Integer getCapacidad() {
        return this.capacidad;
    }

    public Avion capacidad(Integer capacidad) {
        this.setCapacidad(capacidad);
        return this;
    }

    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
    }

    public String getMatricula() {
        return this.matricula;
    }

    public Avion matricula(String matricula) {
        this.setMatricula(matricula);
        return this;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public Set<Vuelo> getVueloses() {
        return this.vueloses;
    }

    public void setVueloses(Set<Vuelo> vuelos) {
        if (this.vueloses != null) {
            this.vueloses.forEach(i -> i.setAvion(null));
        }
        if (vuelos != null) {
            vuelos.forEach(i -> i.setAvion(this));
        }
        this.vueloses = vuelos;
    }

    public Avion vueloses(Set<Vuelo> vuelos) {
        this.setVueloses(vuelos);
        return this;
    }

    public Avion addVuelos(Vuelo vuelo) {
        this.vueloses.add(vuelo);
        vuelo.setAvion(this);
        return this;
    }

    public Avion removeVuelos(Vuelo vuelo) {
        this.vueloses.remove(vuelo);
        vuelo.setAvion(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Avion)) {
            return false;
        }
        return getId() != null && getId().equals(((Avion) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Avion{" +
            "id=" + getId() +
            ", modelo='" + getModelo() + "'" +
            ", capacidad=" + getCapacidad() +
            ", matricula='" + getMatricula() + "'" +
            "}";
    }
}
