import { Component, HostListener, OnInit } from '@angular/core';
import { ICategoria } from 'app/pages/categoria/categoria.model';
import { DataUtils } from 'app/core/util/data-util.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Router } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { QuestionarioService } from 'app/pages/questionario/service/questionario.service';
import { IQuestionario } from 'app/pages/questionario/questionario.model';
import { BASEIMAGEURL, MODULOGAMIFICACAO } from 'app/app.constants';
import { ViewportScroller } from '@angular/common';

@Component({
  selector: 'jhi-questionarios-list',
  templateUrl: './questionarios-list.component.html',
  styleUrls: ['./questionarios-list.component.scss'],
})
export class QuestionariosListComponent implements OnInit {
  public questionarios?: IQuestionario[];
  basePath = BASEIMAGEURL;
  pageYoffset = 0;
  modulogamificacao = MODULOGAMIFICACAO;
  private isLoading: boolean | undefined;

  constructor(
    protected questionarioService: QuestionarioService,
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
    this.questionarioService.query().subscribe(
      (res: HttpResponse<ICategoria[]>) => {
        this.isLoading = false;
        this.questionarios = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  goToQuestionario(id: number): void {
    this.router.navigate(['questionarios', id]);
  }
}
