import { ISubTema } from 'app/pages/sub-tema/sub-tema.model';
import { IQuestionario } from 'app/pages/questionario/questionario.model';

export interface ICategoria {
  id?: number;
  nome?: string | null;
  descricao?: string | null;
  imagemPath?: string | null;
  subTemas?: ISubTema[] | null;
  questionario?: IQuestionario | null;
}

export class Categoria implements ICategoria {
  constructor(
    public id?: number,
    public nome?: string | null,
    public descricao?: string | null,
    public imagemPath?: string | null,
    public subTemas?: ISubTema[] | null,
    public questionario?: IQuestionario | null
  ) {}
}

export function getCategoriaIdentifier(categoria: ICategoria): number | undefined {
  return categoria.id;
}
