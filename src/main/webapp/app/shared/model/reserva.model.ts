import dayjs from 'dayjs';

import { IPasajero } from 'app/shared/model/pasajero.model';
import { IVuelo } from 'app/shared/model/vuelo.model';

export interface IReserva {
  id?: number;
  codigoReserva?: string;
  fechaReserva?: dayjs.Dayjs;
  asiento?: string | null;
  pasajero?: IPasajero | null;
  vuelo?: IVuelo | null;
}

export const defaultValue: Readonly<IReserva> = {};
