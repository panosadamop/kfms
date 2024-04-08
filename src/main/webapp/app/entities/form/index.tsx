import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Form from './form';
import FormDetail from './form-detail';
import FormUpdate from './form-update';
import FormDeleteDialog from './form-delete-dialog';

const FormRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Form />} />
    <Route path="new" element={<FormUpdate />} />
    <Route path=":id">
      <Route index element={<FormDetail />} />
      <Route path="edit" element={<FormUpdate />} />
      <Route path="delete" element={<FormDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default FormRoutes;
