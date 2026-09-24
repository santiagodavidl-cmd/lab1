import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { Translate } from 'react-jhipster';
import { Link, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './pasajero.reducer';

export const PasajeroDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const pasajeroEntity = useAppSelector(state => state.pasajero.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="pasajeroDetailsHeading">
          <Translate contentKey="proyecto2026App.pasajero.detail.title">Pasajero</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{pasajeroEntity.id}</dd>
          <dt>
            <span id="nombre">
              <Translate contentKey="proyecto2026App.pasajero.nombre">Nombre</Translate>
            </span>
          </dt>
          <dd>{pasajeroEntity.nombre}</dd>
          <dt>
            <span id="apellido">
              <Translate contentKey="proyecto2026App.pasajero.apellido">Apellido</Translate>
            </span>
          </dt>
          <dd>{pasajeroEntity.apellido}</dd>
          <dt>
            <span id="documento">
              <Translate contentKey="proyecto2026App.pasajero.documento">Documento</Translate>
            </span>
          </dt>
          <dd>{pasajeroEntity.documento}</dd>
          <dt>
            <span id="email">
              <Translate contentKey="proyecto2026App.pasajero.email">Email</Translate>
            </span>
          </dt>
          <dd>{pasajeroEntity.email}</dd>
        </dl>
        <Button as={Link as any} to="/pasajero" replace variant="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button as={Link as any} to={`/pasajero/${pasajeroEntity.id}/edit`} replace variant="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PasajeroDetail;
