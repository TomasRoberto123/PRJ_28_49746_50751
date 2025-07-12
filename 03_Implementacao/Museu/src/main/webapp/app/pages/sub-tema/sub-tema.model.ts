import { IInstrumento } from 'app/pages/instrumento/instrumento.model';
import { ICategoria } from 'app/pages/categoria/categoria.model';

export interface ISubTema {
  id?: number;
  nome?: string | null;
  descricao?: string | null;
  instrumentos?: IInstrumento[] | null;
  categoria?: ICategoria | null;
}

export class SubTema implements ISubTema {
  constructor(
    public id?: number,
    public nome?: string | null,
    public descricao?: string | null,
    public instrumentos?: IInstrumento[] | null,
    public categoria?: ICategoria | null
  ) {}
}

export function getSubTemaIdentifier(subTema: ISubTema): number | undefined {
  return subTema.id;
}
