import { IFoto } from 'app/pages/foto/foto.model';
import { IQuestaoQuestionario } from 'app/pages/questao-questionario/questao-questionario.model';
import { ISubTema } from 'app/pages/sub-tema/sub-tema.model';

export interface IInstrumento {
  id?: number;
  nome?: string | null;
  codigo?: string | null;
  localizacao?: string | null;
  fabricante?: string | null;
  dimensoes?: string | null;
  descricaoPath?: string | null;
  fotos?: IFoto[] | null;
  questaoQuestionario?: IQuestaoQuestionario | null;
  subTema?: ISubTema | null;
}

export class Instrumento implements IInstrumento {
  constructor(
    public id?: number,
    public nome?: string | null,
    public codigo?: string | null,
    public localizacao?: string | null,
    public fabricante?: string | null,
    public dimensoes?: string | null,
    public descricaoPath?: string | null,
    public fotos?: IFoto[] | null,
    public questaoQuestionario?: IQuestaoQuestionario | null,
    public subTema?: ISubTema | null
  ) {}
}

export function getInstrumentoIdentifier(instrumento: IInstrumento): number | undefined {
  return instrumento.id;
}
