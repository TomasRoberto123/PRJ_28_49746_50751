import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IQuestionario, Questionario } from '../questionario.model';

import { QuestionarioService } from './questionario.service';

describe('Service Tests', () => {
  describe('Questionario Service', () => {
    let service: QuestionarioService;
    let httpMock: HttpTestingController;
    let elemDefault: IQuestionario;
    let expectedResult: IQuestionario | IQuestionario[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(QuestionarioService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        nome: 'AAAAAAA',
        descricao: 'AAAAAAA',
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

      it('should create a Questionario', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Questionario()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Questionario', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nome: 'BBBBBB',
            descricao: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Questionario', () => {
        const patchObject = Object.assign(
          {
            descricao: 'BBBBBB',
          },
          new Questionario()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Questionario', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nome: 'BBBBBB',
            descricao: 'BBBBBB',
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

      it('should delete a Questionario', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addQuestionarioToCollectionIfMissing', () => {
        it('should add a Questionario to an empty array', () => {
          const questionario: IQuestionario = { id: 123 };
          expectedResult = service.addQuestionarioToCollectionIfMissing([], questionario);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(questionario);
        });

        it('should not add a Questionario to an array that contains it', () => {
          const questionario: IQuestionario = { id: 123 };
          const questionarioCollection: IQuestionario[] = [
            {
              ...questionario,
            },
            { id: 456 },
          ];
          expectedResult = service.addQuestionarioToCollectionIfMissing(questionarioCollection, questionario);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Questionario to an array that doesn't contain it", () => {
          const questionario: IQuestionario = { id: 123 };
          const questionarioCollection: IQuestionario[] = [{ id: 456 }];
          expectedResult = service.addQuestionarioToCollectionIfMissing(questionarioCollection, questionario);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(questionario);
        });

        it('should add only unique Questionario to an array', () => {
          const questionarioArray: IQuestionario[] = [{ id: 123 }, { id: 456 }, { id: 39014 }];
          const questionarioCollection: IQuestionario[] = [{ id: 123 }];
          expectedResult = service.addQuestionarioToCollectionIfMissing(questionarioCollection, ...questionarioArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const questionario: IQuestionario = { id: 123 };
          const questionario2: IQuestionario = { id: 456 };
          expectedResult = service.addQuestionarioToCollectionIfMissing([], questionario, questionario2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(questionario);
          expect(expectedResult).toContain(questionario2);
        });

        it('should accept null and undefined values', () => {
          const questionario: IQuestionario = { id: 123 };
          expectedResult = service.addQuestionarioToCollectionIfMissing([], null, questionario, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(questionario);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
