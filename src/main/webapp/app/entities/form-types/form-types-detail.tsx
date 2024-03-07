import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './form-types.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const FormTypesDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const formTypesEntity = useAppSelector(state => state.formTypes.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="formTypesDetailsHeading">
          <Translate contentKey="kfmsApp.formTypes.detail.title">FormTypes</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{formTypesEntity.id}</dd>
          <dt>
            <span id="type">
              <Translate contentKey="kfmsApp.formTypes.type">Type</Translate>
            </span>
          </dt>
          <dd>{formTypesEntity.type}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="kfmsApp.formTypes.description">Description</Translate>
            </span>
          </dt>
          <dd>{formTypesEntity.description}</dd>
        </dl>
        <Button tag={Link} to="/form-types" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/form-types/${formTypesEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default FormTypesDetail;
