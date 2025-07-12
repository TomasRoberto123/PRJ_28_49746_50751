import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IBibliografia, Bibliografia } from '../bibliografia.model';

import { BibliografiaService } from './bibliografia.service';

describe('Service Tests', () => {
  describe('Bibliografia Service', () => {
    let service: BibliografiaService;
    let httpMock: HttpTestingController;
    let elemDefault: IBibliografia;
    let expectedResult: IBibliografia | IBibliografia[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(BibliografiaService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        titulo: 'AAAAAAA',
        edicao: 0,
        editora: 'AAAAAAA',
        localizacao: 'AAAAAAA',
        ano: 'AAAAAAA',
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

      it('should create a Bibliografia', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Bibliografia()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Bibliografia', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            titulo: 'BBBBBB',
            edicao: 1,
            editora: 'BBBBBB',
            localizacao: 'BBBBBB',
            ano: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Bibliografia', () => {
        const patchObject = Object.assign(
          {
            titulo: 'BBBBBB',
            localizacao: 'BBBBBB',
            ano: 'BBBBBB',
          },
          new Bibliografia()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Bibliografia', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            titulo: 'BBBBBB',
            edicao: 1,
            editora: 'BBBBBB',
            localizacao: 'BBBBBB',
            ano: 'BBBBBB',
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

      it('should delete a Bibliografia', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addBibliografiaToCollectionIfMissing', () => {
        it('should add a Bibliografia to an empty array', () => {
          const bibliografia: IBibliografia = { id: 123 };
          expectedResult = service.addBibliografiaToCollectionIfMissing([], bibliografia);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(bibliografia);
        });

        it('should not add a Bibliografia to an array that contains it', () => {
          const bibliografia: IBibliografia = { id: 123 };
          const bibliografiaCollection: IBibliografia[] = [
            {
              ...bibliografia,
            },
            { id: 456 },
          ];
          expectedResult = service.addBibliografiaToCollectionIfMissing(bibliografiaCollection, bibliografia);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Bibliografia to an array that doesn't contain it", () => {
          const bibliografia: IBibliografia = { id: 123 };
          const bibliografiaCollection: IBibliografia[] = [{ id: 456 }];
          expectedResult = service.addBibliografiaToCollectionIfMissing(bibliografiaCollection, bibliografia);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(bibliografia);
        });

        it('should add only unique Bibliografia to an array', () => {
          const bibliografiaArray: IBibliografia[] = [{ id: 123 }, { id: 456 }, { id: 40970 }];
          const bibliografiaCollection: IBibliografia[] = [{ id: 123 }];
          expectedResult = service.addBibliografiaToCollectionIfMissing(bibliografiaCollection, ...bibliografiaArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const bibliografia: IBibliografia = { id: 123 };
          const bibliografia2: IBibliografia = { id: 456 };
          expectedResult = service.addBibliografiaToCollectionIfMissing([], bibliografia, bibliografia2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(bibliografia);
          expect(expectedResult).toContain(bibliografia2);
        });

        it('should accept null and undefined values', () => {
          const bibliografia: IBibliografia = { id: 123 };
          expectedResult = service.addBibliografiaToCollectionIfMissing([], null, bibliografia, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(bibliografia);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
