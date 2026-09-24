export interface IPasajero {
  id?: number;
  nombre?: string;
  apellido?: string;
  documento?: string;
  email?: string;
}

export const defaultValue: Readonly<IPasajero> = {};
