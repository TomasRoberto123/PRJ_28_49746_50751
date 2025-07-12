import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IQuestionario } from '../questionario.model';

export type EntityResponseType = HttpResponse<IQuestionario>;
export type EntityArrayResponseType = HttpResponse<IQuestionario[]>;

@Injectable({ providedIn: 'root' })
export class QuestionarioService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/questionarios');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IQuestionario>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IQuestionario[]>(this.resourceUrl, { params: options, observe: 'response' });
  }
}
