import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IOpcaoQuestaoQuestionario, OpcaoQuestaoQuestionario } from '../opcao-questao-questionario.model';

import { OpcaoQuestaoQuestionarioService } from './opcao-questao-questionario.service';

describe('Service Tests', () => {
  describe('OpcaoQuestaoQuestionario Service', () => {
    let service: OpcaoQuestaoQuestionarioService;
    let httpMock: HttpTestingController;
    let elemDefault: IOpcaoQuestaoQuestionario;
    let expectedResult: IOpcaoQuestaoQuestionario | IOpcaoQuestaoQuestionario[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(OpcaoQuestaoQuestionarioService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        opcaoTexto: 'AAAAAAA',
        correcta: false,
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

      it('should create a OpcaoQuestaoQuestionario', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new OpcaoQuestaoQuestionario()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a OpcaoQuestaoQuestionario', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            opcaoTexto: 'BBBBBB',
            correcta: true,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a OpcaoQuestaoQuestionario', () => {
        const patchObject = Object.assign({}, new OpcaoQuestaoQuestionario());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of OpcaoQuestaoQuestionario', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            opcaoTexto: 'BBBBBB',
            correcta: true,
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

      it('should delete a OpcaoQuestaoQuestionario', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addOpcaoQuestaoQuestionarioToCollectionIfMissing', () => {
        it('should add a OpcaoQuestaoQuestionario to an empty array', () => {
          const opcaoQuestaoQuestionario: IOpcaoQuestaoQuestionario = { id: 123 };
          expectedResult = service.addOpcaoQuestaoQuestionarioToCollectionIfMissing([], opcaoQuestaoQuestionario);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(opcaoQuestaoQuestionario);
        });

        it('should not add a OpcaoQuestaoQuestionario to an array that contains it', () => {
          const opcaoQuestaoQuestionario: IOpcaoQuestaoQuestionario = { id: 123 };
          const opcaoQuestaoQuestionarioCollection: IOpcaoQuestaoQuestionario[] = [
            {
              ...opcaoQuestaoQuestionario,
            },
            { id: 456 },
          ];
          expectedResult = service.addOpcaoQuestaoQuestionarioToCollectionIfMissing(
            opcaoQuestaoQuestionarioCollection,
            opcaoQuestaoQuestionario
          );
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a OpcaoQuestaoQuestionario to an array that doesn't contain it", () => {
          const opcaoQuestaoQuestionario: IOpcaoQuestaoQuestionario = { id: 123 };
          const opcaoQuestaoQuestionarioCollection: IOpcaoQuestaoQuestionario[] = [{ id: 456 }];
          expectedResult = service.addOpcaoQuestaoQuestionarioToCollectionIfMissing(
            opcaoQuestaoQuestionarioCollection,
            opcaoQuestaoQuestionario
          );
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(opcaoQuestaoQuestionario);
        });

        it('should add only unique OpcaoQuestaoQuestionario to an array', () => {
          const opcaoQuestaoQuestionarioArray: IOpcaoQuestaoQuestionario[] = [{ id: 123 }, { id: 456 }, { id: 11341 }];
          const opcaoQuestaoQuestionarioCollection: IOpcaoQuestaoQuestionario[] = [{ id: 123 }];
          expectedResult = service.addOpcaoQuestaoQuestionarioToCollectionIfMissing(
            opcaoQuestaoQuestionarioCollection,
            ...opcaoQuestaoQuestionarioArray
          );
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const opcaoQuestaoQuestionario: IOpcaoQuestaoQuestionario = { id: 123 };
          const opcaoQuestaoQuestionario2: IOpcaoQuestaoQuestionario = { id: 456 };
          expectedResult = service.addOpcaoQuestaoQuestionarioToCollectionIfMissing(
            [],
            opcaoQuestaoQuestionario,
            opcaoQuestaoQuestionario2
          );
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(opcaoQuestaoQuestionario);
          expect(expectedResult).toContain(opcaoQuestaoQuestionario2);
        });

        it('should accept null and undefined values', () => {
          const opcaoQuestaoQuestionario: IOpcaoQuestaoQuestionario = { id: 123 };
          expectedResult = service.addOpcaoQuestaoQuestionarioToCollectionIfMissing([], null, opcaoQuestaoQuestionario, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(opcaoQuestaoQuestionario);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
