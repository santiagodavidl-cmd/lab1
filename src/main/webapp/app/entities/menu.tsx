import React from 'react';
import { Translate } from 'react-jhipster'; // eslint-disable-line

import MenuItem from 'app/shared/layout/menus/menu-item'; // eslint-disable-line

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/avion">
        <Translate contentKey="global.menu.entities.avion" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/vuelo">
        <Translate contentKey="global.menu.entities.vuelo" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/pasajero">
        <Translate contentKey="global.menu.entities.pasajero" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/reserva">
        <Translate contentKey="global.menu.entities.reserva" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
