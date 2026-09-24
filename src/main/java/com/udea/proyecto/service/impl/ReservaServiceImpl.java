package com.udea.proyecto.service.impl;

import com.udea.proyecto.domain.Reserva;
import com.udea.proyecto.repository.ReservaRepository;
import com.udea.proyecto.service.ReservaService;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.udea.proyecto.domain.Reserva}.
 */
@Service
@Transactional
public class ReservaServiceImpl implements ReservaService {

    private static final Logger LOG = LoggerFactory.getLogger(ReservaServiceImpl.class);

    private final ReservaRepository reservaRepository;

    public ReservaServiceImpl(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }

    @Override
    public Reserva save(Reserva reserva) {
        LOG.debug("Request to save Reserva : {}", reserva);
        return reservaRepository.save(reserva);
    }

    @Override
    public Reserva update(Reserva reserva) {
        LOG.debug("Request to update Reserva : {}", reserva);
        return reservaRepository.save(reserva);
    }

    @Override
    public Optional<Reserva> partialUpdate(Reserva reserva) {
        LOG.debug("Request to partially update Reserva : {}", reserva);

        return reservaRepository
            .findById(reserva.getId())
            .map(existingReserva -> {
                updateIfPresent(existingReserva::setCodigoReserva, reserva.getCodigoReserva());
                updateIfPresent(existingReserva::setFechaReserva, reserva.getFechaReserva());
                updateIfPresent(existingReserva::setAsiento, reserva.getAsiento());

                return existingReserva;
            })
            .map(reservaRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Reserva> findAll(Pageable pageable) {
        LOG.debug("Request to get all Reservas");
        return reservaRepository.findAll(pageable);
    }

    public Page<Reserva> findAllWithEagerRelationships(Pageable pageable) {
        return reservaRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Reserva> findOne(Long id) {
        LOG.debug("Request to get Reserva : {}", id);
        return reservaRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Reserva : {}", id);
        reservaRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
