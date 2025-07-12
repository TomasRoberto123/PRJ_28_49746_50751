import { Component, HostListener, OnInit } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { IBibliografia } from 'app/pages/bibliografia/bibliografia.model';
import { BibliografiaService } from 'app/pages/bibliografia/service/bibliografia.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { BASEIMAGEURL } from 'app/app.constants';
import { ViewportScroller } from '@angular/common';

@Component({
  selector: 'jhi-bibliografia',
  templateUrl: './bibliografia-total.component.html',
  styleUrls: ['./bibliografia-total.component.scss'],
})
export class BibliografiaTotalComponent implements OnInit {
  bibliografias?: IBibliografia[];
  isLoading = false;
  basePath = BASEIMAGEURL;
  pageYoffset = 0;
  bibliografiasCatalogos: string[] = [];
  bibliografiasLivros: string[] = [];

  constructor(protected bibliografiaService: BibliografiaService, protected modalService: NgbModal, private http: HttpClient,private scroll: ViewportScroller) {}

  loadAll(): void {
    this.isLoading = true;

    this.bibliografiaService.query().subscribe(
      (res: HttpResponse<IBibliografia[]>) => {
        this.isLoading = false;
        this.bibliografias = res.body ?? [];
        if (this.bibliografias) {
          this.readTextArray(this.bibliografias);
        }
      },
      () => {
        this.isLoading = false;
      }
    );
  }


  @HostListener('window:scroll', ['$event']) onScroll() {
    this.pageYoffset = window.pageYOffset;
  }
  scrollToTop() {
    this.scroll.scrollToPosition([0, 0]);
  }
  ngOnInit(): void {
    this.loadAll();
  }

  readText(s?: string) {
    this.http.get(this.basePath + s, { responseType: 'text' }).subscribe(data => {
      let descricao = data;
      return descricao;
    });
  }

  readTextArray(s: IBibliografia[]) {
    for (let bibliografia of s) {
      this.http.get(this.basePath + bibliografia.textoPath, { responseType: 'text' }).subscribe(data => {
        if (bibliografia.isLivro) {
          this.bibliografiasLivros.push(data);
        } else {
          this.bibliografiasCatalogos.push(data);
        }
      });
    }
  }
}
