import { ICategoria } from 'app/pages/categoria/categoria.model';
import { IRespostaUserQuestionario } from 'app/pages/resposta-user-questionario/resposta-user-questionario.model';
import { IQuestaoQuestionario } from 'app/pages/questao-questionario/questao-questionario.model';

export interface IQuestionario {
  id?: number;
  nome?: string | null;
  descricao?: string | null;
  categoria?: ICategoria | null;
  respostaUserQuestionarios?: IRespostaUserQuestionario[] | null;
  questaoQuestionarios?: IQuestaoQuestionario[] | null;
}

export class Questionario implements IQuestionario {
  constructor(
    public id?: number,
    public nome?: string | null,
    public descricao?: string | null,
    public categoria?: ICategoria | null,
    public respostaUserQuestionarios?: IRespostaUserQuestionario[] | null,
    public questaoQuestionarios?: IQuestaoQuestionario[] | null
  ) {}
}

export function getQuestionarioIdentifier(questionario: IQuestionario): number | undefined {
  return questionario.id;
}
