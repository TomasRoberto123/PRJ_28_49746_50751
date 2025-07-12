import { Component, HostListener, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { IInstrumento, Instrumento } from 'app/pages/instrumento/instrumento.model';
import { InstrumentoService } from 'app/pages/instrumento/service/instrumento.service';
import { DataUtils } from 'app/core/util/data-util.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Router } from '@angular/router';
// @ts-ignore
import * as _ from 'underscore';
import { ViewportScroller } from '@angular/common';

@Component({
  selector: 'jhi-glossario',
  templateUrl: './glossario.component.html',
  styleUrls: ['./glossario.component.scss'],
})
export class GlossarioComponent implements OnInit {
  isLoading = false;
  pageYoffset = 0;
  instrumentos: Instrumento[] = [];
  letras = [
    'A-Z',
    'A',
    'B',
    'C',
    'D',
    'E',
    'F',
    'G',
    'H',
    'I',
    'J',
    'K',
    'L',
    'M',
    'N',
    'O',
    'P',
    'Q',
    'R',
    'S',
    'T',
    'U',
    'V',
    'W',
    'X',
    'Y',
    'Z',
  ];
  selected = 0;
  filtrados: Instrumento[] = [];

  constructor(
    protected instrumentoService: InstrumentoService,
    protected dataUtils: DataUtils,
    protected modalService: NgbModal,
    private router: Router,
    private scroll: ViewportScroller
  ) {}

  ngOnInit(): void {
    this.loadAll();
  }

  @HostListener('window:scroll', ['$event']) onScroll() {
    this.pageYoffset = window.pageYOffset;
  }
  scrollToTop() {
    this.scroll.scrollToPosition([0, 0]);
  }
  loadAll(): void {
    this.instrumentoService.query().subscribe(
      (response: HttpResponse<IInstrumento[]>) => {
        this.isLoading = false;
        this.instrumentos = response.body ?? [];
        this.filtrados = this.instrumentos;
        this.filtrados = _.sortBy(this.filtrados, 'nome');
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  filter(filter: string): Instrumento[] {
    this.filtrados = [];
    if (this.instrumentos.length > 0) {
      if (filter === this.letras[0]) {
        this.filtrados = this.instrumentos;
        this.filtrados = _.sortBy(this.filtrados, 'nome');
        this.selected = 0;
      } else {
        this.filtrados = this.instrumentos.filter(item => item.nome?.startsWith(filter));
        this.filtrados = _.sortBy(this.filtrados, 'nome');
        this.selected = this.letras.indexOf(filter);
      }
    }
    return this.filtrados;
  }

  goToInstrumento(idAparelho: number): void {
    this.router.navigate(['apa', idAparelho]).then();
  }

  disableLetra(filter: string) {
    if (filter !== 'A-Z') {
      return this.instrumentos.filter(item => item.nome?.startsWith(filter)).length === 0;
    }
    return false;
  }
}
