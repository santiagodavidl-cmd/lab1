import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { Link, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './reserva.reducer';

export const ReservaDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const reservaEntity = useAppSelector(state => state.reserva.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="reservaDetailsHeading">
          <Translate contentKey="aerolineaVirtualApp.reserva.detail.title">Reserva</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{reservaEntity.id}</dd>
          <dt>
            <span id="codigoReserva">
              <Translate contentKey="aerolineaVirtualApp.reserva.codigoReserva">Codigo Reserva</Translate>
            </span>
          </dt>
          <dd>{reservaEntity.codigoReserva}</dd>
          <dt>
            <span id="fechaReserva">
              <Translate contentKey="aerolineaVirtualApp.reserva.fechaReserva">Fecha Reserva</Translate>
            </span>
          </dt>
          <dd>
            {reservaEntity.fechaReserva ? <TextFormat value={reservaEntity.fechaReserva} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="asiento">
              <Translate contentKey="aerolineaVirtualApp.reserva.asiento">Asiento</Translate>
            </span>
          </dt>
          <dd>{reservaEntity.asiento}</dd>
          <dt>
            <Translate contentKey="aerolineaVirtualApp.reserva.pasajero">Pasajero</Translate>
          </dt>
          <dd>{reservaEntity.pasajero ? reservaEntity.pasajero.documento : ''}</dd>
          <dt>
            <Translate contentKey="aerolineaVirtualApp.reserva.vuelo">Vuelo</Translate>
          </dt>
          <dd>{reservaEntity.vuelo ? reservaEntity.vuelo.numeroVuelo : ''}</dd>
        </dl>
        <Button as={Link as any} to="/reserva" replace variant="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button as={Link as any} to={`/reserva/${reservaEntity.id}/edit`} replace variant="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ReservaDetail;
