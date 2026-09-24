import React from 'react';
import { Route } from 'react-router'; // eslint-disable-line

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Avion from './avion';
import Pasajero from './pasajero';
import Reserva from './reserva';
import Vuelo from './vuelo';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="/avion/*" element={<Avion />} />
        <Route path="/vuelo/*" element={<Vuelo />} />
        <Route path="/pasajero/*" element={<Pasajero />} />
        <Route path="/reserva/*" element={<Reserva />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
