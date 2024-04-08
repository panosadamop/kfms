import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './form.reducer';

export const FormDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const formEntity = useAppSelector(state => state.form.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="formDetailsHeading">
          <Translate contentKey="kfmsApp.form.detail.title">Form</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{formEntity.id}</dd>
          <dt>
            <span id="formName">
              <Translate contentKey="kfmsApp.form.formName">Form Name</Translate>
            </span>
          </dt>
          <dd>{formEntity.formName}</dd>
          <dt>
            <span id="formDescription">
              <Translate contentKey="kfmsApp.form.formDescription">Form Description</Translate>
            </span>
          </dt>
          <dd>{formEntity.formDescription}</dd>
          <dt>
            <span id="formData">
              <Translate contentKey="kfmsApp.form.formData">Form Data</Translate>
            </span>
          </dt>
          <dd>{formEntity.formData}</dd>
          <dt>
            <span id="version">
              <Translate contentKey="kfmsApp.form.version">Version</Translate>
            </span>
          </dt>
          <dd>{formEntity.version}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="kfmsApp.form.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{formEntity.isDeleted ? 'true' : 'false'}</dd>
          <dt>
            <span id="isLocked">
              <Translate contentKey="kfmsApp.form.isLocked">Is Locked</Translate>
            </span>
          </dt>
          <dd>{formEntity.isLocked ? 'true' : 'false'}</dd>
          <dt>
            <span id="createDate">
              <Translate contentKey="kfmsApp.form.createDate">Create Date</Translate>
            </span>
          </dt>
          <dd>{formEntity.createDate ? <TextFormat value={formEntity.createDate} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="updateDate">
              <Translate contentKey="kfmsApp.form.updateDate">Update Date</Translate>
            </span>
          </dt>
          <dd>{formEntity.updateDate ? <TextFormat value={formEntity.updateDate} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}</dd>
          <dt>
            <Translate contentKey="kfmsApp.form.formType">Form Type</Translate>
          </dt>
          <dd>{formEntity.formType ? formEntity.formType.type : ''}</dd>
        </dl>
        <Button tag={Link} to="/form" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/form/${formEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default FormDetail;
