import { IUser } from 'app/pages/user/user.model';
import { IQuestionario } from 'app/pages/questionario/questionario.model';

export interface IRespostaUserQuestionario {
  id?: number;
  pontuacao?: number | null;
  user?: IUser | null;
  questionario?: IQuestionario | null;
}

export class RespostaUserQuestionario implements IRespostaUserQuestionario {
  constructor(
    public id?: number,
    public pontuacao?: number | null,
    public user?: IUser | null,
    public questionario?: IQuestionario | null
  ) {}
}

export function getRespostaUserQuestionarioIdentifier(respostaUserQuestionario: IRespostaUserQuestionario): number | undefined {
  return respostaUserQuestionario.id;
}
