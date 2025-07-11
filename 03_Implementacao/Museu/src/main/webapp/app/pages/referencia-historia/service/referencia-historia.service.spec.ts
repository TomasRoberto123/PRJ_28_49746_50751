import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IReferenciaHistoria, ReferenciaHistoria } from '../referencia-historia.model';

import { ReferenciaHistoriaService } from './referencia-historia.service';

describe('Service Tests', () => {
  describe('ReferenciaHistoria Service', () => {
    let service: ReferenciaHistoriaService;
    let httpMock: HttpTestingController;
    let elemDefault: IReferenciaHistoria;
    let expectedResult: IReferenciaHistoria | IReferenciaHistoria[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ReferenciaHistoriaService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        ancora: 'AAAAAAA',
        conteudo: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a ReferenciaHistoria', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new ReferenciaHistoria()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ReferenciaHistoria', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            ancora: 'BBBBBB',
            conteudo: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a ReferenciaHistoria', () => {
        const patchObject = Object.assign({}, new ReferenciaHistoria());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ReferenciaHistoria', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            ancora: 'BBBBBB',
            conteudo: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a ReferenciaHistoria', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addReferenciaHistoriaToCollectionIfMissing', () => {
        it('should add a ReferenciaHistoria to an empty array', () => {
          const referenciaHistoria: IReferenciaHistoria = { id: 123 };
          expectedResult = service.addReferenciaHistoriaToCollectionIfMissing([], referenciaHistoria);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(referenciaHistoria);
        });

        it('should not add a ReferenciaHistoria to an array that contains it', () => {
          const referenciaHistoria: IReferenciaHistoria = { id: 123 };
          const referenciaHistoriaCollection: IReferenciaHistoria[] = [
            {
              ...referenciaHistoria,
            },
            { id: 456 },
          ];
          expectedResult = service.addReferenciaHistoriaToCollectionIfMissing(referenciaHistoriaCollection, referenciaHistoria);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ReferenciaHistoria to an array that doesn't contain it", () => {
          const referenciaHistoria: IReferenciaHistoria = { id: 123 };
          const referenciaHistoriaCollection: IReferenciaHistoria[] = [{ id: 456 }];
          expectedResult = service.addReferenciaHistoriaToCollectionIfMissing(referenciaHistoriaCollection, referenciaHistoria);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(referenciaHistoria);
        });

        it('should add only unique ReferenciaHistoria to an array', () => {
          const referenciaHistoriaArray: IReferenciaHistoria[] = [{ id: 123 }, { id: 456 }, { id: 883 }];
          const referenciaHistoriaCollection: IReferenciaHistoria[] = [{ id: 123 }];
          expectedResult = service.addReferenciaHistoriaToCollectionIfMissing(referenciaHistoriaCollection, ...referenciaHistoriaArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const referenciaHistoria: IReferenciaHistoria = { id: 123 };
          const referenciaHistoria2: IReferenciaHistoria = { id: 456 };
          expectedResult = service.addReferenciaHistoriaToCollectionIfMissing([], referenciaHistoria, referenciaHistoria2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(referenciaHistoria);
          expect(expectedResult).toContain(referenciaHistoria2);
        });

        it('should accept null and undefined values', () => {
          const referenciaHistoria: IReferenciaHistoria = { id: 123 };
          expectedResult = service.addReferenciaHistoriaToCollectionIfMissing([], null, referenciaHistoria, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(referenciaHistoria);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
