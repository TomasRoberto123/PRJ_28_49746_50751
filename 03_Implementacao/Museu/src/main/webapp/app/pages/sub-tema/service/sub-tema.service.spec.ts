import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISubTema, SubTema } from '../sub-tema.model';

import { SubTemaService } from './sub-tema.service';

describe('Service Tests', () => {
  describe('SubTema Service', () => {
    let service: SubTemaService;
    let httpMock: HttpTestingController;
    let elemDefault: ISubTema;
    let expectedResult: ISubTema | ISubTema[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(SubTemaService);
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

      it('should create a SubTema', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new SubTema()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a SubTema', () => {
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

      it('should partial update a SubTema', () => {
        const patchObject = Object.assign(
          {
            descricao: 'BBBBBB',
          },
          new SubTema()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of SubTema', () => {
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

      it('should delete a SubTema', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addSubTemaToCollectionIfMissing', () => {
        it('should add a SubTema to an empty array', () => {
          const subTema: ISubTema = { id: 123 };
          expectedResult = service.addSubTemaToCollectionIfMissing([], subTema);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(subTema);
        });

        it('should not add a SubTema to an array that contains it', () => {
          const subTema: ISubTema = { id: 123 };
          const subTemaCollection: ISubTema[] = [
            {
              ...subTema,
            },
            { id: 456 },
          ];
          expectedResult = service.addSubTemaToCollectionIfMissing(subTemaCollection, subTema);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a SubTema to an array that doesn't contain it", () => {
          const subTema: ISubTema = { id: 123 };
          const subTemaCollection: ISubTema[] = [{ id: 456 }];
          expectedResult = service.addSubTemaToCollectionIfMissing(subTemaCollection, subTema);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(subTema);
        });

        it('should add only unique SubTema to an array', () => {
          const subTemaArray: ISubTema[] = [{ id: 123 }, { id: 456 }, { id: 17661 }];
          const subTemaCollection: ISubTema[] = [{ id: 123 }];
          expectedResult = service.addSubTemaToCollectionIfMissing(subTemaCollection, ...subTemaArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const subTema: ISubTema = { id: 123 };
          const subTema2: ISubTema = { id: 456 };
          expectedResult = service.addSubTemaToCollectionIfMissing([], subTema, subTema2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(subTema);
          expect(expectedResult).toContain(subTema2);
        });

        it('should accept null and undefined values', () => {
          const subTema: ISubTema = { id: 123 };
          expectedResult = service.addSubTemaToCollectionIfMissing([], null, subTema, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(subTema);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
