import dayjs from 'dayjs';

import { IAvion } from 'app/shared/model/avion.model';

export interface IVuelo {
  id?: number;
  numeroVuelo?: string;
  origen?: string;
  destino?: string;
  fechaSalida?: dayjs.Dayjs;
  precio?: number;
  avion?: IAvion | null;
}

export const defaultValue: Readonly<IVuelo> = {};
