import { Component, HostListener, OnInit } from '@angular/core';
import { IInstrumento } from 'app/pages/instrumento/instrumento.model';
import { DataUtils } from 'app/core/util/data-util.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { InstrumentoService } from 'app/pages/instrumento/service/instrumento.service';
import { BASEIMAGEURL } from 'app/app.constants';
import { Foto } from 'app/pages/foto/foto.model';
import { ViewportScroller } from '@angular/common';

@Component({
  selector: 'jhi-aparelho-detalhe',
  templateUrl: './aparelho-detalhe.component.html',
  styleUrls: ['./aparelho-detalhe.component.scss'],
})
export class AparelhoDetalheComponent implements OnInit {
  instrumento?: IInstrumento;
  isLoading = false;
  basePath = BASEIMAGEURL;
  descricao: any;
  pageYoffset = 0;
  foto?: Foto = new Foto();
  index = 0;
  fotos: any;
  imageUrlArray: any[] = [];
  path = '{{basePath}}';
  searchRegExp = new RegExp(this.path, 'g');

  constructor(
    protected instrumentoService: InstrumentoService,
    protected dataUtils: DataUtils,
    protected modalService: NgbModal,
    private router: Router,
    private route: ActivatedRoute,
    private http: HttpClient,
    private scroll: ViewportScroller
  ) {}

  @HostListener('window:scroll', ['$event']) onScroll() {
    this.pageYoffset = window.pageYOffset;
  }
  scrollToTop() {
    this.scroll.scrollToPosition([0, 0]);
  }
  loadAll(): void {
    this.isLoading = true;
    const paramId = this.route.snapshot.params['idAparelho'];
    this.instrumentoService.find(paramId).subscribe(
      (res: HttpResponse<IInstrumento>) => {
        this.instrumento = res.body ?? {};
        if (this.instrumento !== null) {
          if (this.instrumento.fotos) {
            if (this.instrumento.descricaoPath) {
              this.getDescricao(this.instrumento.descricaoPath);
              if (this.instrumento.fotos.length >= 0) {
                this.foto = this.instrumento.fotos[0];
                this.index = 0;
              }
            }
          }
        }
        this.isLoading = false;
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
    this.isLoading = false;
  }

  getDescricao(s?: string): void {
    this.http.get(this.basePath + s, { responseType: 'text' }).subscribe(data => {
      this.descricao = data;
      this.descricao = this.descricao.replace(this.searchRegExp, this.basePath);
      console.log('dimensao ready');
    });
  }

  getNextFotoSlide(index: number): void {
    // @ts-ignore
    if (index === this.instrumento?.fotos?.length - 1) {
      // @ts-ignore
      this.foto = this.instrumento?.fotos[0];
      this.index = 0;
    } else {
      // @ts-ignore
      this.foto = this.instrumento?.fotos[index + 1];
      this.index++;
    }
  }

  getPreviousFotoSlide(index: number): void {
    if (index < 0) {
      // @ts-ignore
      this.foto = this.instrumento?.fotos[this.instrumento.fotos.length - 1];
      // @ts-ignore
      this.index = this.instrumento.fotos?.length - 1;
    } else {
      // @ts-ignore
      this.foto = this.instrumento?.fotos[index - 1];
      this.index--;
    }
  }
}
