import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { Link, useNavigate, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getPasajeros } from 'app/entities/pasajero/pasajero.reducer';
import { getEntities as getVuelos } from 'app/entities/vuelo/vuelo.reducer';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';

import { createEntity, getEntity, reset, updateEntity } from './reserva.reducer';

export const ReservaUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const pasajeros = useAppSelector(state => state.pasajero.entities);
  const vuelos = useAppSelector(state => state.vuelo.entities);
  const reservaEntity = useAppSelector(state => state.reserva.entity);
  const loading = useAppSelector(state => state.reserva.loading);
  const updating = useAppSelector(state => state.reserva.updating);
  const updateSuccess = useAppSelector(state => state.reserva.updateSuccess);

  const handleClose = () => {
    navigate(`/reserva${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getPasajeros({}));
    dispatch(getVuelos({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    values.fechaReserva = convertDateTimeToServer(values.fechaReserva);

    const entity = {
      ...reservaEntity,
      ...values,
      pasajero: pasajeros.find(it => it.id.toString() === values.pasajero?.toString()),
      vuelo: vuelos.find(it => it.id.toString() === values.vuelo?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          fechaReserva: displayDefaultDateTime(),
        }
      : {
          ...reservaEntity,
          fechaReserva: convertDateTimeFromServer(reservaEntity.fechaReserva),
          pasajero: reservaEntity?.pasajero?.id,
          vuelo: reservaEntity?.vuelo?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="aerolineaVirtualApp.reserva.home.createOrEditLabel" data-cy="ReservaCreateUpdateHeading">
            <Translate contentKey="aerolineaVirtualApp.reserva.home.createOrEditLabel">Create or edit a Reserva</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew && (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="reserva-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              )}
              <ValidatedField
                label={translate('aerolineaVirtualApp.reserva.codigoReserva')}
                id="reserva-codigoReserva"
                name="codigoReserva"
                data-cy="codigoReserva"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('aerolineaVirtualApp.reserva.fechaReserva')}
                id="reserva-fechaReserva"
                name="fechaReserva"
                data-cy="fechaReserva"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('aerolineaVirtualApp.reserva.asiento')}
                id="reserva-asiento"
                name="asiento"
                data-cy="asiento"
                type="text"
              />
              <ValidatedField
                id="reserva-pasajero"
                name="pasajero"
                data-cy="pasajero"
                label={translate('aerolineaVirtualApp.reserva.pasajero')}
                type="select"
              >
                <option value="" key="0" />
                {pasajeros
                  ? pasajeros.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.documento}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="reserva-vuelo"
                name="vuelo"
                data-cy="vuelo"
                label={translate('aerolineaVirtualApp.reserva.vuelo')}
                type="select"
              >
                <option value="" key="0" />
                {vuelos
                  ? vuelos.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.numeroVuelo}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button as={Link as any} id="cancel-save" data-cy="entityCreateCancelButton" to="/reserva" replace variant="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button variant="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default ReservaUpdate;
