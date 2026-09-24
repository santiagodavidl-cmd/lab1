import React, { useEffect, useState } from 'react';
import { Button, Table } from 'react-bootstrap';
import { Translate, getSortState } from 'react-jhipster';
import { Link, useLocation, useNavigate } from 'react-router';

import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { ASC, DESC } from 'app/shared/util/pagination.constants';

import { getEntities } from './avion.reducer';

export const Avion = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const avionList = useAppSelector(state => state.avion.entities);
  const loading = useAppSelector(state => state.avion.loading);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        sort: `${sortState.sort},${sortState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?sort=${sortState.sort},${sortState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [sortState.order, sortState.sort]);

  const sort = p => () => {
    setSortState({
      ...sortState,
      order: sortState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = sortState.sort;
    const order = sortState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div>
      <h2 id="avion-heading" data-cy="AvionHeading">
        <Translate contentKey="proyecto2026App.avion.home.title">Avions</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" variant="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="proyecto2026App.avion.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/avion/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="proyecto2026App.avion.home.createLabel">Create new Avion</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {avionList?.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="proyecto2026App.avion.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('modelo')}>
                  <Translate contentKey="proyecto2026App.avion.modelo">Modelo</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('modelo')} />
                </th>
                <th className="hand" onClick={sort('capacidad')}>
                  <Translate contentKey="proyecto2026App.avion.capacidad">Capacidad</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('capacidad')} />
                </th>
                <th className="hand" onClick={sort('matricula')}>
                  <Translate contentKey="proyecto2026App.avion.matricula">Matricula</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('matricula')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {avionList.map(avion => (
                <tr key={`entity-${avion.id}`} data-cy="entityTable">
                  <td>
                    <Button as={Link as any} to={`/avion/${avion.id}`} variant="link" size="sm">
                      {avion.id}
                    </Button>
                  </td>
                  <td>{avion.modelo}</td>
                  <td>{avion.capacidad}</td>
                  <td>{avion.matricula}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button as={Link as any} to={`/avion/${avion.id}`} variant="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button as={Link as any} to={`/avion/${avion.id}/edit`} variant="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/avion/${avion.id}/delete`)}
                        variant="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="proyecto2026App.avion.home.notFound">No Avions found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Avion;
