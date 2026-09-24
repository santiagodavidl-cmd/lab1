package com.udea.proyecto.service.impl;

import com.udea.proyecto.domain.Avion;
import com.udea.proyecto.repository.AvionRepository;
import com.udea.proyecto.service.AvionService;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.udea.proyecto.domain.Avion}.
 */
@Service
@Transactional
public class AvionServiceImpl implements AvionService {

    private static final Logger LOG = LoggerFactory.getLogger(AvionServiceImpl.class);

    private final AvionRepository avionRepository;

    public AvionServiceImpl(AvionRepository avionRepository) {
        this.avionRepository = avionRepository;
    }

    @Override
    public Avion save(Avion avion) {
        LOG.debug("Request to save Avion : {}", avion);
        return avionRepository.save(avion);
    }

    @Override
    public Avion update(Avion avion) {
        LOG.debug("Request to update Avion : {}", avion);
        return avionRepository.save(avion);
    }

    @Override
    public Optional<Avion> partialUpdate(Avion avion) {
        LOG.debug("Request to partially update Avion : {}", avion);

        return avionRepository
            .findById(avion.getId())
            .map(existingAvion -> {
                updateIfPresent(existingAvion::setModelo, avion.getModelo());
                updateIfPresent(existingAvion::setCapacidad, avion.getCapacidad());
                updateIfPresent(existingAvion::setMatricula, avion.getMatricula());

                return existingAvion;
            })
            .map(avionRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Avion> findAll() {
        LOG.debug("Request to get all Avions");
        return avionRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Avion> findOne(Long id) {
        LOG.debug("Request to get Avion : {}", id);
        return avionRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Avion : {}", id);
        avionRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
