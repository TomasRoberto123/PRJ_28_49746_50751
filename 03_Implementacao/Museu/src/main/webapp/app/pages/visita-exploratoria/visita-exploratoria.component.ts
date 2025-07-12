import { Component, HostListener, OnInit } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Questionario } from 'app/pages/questionario/questionario.model';
import { IQuestaoQuestionario, QuestaoQuestionario } from 'app/pages/questao-questionario/questao-questionario.model';
import { IOpcaoQuestaoQuestionario, OpcaoQuestaoQuestionario } from 'app/pages/opcao-questao-questionario/opcao-questao-questionario.model';
import { QuestionarioService } from 'app/pages/questionario/service/questionario.service';
import { OpcaoQuestaoQuestionarioService } from 'app/pages/opcao-questao-questionario/service/opcao-questao-questionario.service';
import { RespostaUserQuestionarioService } from 'app/pages/resposta-user-questionario/service/resposta-user-questionario.service';
import { DataUtils } from 'app/core/util/data-util.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ActivatedRoute, Router } from '@angular/router';
import { QuestaoQuestionarioService } from 'app/pages/questao-questionario/service/questao-questionario.service';
import { InstrumentoService } from 'app/pages/instrumento/service/instrumento.service';
import { FotoService } from 'app/pages/foto/service/foto.service';
import { IInstrumento } from 'app/pages/instrumento/instrumento.model';
import { Subscription, timer } from 'rxjs';
import { BASEIMAGEURL, MODULOGAMIFICACAO } from 'app/app.constants';
import { ViewportScroller } from '@angular/common';

@Component({
  selector: 'jhi-visita-exploratoria',
  templateUrl: './visita-exploratoria.component.html',
  styleUrls: ['./visita-exploratoria.component.scss'],
})
export class VisitaExploratoriaComponent implements OnInit {
  questionario = new Questionario();
  isLoading = false;
  numQuestao = 1;
  pageYoffset = 0;
  questaoActual = new QuestaoQuestionario();
  respostasEscolhidas: OpcaoQuestaoQuestionario[] = [];
  selectedType: any;
  isShown = false;
  questoes: IQuestaoQuestionario[] = [];
  counter = 120;
  baseTime = 120;
  tick = 1000;
  countDown: Subscription | undefined;
  basePath = BASEIMAGEURL;
  descricao = '';
  path = '{{basePath}}';
  searchRegExp = new RegExp(this.path, 'g');
  respondeu = false;
  entrar = false;
  modulogamificacao = MODULOGAMIFICACAO;

  constructor(
    protected questionarioService: QuestionarioService,
    protected questaoQuestionarioService: QuestaoQuestionarioService,
    protected opcaoQuestaoQuestionarioService: OpcaoQuestaoQuestionarioService,
    protected respostaUserQuestionarioService: RespostaUserQuestionarioService,
    protected fotoService: FotoService,
    protected instrumentoService: InstrumentoService,
    protected dataUtils: DataUtils,
    protected modalService: NgbModal,
    private router: Router,
    private route: ActivatedRoute,
    private http: HttpClient,
    private scroll: ViewportScroller
  ) {
    this.questionario.questaoQuestionarios = [];
  }

  @HostListener('window:scroll', ['$event']) onScroll() {
    this.pageYoffset = window.pageYOffset;
  }
  scrollToTop() {
    this.scroll.scrollToPosition([0, 0]);
  }
  loadAll(): void {
    this.questaoQuestionarioService.query().subscribe(
      (res: HttpResponse<IQuestaoQuestionario[]>) => {
        this.questoes = res.body ?? [];
        this.isLoading = false;
        this.questoes = this.shuffleArray(this.questoes);
        this.questaoActual = this.questoes[this.numQuestao - 1];
        this.findFotosInstrumentoActual(this.questaoActual.instrumento?.id);
        this.findOpcoesRequest(this.questaoActual.id);
        if (this.questaoActual.instrumento?.descricaoPath) {
          this.getDescricao(this.questaoActual.instrumento.descricaoPath);
        }
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }
  getCounter(tick: number): any {
    return timer(0, tick);
  }

  findOpcoesRequest(idQuestao: any): void {
    this.opcaoQuestaoQuestionarioService.findAllByQuestaoId(idQuestao).subscribe((response: HttpResponse<IOpcaoQuestaoQuestionario[]>) => {
      this.questaoActual.opcaoQuestaoQuestionarios = response.body ?? [];
      this.questaoActual.opcaoQuestaoQuestionarios = this.shuffleArray(this.questaoActual.opcaoQuestaoQuestionarios);
    });
  }

  findFotosInstrumentoActual(idInstrumento: any): void {
    this.instrumentoService.find(idInstrumento).subscribe((response: HttpResponse<IInstrumento>) => {
      this.questaoActual.instrumento = response.body ?? {};
    });
  }

  showResposta(): void {
    if (this.numQuestao < this.questoes.length) {
      console.log(this.questaoActual.opcaoQuestaoQuestionarios);

      console.log(this.selectedType);
      this.respondeu = true;
    }
  }

  getNextQuestion(): void {
    if (this.numQuestao < this.questoes.length) {
      this.numQuestao++;
      this.questaoActual = this.questoes[this.numQuestao - 1];
      this.findFotosInstrumentoActual(this.questaoActual.instrumento?.id);
      this.findOpcoesRequest(this.questaoActual.id);
      if (this.questaoActual.instrumento?.descricaoPath) {
        this.getDescricao(this.questaoActual.instrumento.descricaoPath);
      }
      this.isShown = false;
      this.selectedType = false;
      this.counter = 120;
      this.respondeu = false;
    } else {
      this.numQuestao = 1;
      this.questaoActual = this.questoes[this.numQuestao - 1];
      this.findFotosInstrumentoActual(this.questaoActual.instrumento?.id);
      this.findOpcoesRequest(this.questaoActual.id);
      if (this.questaoActual.instrumento?.descricaoPath) {
        this.getDescricao(this.questaoActual.instrumento.descricaoPath);
      }
      this.isShown = false;
      this.counter = 120;
      this.respondeu = false;
    }
  }

  showDetalhes(): void {
    this.isShown = true;
    this.counter = 0;
  }

  public getDescricao(s?: string): void {
    this.http.get(this.basePath + s, { responseType: 'text' }).subscribe(data => {
      this.descricao = data;
      this.descricao = this.descricao.replace(this.searchRegExp, this.basePath);
    });
  }

  public shuffleArray(array: any): any[] {
    let curId = array.length;
    // There remain elements to shuffle
    while (0 !== curId) {
      // Pick a remaining element
      let randId = Math.floor(Math.random() * curId);
      curId -= 1;
      // Swap it with the current element.
      let tmp = array[curId];
      array[curId] = array[randId];
      array[randId] = tmp;
    }
    return array;
  }

  getCor(selectedType: any): string {
    if (this.respondeu) {
      if (selectedType.correcta) {
        return 'limegreen';
      }
      if (!selectedType.correcta) {
        return 'red';
      }
    }
    return '';
  }

  startCountdown(): void {
    this.countDown = this.getCounter(this.tick).subscribe(() => {
      if (this.counter > 0) {
        this.counter--;
      } else {
        this.isShown = true;
      }
    });
  }
}
