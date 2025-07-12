import { IInstrumento } from 'app/pages/instrumento/instrumento.model';
import { IQuestionario } from 'app/pages/questionario/questionario.model';
import { IOpcaoQuestaoQuestionario } from 'app/pages/opcao-questao-questionario/opcao-questao-questionario.model';

export interface IQuestaoQuestionario {
  id?: number;
  pergunta?: string | null;
  pontuacao?: string | null;
  instrumento?: IInstrumento | null;
  questionario?: IQuestionario | null;
  opcaoQuestaoQuestionarios?: IOpcaoQuestaoQuestionario[] | null;
}

export class QuestaoQuestionario implements IQuestaoQuestionario {
  constructor(
    public id?: number,
    public pergunta?: string | null,
    public pontuacao?: string | null,
    public instrumento?: IInstrumento | null,
    public questionario?: IQuestionario | null,
    public opcaoQuestaoQuestionarios?: IOpcaoQuestaoQuestionario[] | null
  ) {}
}

export function getQuestaoQuestionarioIdentifier(questaoQuestionario: IQuestaoQuestionario): number | undefined {
  return questaoQuestionario.id;
}
