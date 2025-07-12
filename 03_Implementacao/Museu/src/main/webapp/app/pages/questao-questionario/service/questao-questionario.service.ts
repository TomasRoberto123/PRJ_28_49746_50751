import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IQuestaoQuestionario } from '../questao-questionario.model';

export type EntityResponseType = HttpResponse<IQuestaoQuestionario>;
export type EntityArrayResponseType = HttpResponse<IQuestaoQuestionario[]>;

@Injectable({ providedIn: 'root' })
export class QuestaoQuestionarioService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/questao-questionarios');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IQuestaoQuestionario>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IQuestaoQuestionario[]>(this.resourceUrl, { params: options, observe: 'response' });
  }
}
