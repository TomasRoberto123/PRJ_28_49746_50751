import { Component, HostListener, OnInit } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { DataUtils } from 'app/core/util/data-util.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ActivatedRoute, Router } from '@angular/router';
import { IHistoria } from 'app/pages/historia/historia.model';
import { HistoriaService } from 'app/pages/historia/service/historia.service';
import { BASEIMAGEURL } from 'app/app.constants';
import { ViewportScroller } from '@angular/common';

@Component({
  selector: 'jhi-historia-details',
  templateUrl: './historia-details.component.html',
  styleUrls: ['./historia-details.component.scss'],
})
export class HistoriaDetailsComponent implements OnInit {
  isLoading = false;
  historia?: IHistoria;
  basePath = BASEIMAGEURL;
  descricao = '';
  pageYoffset = 0;

  constructor(
    protected historiaService: HistoriaService,
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
    const paramId = this.route.snapshot.params['idHistoria'];
    this.historiaService.find(paramId).subscribe(
      (res: HttpResponse<IHistoria>) => {
        this.historia = res.body ?? {};
        if (this.historia.conteudoPath) {
          this.getConteudo(this.historia.conteudoPath);
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
  }

  public getConteudo(s?: string): void {
    this.http.get(this.basePath + s, { responseType: 'text' }).subscribe(data => {
      this.descricao = data;
    });
  }
}
