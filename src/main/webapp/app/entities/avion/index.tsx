import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Avion from './avion';
import AvionDeleteDialog from './avion-delete-dialog';
import AvionDetail from './avion-detail';
import AvionUpdate from './avion-update';

const AvionRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Avion />} />
    <Route path="new" element={<AvionUpdate />} />
    <Route path=":id">
      <Route index element={<AvionDetail />} />
      <Route path="edit" element={<AvionUpdate />} />
      <Route path="delete" element={<AvionDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default AvionRoutes;
