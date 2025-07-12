import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISubTema } from '../sub-tema.model';

export type EntityResponseType = HttpResponse<ISubTema>;
export type EntityArrayResponseType = HttpResponse<ISubTema[]>;

@Injectable({ providedIn: 'root' })
export class SubTemaService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/sub-temas');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISubTema>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  findByCategoriaId(id: number): Observable<EntityArrayResponseType> {
    return this.http.get<ISubTema[]>(`${this.resourceUrl}/categoria/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISubTema[]>(this.resourceUrl, { params: options, observe: 'response' });
  }
}
