import { Component, HostListener, OnInit } from '@angular/core';
import { DataUtils } from 'app/core/util/data-util.service';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { InstrumentoService } from 'app/pages/instrumento/service/instrumento.service';
import { IInstrumento, Instrumento } from 'app/pages/instrumento/instrumento.model';
import { SubTemaService } from 'app/pages/sub-tema/service/sub-tema.service';
import { ISubTema } from 'app/pages/sub-tema/sub-tema.model';
import { BASEIMAGEURL } from 'app/app.constants';
import { timeout } from 'rxjs/operators';
import { ViewportScroller } from '@angular/common';

@Component({
  selector: 'jhi-subtemas-list',
  templateUrl: './subtemas-list.component.html',
  styleUrls: ['./subtemas-list.component.scss'],
})
export class SubtemasListComponent implements OnInit {
  subtemas?: ISubTema[] = [];
  isLoading = false;
  isCollapsed = false;
  activeIds: string[] = [];
  panels = [0, 1, 2, 3];
  instrumentos: Instrumento[] = [];
  basePath = BASEIMAGEURL;
  pageYoffset = 0;

  constructor(
    protected subtemaService: SubTemaService,
    protected instrumentoService: InstrumentoService,
    protected dataUtils: DataUtils,
    protected modalService: NgbModal,
    private router: Router,
    private route: ActivatedRoute,
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
    const paramId = this.route.snapshot.params['idCategoria'];
    this.subtemaService.findByCategoriaId(paramId).subscribe(
      (res: HttpResponse<ISubTema[]>) => {
        this.isLoading = false;
        this.subtemas = res.body ?? [];
        if (this.subtemas.length > 0) {
          for (const s of this.subtemas) {
            if (s.id !== undefined) {
              this.instrumentoService.findAllBySubtema(s.id).subscribe(
                (response: HttpResponse<IInstrumento[]>) => {
                  this.isLoading = false;
                  s.instrumentos = response.body ?? [];
                },
                () => {
                  this.isLoading = false;
                }
              );
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
  }

  goToInstrumento(idAparelho: number): void {
    this.router.navigate(['apa', idAparelho]).then();
  }

  scrollToElement(index: number): void {
    console.log(index);
    let elem = document.getElementById(String(index));
    console.log(elem);
    if (elem) {
      // elem = elem.parentElement.parentElement.parentElement.parentElement;
      console.log(elem);

      elem.scrollIntoView({ behavior: 'smooth', inline: 'nearest', block: 'end' });
      // elem.scrollIntoView(false);
    }
  }

  scrollSubtemas() {
    timeout(1000);
    document.getElementById('acordiao')?.scrollIntoView({
      behavior: 'smooth',
    });
  }

  
}
