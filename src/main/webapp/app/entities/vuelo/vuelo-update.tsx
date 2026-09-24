import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { Link, useNavigate, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getAvions } from 'app/entities/avion/avion.reducer';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';

import { createEntity, getEntity, reset, updateEntity } from './vuelo.reducer';

export const VueloUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const avions = useAppSelector(state => state.avion.entities);
  const vueloEntity = useAppSelector(state => state.vuelo.entity);
  const loading = useAppSelector(state => state.vuelo.loading);
  const updating = useAppSelector(state => state.vuelo.updating);
  const updateSuccess = useAppSelector(state => state.vuelo.updateSuccess);

  const handleClose = () => {
    navigate(`/vuelo${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getAvions({}));
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
    values.fechaSalida = convertDateTimeToServer(values.fechaSalida);
    if (values.precio !== undefined && typeof values.precio !== 'number') {
      values.precio = Number(values.precio);
    }

    const entity = {
      ...vueloEntity,
      ...values,
      avion: avions.find(it => it.id.toString() === values.avion?.toString()),
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
          fechaSalida: displayDefaultDateTime(),
        }
      : {
          ...vueloEntity,
          fechaSalida: convertDateTimeFromServer(vueloEntity.fechaSalida),
          avion: vueloEntity?.avion?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="aerolineaVirtualApp.vuelo.home.createOrEditLabel" data-cy="VueloCreateUpdateHeading">
            <Translate contentKey="aerolineaVirtualApp.vuelo.home.createOrEditLabel">Create or edit a Vuelo</Translate>
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
                  id="vuelo-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              )}
              <ValidatedField
                label={translate('aerolineaVirtualApp.vuelo.numeroVuelo')}
                id="vuelo-numeroVuelo"
                name="numeroVuelo"
                data-cy="numeroVuelo"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('aerolineaVirtualApp.vuelo.origen')}
                id="vuelo-origen"
                name="origen"
                data-cy="origen"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('aerolineaVirtualApp.vuelo.destino')}
                id="vuelo-destino"
                name="destino"
                data-cy="destino"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('aerolineaVirtualApp.vuelo.fechaSalida')}
                id="vuelo-fechaSalida"
                name="fechaSalida"
                data-cy="fechaSalida"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('aerolineaVirtualApp.vuelo.precio')}
                id="vuelo-precio"
                name="precio"
                data-cy="precio"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                id="vuelo-avion"
                name="avion"
                data-cy="avion"
                label={translate('aerolineaVirtualApp.vuelo.avion')}
                type="select"
              >
                <option value="" key="0" />
                {avions
                  ? avions.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.matricula}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button as={Link as any} id="cancel-save" data-cy="entityCreateCancelButton" to="/vuelo" replace variant="info">
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

export default VueloUpdate;
