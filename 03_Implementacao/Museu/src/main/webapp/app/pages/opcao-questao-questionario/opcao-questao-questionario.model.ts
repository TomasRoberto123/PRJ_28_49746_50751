import { IQuestaoQuestionario } from 'app/pages/questao-questionario/questao-questionario.model';

export interface IOpcaoQuestaoQuestionario {
  id?: number;
  opcaoTexto?: string | null;
  correcta?: boolean | null;
  questaoQuestionario?: IQuestaoQuestionario | null;
}

export class OpcaoQuestaoQuestionario implements IOpcaoQuestaoQuestionario {
  constructor(
    public id?: number,
    public opcaoTexto?: string | null,
    public correcta?: boolean | null,
    public questaoQuestionario?: IQuestaoQuestionario | null
  ) {
    this.correcta = this.correcta ?? false;
  }
}

export function getOpcaoQuestaoQuestionarioIdentifier(opcaoQuestaoQuestionario: IOpcaoQuestaoQuestionario): number | undefined {
  return opcaoQuestaoQuestionario.id;
}
