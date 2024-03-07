import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Form from './form';
import FormDetail from './form-detail';
import FormUpdate from './form-update';
import FormDeleteDialog from './form-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={FormUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={FormUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={FormDetail} />
      <ErrorBoundaryRoute path={match.url} component={Form} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={FormDeleteDialog} />
  </>
);

export default Routes;
