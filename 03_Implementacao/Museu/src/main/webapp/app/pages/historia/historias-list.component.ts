import { Component, HostListener, OnInit } from '@angular/core';
import { IHistoria } from 'app/pages/historia/historia.model';
import { HistoriaService } from 'app/pages/historia/service/historia.service';
import { DataUtils } from 'app/core/util/data-util.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { ViewportScroller } from '@angular/common';

@Component({
  selector: 'jhi-historias-list',
  templateUrl: './historias-list.component.html',
  styleUrls: ['./historias-list.component.scss'],
})
export class HistoriasListComponent implements OnInit {
  historias?: IHistoria[];
  isLoading = false;
  pageYoffset = 0;

  constructor(
    protected historiaService: HistoriaService,
    protected dataUtils: DataUtils,
    protected modalService: NgbModal,
    private router: Router,
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
    this.historiaService.query().subscribe(
      (res: HttpResponse<IHistoria[]>) => {
        this.isLoading = false;
        this.historias = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  goToHistoria(id: number): void {
    this.router.navigate(['historias', id]);
  }
}
