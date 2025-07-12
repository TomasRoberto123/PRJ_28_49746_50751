import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IReferenciaHistoria, getReferenciaHistoriaIdentifier } from '../referencia-historia.model';

export type EntityResponseType = HttpResponse<IReferenciaHistoria>;
export type EntityArrayResponseType = HttpResponse<IReferenciaHistoria[]>;

@Injectable({ providedIn: 'root' })
export class ReferenciaHistoriaService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/referencia-historias');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IReferenciaHistoria>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IReferenciaHistoria[]>(this.resourceUrl, { params: options, observe: 'response' });
  }
}
