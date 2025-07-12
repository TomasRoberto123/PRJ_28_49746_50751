import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IInstrumento } from '../instrumento.model';

export type EntityResponseType = HttpResponse<IInstrumento>;
export type EntityArrayResponseType = HttpResponse<IInstrumento[]>;

@Injectable({ providedIn: 'root' })
export class InstrumentoService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/instrumentos');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IInstrumento>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  findAllBySubtema(id: number): Observable<EntityArrayResponseType> {
    return this.http.get<IInstrumento[]>(`${this.resourceUrl}/subtema/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IInstrumento[]>(this.resourceUrl, { params: options, observe: 'response' });
  }
}
