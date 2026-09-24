package com.udea.proyecto.service.impl;

import com.udea.proyecto.domain.Vuelo;
import com.udea.proyecto.repository.VueloRepository;
import com.udea.proyecto.service.VueloService;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.udea.proyecto.domain.Vuelo}.
 */
@Service
@Transactional
public class VueloServiceImpl implements VueloService {

    private static final Logger LOG = LoggerFactory.getLogger(VueloServiceImpl.class);

    private final VueloRepository vueloRepository;

    public VueloServiceImpl(VueloRepository vueloRepository) {
        this.vueloRepository = vueloRepository;
    }

    @Override
    public Vuelo save(Vuelo vuelo) {
        LOG.debug("Request to save Vuelo : {}", vuelo);
        return vueloRepository.save(vuelo);
    }

    @Override
    public Vuelo update(Vuelo vuelo) {
        LOG.debug("Request to update Vuelo : {}", vuelo);
        return vueloRepository.save(vuelo);
    }

    @Override
    public Optional<Vuelo> partialUpdate(Vuelo vuelo) {
        LOG.debug("Request to partially update Vuelo : {}", vuelo);

        return vueloRepository
            .findById(vuelo.getId())
            .map(existingVuelo -> {
                updateIfPresent(existingVuelo::setNumeroVuelo, vuelo.getNumeroVuelo());
                updateIfPresent(existingVuelo::setOrigen, vuelo.getOrigen());
                updateIfPresent(existingVuelo::setDestino, vuelo.getDestino());
                updateIfPresent(existingVuelo::setFechaSalida, vuelo.getFechaSalida());
                updateIfPresent(existingVuelo::setPrecio, vuelo.getPrecio());

                return existingVuelo;
            })
            .map(vueloRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Vuelo> findAll(Pageable pageable) {
        LOG.debug("Request to get all Vuelos");
        return vueloRepository.findAll(pageable);
    }

    public Page<Vuelo> findAllWithEagerRelationships(Pageable pageable) {
        return vueloRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Vuelo> findOne(Long id) {
        LOG.debug("Request to get Vuelo : {}", id);
        return vueloRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Vuelo : {}", id);
        vueloRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
