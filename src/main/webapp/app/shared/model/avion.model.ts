export interface IAvion {
  id?: number;
  modelo?: string;
  capacidad?: number;
  matricula?: string;
}

export const defaultValue: Readonly<IAvion> = {};
