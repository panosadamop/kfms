import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import FormTypes from './form-types';
import FormTypesDetail from './form-types-detail';
import FormTypesUpdate from './form-types-update';
import FormTypesDeleteDialog from './form-types-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={FormTypesUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={FormTypesUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={FormTypesDetail} />
      <ErrorBoundaryRoute path={match.url} component={FormTypes} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={FormTypesDeleteDialog} />
  </>
);

export default Routes;
