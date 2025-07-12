import { IFoto } from 'app/pages/foto/foto.model';
import { IReferenciaHistoria } from 'app/pages/referencia-historia/referencia-historia.model';

export interface IHistoria {
  id?: number;
  titulo?: string | null;
  autor?: string | null;
  conteudoPath?: string | null;
  fotos?: IFoto[] | null;
  referenciaHistorias?: IReferenciaHistoria[] | null;
}

export class Historia implements IHistoria {
  constructor(
    public id?: number,
    public titulo?: string | null,
    public autor?: string | null,
    public conteudoPath?: string | null,
    public fotos?: IFoto[] | null,
    public referenciaHistorias?: IReferenciaHistoria[] | null
  ) {}
}

export function getHistoriaIdentifier(historia: IHistoria): number | undefined {
  return historia.id;
}
