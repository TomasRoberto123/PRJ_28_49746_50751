import { Component, HostListener, OnInit } from '@angular/core';
import { CategoriaService } from 'app/pages/categoria/service/categoria.service';
import { DataUtils } from 'app/core/util/data-util.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { ICategoria } from 'app/pages/categoria/categoria.model';
import { Router } from '@angular/router';
import { BASEIMAGEURL } from 'app/app.constants';
import { ViewportScroller } from '@angular/common';

@Component({
  selector: 'jhi-categorias-list',
  templateUrl: './categorias-list.component.html',
  styleUrls: ['./categorias-list.component.scss'],
})
export class CategoriasListComponent implements OnInit {
  public categorias?: ICategoria[];
  basePath = BASEIMAGEURL;
  pageYoffset = 0;
  private isLoading: boolean | undefined;
  constructor(
    protected categoriaService: CategoriaService,
    protected dataUtils: DataUtils,
    protected modalService: NgbModal,
    private router: Router,
    private scroll: ViewportScroller
  ) {}

  loadAll(): void {
    this.isLoading = true;
    this.categoriaService.query().subscribe(
      (res: HttpResponse<ICategoria[]>) => {
        this.isLoading = false;
        this.categorias = res.body ?? [];
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

  goToSubtemas(idCategoria: any): void {
    this.router.navigate([idCategoria, 'subs']);
  }
}
