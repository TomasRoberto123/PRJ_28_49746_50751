import { IInstrumento } from 'app/pages/instrumento/instrumento.model';
import { IHistoria } from 'app/pages/historia/historia.model';

export interface IFoto {
  id?: number;
  nome?: string | null;
  imagemPath?: string | null;
  isFigura?: boolean | null;
  legenda?: string | null;
  instrumento?: IInstrumento | null;
  historia?: IHistoria | null;
}

export class Foto implements IFoto {
  constructor(
    public id?: number,
    public nome?: string | null,
    public imagemPath?: string | null,
    public isFigura?: boolean | null,
    public legenda?: string | null,
    public instrumento?: IInstrumento | null,
    public historia?: IHistoria | null
  ) {
    this.isFigura = this.isFigura ?? false;
  }
}

export function getFotoIdentifier(foto: IFoto): number | undefined {
  return foto.id;
}
