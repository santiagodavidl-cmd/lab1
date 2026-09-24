import avion from 'app/entities/avion/avion.reducer';
import pasajero from 'app/entities/pasajero/pasajero.reducer';
import reserva from 'app/entities/reserva/reserva.reducer';
import vuelo from 'app/entities/vuelo/vuelo.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  avion,
  vuelo,
  pasajero,
  reserva,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
