import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRespostaUserQuestionario } from '../resposta-user-questionario.model';

export type EntityResponseType = HttpResponse<IRespostaUserQuestionario>;
export type EntityArrayResponseType = HttpResponse<IRespostaUserQuestionario[]>;

@Injectable({ providedIn: 'root' })
export class RespostaUserQuestionarioService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/resposta-user-questionarios');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(respostaUserQuestionario: IRespostaUserQuestionario): Observable<EntityResponseType> {
    return this.http.post<IRespostaUserQuestionario>(this.resourceUrl, respostaUserQuestionario, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRespostaUserQuestionario>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  findAllByUserId(): Observable<EntityArrayResponseType> {
    return this.http.get<IRespostaUserQuestionario[]>(this.resourceUrl + '/user', { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRespostaUserQuestionario[]>(this.resourceUrl, { params: options, observe: 'response' });
  }
}
