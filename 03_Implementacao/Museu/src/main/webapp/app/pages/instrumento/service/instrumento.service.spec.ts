import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IInstrumento, Instrumento } from '../instrumento.model';

import { InstrumentoService } from './instrumento.service';

describe('Service Tests', () => {
  describe('Instrumento Service', () => {
    let service: InstrumentoService;
    let httpMock: HttpTestingController;
    let elemDefault: IInstrumento;
    let expectedResult: IInstrumento | IInstrumento[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(InstrumentoService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        nome: 'AAAAAAA',
        codigo: 'AAAAAAA',
        localizacao: 'AAAAAAA',
        fabricante: 'AAAAAAA',
        dimensoes: 'AAAAAAA',
        descricaoPath: 'AAAAAAA',
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

      it('should create a Instrumento', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Instrumento()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Instrumento', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nome: 'BBBBBB',
            codigo: 'BBBBBB',
            localizacao: 'BBBBBB',
            fabricante: 'BBBBBB',
            dimensoes: 'BBBBBB',
            descricaoPath: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Instrumento', () => {
        const patchObject = Object.assign(
          {
            codigo: 'BBBBBB',
            localizacao: 'BBBBBB',
            dimensoes: 'BBBBBB',
            descricaoPath: 'BBBBBB',
          },
          new Instrumento()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Instrumento', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nome: 'BBBBBB',
            codigo: 'BBBBBB',
            localizacao: 'BBBBBB',
            fabricante: 'BBBBBB',
            dimensoes: 'BBBBBB',
            descricaoPath: 'BBBBBB',
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

      it('should delete a Instrumento', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addInstrumentoToCollectionIfMissing', () => {
        it('should add a Instrumento to an empty array', () => {
          const instrumento: IInstrumento = { id: 123 };
          expectedResult = service.addInstrumentoToCollectionIfMissing([], instrumento);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(instrumento);
        });

        it('should not add a Instrumento to an array that contains it', () => {
          const instrumento: IInstrumento = { id: 123 };
          const instrumentoCollection: IInstrumento[] = [
            {
              ...instrumento,
            },
            { id: 456 },
          ];
          expectedResult = service.addInstrumentoToCollectionIfMissing(instrumentoCollection, instrumento);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Instrumento to an array that doesn't contain it", () => {
          const instrumento: IInstrumento = { id: 123 };
          const instrumentoCollection: IInstrumento[] = [{ id: 456 }];
          expectedResult = service.addInstrumentoToCollectionIfMissing(instrumentoCollection, instrumento);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(instrumento);
        });

        it('should add only unique Instrumento to an array', () => {
          const instrumentoArray: IInstrumento[] = [{ id: 123 }, { id: 456 }, { id: 39409 }];
          const instrumentoCollection: IInstrumento[] = [{ id: 123 }];
          expectedResult = service.addInstrumentoToCollectionIfMissing(instrumentoCollection, ...instrumentoArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const instrumento: IInstrumento = { id: 123 };
          const instrumento2: IInstrumento = { id: 456 };
          expectedResult = service.addInstrumentoToCollectionIfMissing([], instrumento, instrumento2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(instrumento);
          expect(expectedResult).toContain(instrumento2);
        });

        it('should accept null and undefined values', () => {
          const instrumento: IInstrumento = { id: 123 };
          expectedResult = service.addInstrumentoToCollectionIfMissing([], null, instrumento, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(instrumento);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
