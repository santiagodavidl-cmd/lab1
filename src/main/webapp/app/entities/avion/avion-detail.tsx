import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { Translate } from 'react-jhipster';
import { Link, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './avion.reducer';

export const AvionDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const avionEntity = useAppSelector(state => state.avion.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="avionDetailsHeading">
          <Translate contentKey="aerolineaVirtualApp.avion.detail.title">Avion</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{avionEntity.id}</dd>
          <dt>
            <span id="modelo">
              <Translate contentKey="aerolineaVirtualApp.avion.modelo">Modelo</Translate>
            </span>
          </dt>
          <dd>{avionEntity.modelo}</dd>
          <dt>
            <span id="capacidad">
              <Translate contentKey="aerolineaVirtualApp.avion.capacidad">Capacidad</Translate>
            </span>
          </dt>
          <dd>{avionEntity.capacidad}</dd>
          <dt>
            <span id="matricula">
              <Translate contentKey="aerolineaVirtualApp.avion.matricula">Matricula</Translate>
            </span>
          </dt>
          <dd>{avionEntity.matricula}</dd>
        </dl>
        <Button as={Link as any} to="/avion" replace variant="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button as={Link as any} to={`/avion/${avionEntity.id}/edit`} replace variant="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AvionDetail;
