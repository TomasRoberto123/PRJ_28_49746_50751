import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOpcaoQuestaoQuestionario } from '../opcao-questao-questionario.model';

export type EntityResponseType = HttpResponse<IOpcaoQuestaoQuestionario>;
export type EntityArrayResponseType = HttpResponse<IOpcaoQuestaoQuestionario[]>;

@Injectable({ providedIn: 'root' })
export class OpcaoQuestaoQuestionarioService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/opcao-questao-questionarios');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IOpcaoQuestaoQuestionario>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOpcaoQuestaoQuestionario[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  findAllByQuestaoId(id: number): Observable<EntityArrayResponseType> {
    return this.http.get<IOpcaoQuestaoQuestionario[]>(`${this.resourceUrl}/questao/${id}`, { observe: 'response' });
  }
}
