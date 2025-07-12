import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IRespostaUserQuestionario, RespostaUserQuestionario } from '../resposta-user-questionario.model';

import { RespostaUserQuestionarioService } from './resposta-user-questionario.service';

describe('Service Tests', () => {
  describe('RespostaUserQuestionario Service', () => {
    let service: RespostaUserQuestionarioService;
    let httpMock: HttpTestingController;
    let elemDefault: IRespostaUserQuestionario;
    let expectedResult: IRespostaUserQuestionario | IRespostaUserQuestionario[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(RespostaUserQuestionarioService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        pontuacao: 0,
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

      it('should create a RespostaUserQuestionario', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new RespostaUserQuestionario()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a RespostaUserQuestionario', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            pontuacao: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a RespostaUserQuestionario', () => {
        const patchObject = Object.assign({}, new RespostaUserQuestionario());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of RespostaUserQuestionario', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            pontuacao: 1,
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

      it('should delete a RespostaUserQuestionario', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addRespostaUserQuestionarioToCollectionIfMissing', () => {
        it('should add a RespostaUserQuestionario to an empty array', () => {
          const respostaUserQuestionario: IRespostaUserQuestionario = { id: 123 };
          expectedResult = service.addRespostaUserQuestionarioToCollectionIfMissing([], respostaUserQuestionario);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(respostaUserQuestionario);
        });

        it('should not add a RespostaUserQuestionario to an array that contains it', () => {
          const respostaUserQuestionario: IRespostaUserQuestionario = { id: 123 };
          const respostaUserQuestionarioCollection: IRespostaUserQuestionario[] = [
            {
              ...respostaUserQuestionario,
            },
            { id: 456 },
          ];
          expectedResult = service.addRespostaUserQuestionarioToCollectionIfMissing(
            respostaUserQuestionarioCollection,
            respostaUserQuestionario
          );
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a RespostaUserQuestionario to an array that doesn't contain it", () => {
          const respostaUserQuestionario: IRespostaUserQuestionario = { id: 123 };
          const respostaUserQuestionarioCollection: IRespostaUserQuestionario[] = [{ id: 456 }];
          expectedResult = service.addRespostaUserQuestionarioToCollectionIfMissing(
            respostaUserQuestionarioCollection,
            respostaUserQuestionario
          );
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(respostaUserQuestionario);
        });

        it('should add only unique RespostaUserQuestionario to an array', () => {
          const respostaUserQuestionarioArray: IRespostaUserQuestionario[] = [{ id: 123 }, { id: 456 }, { id: 46602 }];
          const respostaUserQuestionarioCollection: IRespostaUserQuestionario[] = [{ id: 123 }];
          expectedResult = service.addRespostaUserQuestionarioToCollectionIfMissing(
            respostaUserQuestionarioCollection,
            ...respostaUserQuestionarioArray
          );
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const respostaUserQuestionario: IRespostaUserQuestionario = { id: 123 };
          const respostaUserQuestionario2: IRespostaUserQuestionario = { id: 456 };
          expectedResult = service.addRespostaUserQuestionarioToCollectionIfMissing(
            [],
            respostaUserQuestionario,
            respostaUserQuestionario2
          );
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(respostaUserQuestionario);
          expect(expectedResult).toContain(respostaUserQuestionario2);
        });

        it('should accept null and undefined values', () => {
          const respostaUserQuestionario: IRespostaUserQuestionario = { id: 123 };
          expectedResult = service.addRespostaUserQuestionarioToCollectionIfMissing([], null, respostaUserQuestionario, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(respostaUserQuestionario);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
