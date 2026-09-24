package com.udea.proyecto.service.impl;

import com.udea.proyecto.domain.Pasajero;
import com.udea.proyecto.repository.PasajeroRepository;
import com.udea.proyecto.service.PasajeroService;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.udea.proyecto.domain.Pasajero}.
 */
@Service
@Transactional
public class PasajeroServiceImpl implements PasajeroService {

    private static final Logger LOG = LoggerFactory.getLogger(PasajeroServiceImpl.class);

    private final PasajeroRepository pasajeroRepository;

    public PasajeroServiceImpl(PasajeroRepository pasajeroRepository) {
        this.pasajeroRepository = pasajeroRepository;
    }

    @Override
    public Pasajero save(Pasajero pasajero) {
        LOG.debug("Request to save Pasajero : {}", pasajero);
        return pasajeroRepository.save(pasajero);
    }

    @Override
    public Pasajero update(Pasajero pasajero) {
        LOG.debug("Request to update Pasajero : {}", pasajero);
        return pasajeroRepository.save(pasajero);
    }

    @Override
    public Optional<Pasajero> partialUpdate(Pasajero pasajero) {
        LOG.debug("Request to partially update Pasajero : {}", pasajero);

        return pasajeroRepository
            .findById(pasajero.getId())
            .map(existingPasajero -> {
                updateIfPresent(existingPasajero::setNombre, pasajero.getNombre());
                updateIfPresent(existingPasajero::setApellido, pasajero.getApellido());
                updateIfPresent(existingPasajero::setDocumento, pasajero.getDocumento());
                updateIfPresent(existingPasajero::setEmail, pasajero.getEmail());

                return existingPasajero;
            })
            .map(pasajeroRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Pasajero> findAll(Pageable pageable) {
        LOG.debug("Request to get all Pasajeros");
        return pasajeroRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Pasajero> findOne(Long id) {
        LOG.debug("Request to get Pasajero : {}", id);
        return pasajeroRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Pasajero : {}", id);
        pasajeroRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
