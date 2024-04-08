import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import FormTypes from './form-types';
import FormTypesDetail from './form-types-detail';
import FormTypesUpdate from './form-types-update';
import FormTypesDeleteDialog from './form-types-delete-dialog';

const FormTypesRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<FormTypes />} />
    <Route path="new" element={<FormTypesUpdate />} />
    <Route path=":id">
      <Route index element={<FormTypesDetail />} />
      <Route path="edit" element={<FormTypesUpdate />} />
      <Route path="delete" element={<FormTypesDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default FormTypesRoutes;
