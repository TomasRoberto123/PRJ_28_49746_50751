import { Component, HostListener, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { DataUtils } from 'app/core/util/data-util.service';
import { MODULOGAMIFICACAO } from 'app/app.constants';
import { ViewportScroller } from '@angular/common';

@Component({
  selector: 'jhi-visita-livre',
  templateUrl: './visita-livre.component.html',
  styleUrls: ['./visita-livre.component.scss'],
})
export class VisitaLivreComponent implements OnInit {
  loggedIN = true;
  old = false;
  pageYoffset = 0;
  modulogamificacao = MODULOGAMIFICACAO;
  private isLoading = false;

  constructor(private router: Router, protected dataUtils: DataUtils,private scroll: ViewportScroller) {}

  @HostListener('window:scroll', ['$event']) onScroll() {
    this.pageYoffset = window.pageYOffset;
  }
  scrollToTop() {
    this.scroll.scrollToPosition([0, 0]);
  }
  ngOnInit(): void {
    this.isLoading = false;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  goToInstrumentos(): void {
    this.router.navigate(['/categorias']);
  }

  goToGlossario(): void {
    this.router.navigate(['/glossario']);
  }

  goToQuestionarios(): void {
    this.router.navigate(['/questionarios']);
  }

  goToHistorias(): void {
    this.router.navigate(['/historias']);
  }
}
