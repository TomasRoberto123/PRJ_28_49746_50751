import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IQuestaoQuestionario, QuestaoQuestionario } from '../questao-questionario.model';

import { QuestaoQuestionarioService } from './questao-questionario.service';

describe('Service Tests', () => {
  describe('QuestaoQuestionario Service', () => {
    let service: QuestaoQuestionarioService;
    let httpMock: HttpTestingController;
    let elemDefault: IQuestaoQuestionario;
    let expectedResult: IQuestaoQuestionario | IQuestaoQuestionario[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(QuestaoQuestionarioService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        pergunta: 'AAAAAAA',
        pontuacao: 'AAAAAAA',
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

      it('should create a QuestaoQuestionario', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new QuestaoQuestionario()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a QuestaoQuestionario', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            pergunta: 'BBBBBB',
            pontuacao: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a QuestaoQuestionario', () => {
        const patchObject = Object.assign({}, new QuestaoQuestionario());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of QuestaoQuestionario', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            pergunta: 'BBBBBB',
            pontuacao: 'BBBBBB',
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

      it('should delete a QuestaoQuestionario', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addQuestaoQuestionarioToCollectionIfMissing', () => {
        it('should add a QuestaoQuestionario to an empty array', () => {
          const questaoQuestionario: IQuestaoQuestionario = { id: 123 };
          expectedResult = service.addQuestaoQuestionarioToCollectionIfMissing([], questaoQuestionario);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(questaoQuestionario);
        });

        it('should not add a QuestaoQuestionario to an array that contains it', () => {
          const questaoQuestionario: IQuestaoQuestionario = { id: 123 };
          const questaoQuestionarioCollection: IQuestaoQuestionario[] = [
            {
              ...questaoQuestionario,
            },
            { id: 456 },
          ];
          expectedResult = service.addQuestaoQuestionarioToCollectionIfMissing(questaoQuestionarioCollection, questaoQuestionario);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a QuestaoQuestionario to an array that doesn't contain it", () => {
          const questaoQuestionario: IQuestaoQuestionario = { id: 123 };
          const questaoQuestionarioCollection: IQuestaoQuestionario[] = [{ id: 456 }];
          expectedResult = service.addQuestaoQuestionarioToCollectionIfMissing(questaoQuestionarioCollection, questaoQuestionario);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(questaoQuestionario);
        });

        it('should add only unique QuestaoQuestionario to an array', () => {
          const questaoQuestionarioArray: IQuestaoQuestionario[] = [{ id: 123 }, { id: 456 }, { id: 10247 }];
          const questaoQuestionarioCollection: IQuestaoQuestionario[] = [{ id: 123 }];
          expectedResult = service.addQuestaoQuestionarioToCollectionIfMissing(questaoQuestionarioCollection, ...questaoQuestionarioArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const questaoQuestionario: IQuestaoQuestionario = { id: 123 };
          const questaoQuestionario2: IQuestaoQuestionario = { id: 456 };
          expectedResult = service.addQuestaoQuestionarioToCollectionIfMissing([], questaoQuestionario, questaoQuestionario2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(questaoQuestionario);
          expect(expectedResult).toContain(questaoQuestionario2);
        });

        it('should accept null and undefined values', () => {
          const questaoQuestionario: IQuestaoQuestionario = { id: 123 };
          expectedResult = service.addQuestaoQuestionarioToCollectionIfMissing([], null, questaoQuestionario, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(questaoQuestionario);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
