import { IHistoria } from 'app/pages/historia/historia.model';

export interface IReferenciaHistoria {
  id?: number;
  ancora?: string | null;
  conteudo?: string | null;
  historia?: IHistoria | null;
}

export class ReferenciaHistoria implements IReferenciaHistoria {
  constructor(public id?: number, public ancora?: string | null, public conteudo?: string | null, public historia?: IHistoria | null) {}
}

export function getReferenciaHistoriaIdentifier(referenciaHistoria: IReferenciaHistoria): number | undefined {
  return referenciaHistoria.id;
}
