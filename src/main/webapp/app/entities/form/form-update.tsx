import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IFormTypes } from 'app/shared/model/form-types.model';
import { getEntities as getFormTypes } from 'app/entities/form-types/form-types.reducer';
import { IForm } from 'app/shared/model/form.model';
import { getEntity, updateEntity, createEntity, reset } from './form.reducer';

export const FormUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const formTypes = useAppSelector(state => state.formTypes.entities);
  const formEntity = useAppSelector(state => state.form.entity);
  const loading = useAppSelector(state => state.form.loading);
  const updating = useAppSelector(state => state.form.updating);
  const updateSuccess = useAppSelector(state => state.form.updateSuccess);

  const handleClose = () => {
    navigate('/form' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getFormTypes({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }

    const entity = {
      ...formEntity,
      ...values,
      formType: formTypes.find(it => it.id.toString() === values.formType?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...formEntity,
          formType: formEntity?.formType?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="kfmsApp.form.home.createOrEditLabel" data-cy="FormCreateUpdateHeading">
            <Translate contentKey="kfmsApp.form.home.createOrEditLabel">Create or edit a Form</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="form-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('kfmsApp.form.formName')}
                id="form-formName"
                name="formName"
                data-cy="formName"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('kfmsApp.form.formDescription')}
                id="form-formDescription"
                name="formDescription"
                data-cy="formDescription"
                type="textarea"
              />
              <ValidatedField
                label={translate('kfmsApp.form.formData')}
                id="form-formData"
                name="formData"
                data-cy="formData"
                type="textarea"
              />
              <ValidatedField
                label={translate('kfmsApp.form.version')}
                id="form-version"
                name="version"
                data-cy="version"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('kfmsApp.form.isDeleted')}
                id="form-isDeleted"
                name="isDeleted"
                data-cy="isDeleted"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('kfmsApp.form.isLocked')}
                id="form-isLocked"
                name="isLocked"
                data-cy="isLocked"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('kfmsApp.form.createDate')}
                id="form-createDate"
                name="createDate"
                data-cy="createDate"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('kfmsApp.form.updateDate')}
                id="form-updateDate"
                name="updateDate"
                data-cy="updateDate"
                type="date"
              />
              <ValidatedField
                id="form-formType"
                name="formType"
                data-cy="formType"
                label={translate('kfmsApp.form.formType')}
                type="select"
              >
                <option value="" key="0" />
                {formTypes
                  ? formTypes.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.type}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/form" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
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

export default FormUpdate;
