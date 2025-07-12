import { Component, HostListener, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { DataUtils } from 'app/core/util/data-util.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ActivatedRoute, Router } from '@angular/router';
import { IQuestionario, Questionario } from 'app/pages/questionario/questionario.model';
import { QuestionarioService } from 'app/pages/questionario/service/questionario.service';
import { QuestaoQuestionario } from 'app/pages/questao-questionario/questao-questionario.model';
import { OpcaoQuestaoQuestionarioService } from 'app/pages/opcao-questao-questionario/service/opcao-questao-questionario.service';
import { IOpcaoQuestaoQuestionario, OpcaoQuestaoQuestionario } from 'app/pages/opcao-questao-questionario/opcao-questao-questionario.model';
import { RespostaUserQuestionarioService } from 'app/pages/resposta-user-questionario/service/resposta-user-questionario.service';
import { RespostaUserQuestionario } from 'app/pages/resposta-user-questionario/resposta-user-questionario.model';
import { AccountService } from 'app/core/auth/account.service';
import { UserManagementService } from 'app/admin/user-management/service/user-management.service';
import { IUser } from 'app/pages/user/user.model';
import { BASEIMAGEURL, MODULOGAMIFICACAO } from 'app/app.constants';
import { IInstrumento } from 'app/pages/instrumento/instrumento.model';
import { InstrumentoService } from 'app/pages/instrumento/service/instrumento.service';
import { FotoService } from 'app/pages/foto/service/foto.service';
import { ViewportScroller } from '@angular/common';

@Component({
  selector: 'jhi-questionario-pagina',
  templateUrl: './questionario-pagina.component.html',
  styleUrls: ['./questionario-pagina.component.scss'],
})
export class QuestionarioPaginaComponent implements OnInit {
  questionario = new Questionario();
  isLoading = false;
  numQuestao = 1;
  basePath = BASEIMAGEURL;
  questaoActual = new QuestaoQuestionario();
  respostasEscolhidas: OpcaoQuestaoQuestionario[] = [];
  selectedType: any = undefined;
  showResult = false;
  resultPercentage = 0;
  respostasCertasTotal = 0;
  submitted = false;
  user?: IUser;
  modulogamificacao = MODULOGAMIFICACAO;
  entrar = false;
  pageYoffset = 0;
  private saved: boolean | undefined;
  private account: any;

  constructor(
    protected questionarioService: QuestionarioService,
    protected opcaoQuestaoQuestionarioService: OpcaoQuestaoQuestionarioService,
    protected respostaUserQuestionarioService: RespostaUserQuestionarioService,
    protected instrumentoService: InstrumentoService,
    protected accountService: AccountService,
    protected fotoService: FotoService,
    protected userManagementService: UserManagementService,
    protected dataUtils: DataUtils,
    protected modalService: NgbModal,
    private router: Router,
    private route: ActivatedRoute,
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
    this.isLoading = true;
    const paramId = this.route.snapshot.params['idQuestionario'];
    this.questionarioService.find(paramId).subscribe(
      (res: HttpResponse<IQuestionario>) => {
        this.questionario = res.body ?? {};
        this.isLoading = false;
        this.questionario.questaoQuestionarios = this.shuffleArray(this.questionario.questaoQuestionarios);
        if (this.questionario.questaoQuestionarios) {
          this.questaoActual = this.questionario.questaoQuestionarios[0];
          this.findFotosInstrumentoActual(this.questaoActual.instrumento?.id);
          this.findOpcoesRequest(this.questaoActual.id);
        }
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
    this.accountService.identity().subscribe(account => {
      this.account = account;
      this.userManagementService.find(this.account.login).subscribe(user => {
        this.user = user;
        console.log(user);
      });
    });
  }

  getNextQuestion(): void {
    this.respostasEscolhidas.push(this.selectedType);
    if (this.questionario.questaoQuestionarios) {
      if (this.numQuestao < this.questionario.questaoQuestionarios.length) {
        this.numQuestao++;
        this.questaoActual = this.questionario.questaoQuestionarios[this.numQuestao - 1];
        this.findFotosInstrumentoActual(this.questaoActual.instrumento?.id);
        this.findOpcoesRequest(this.questaoActual.id);
        this.selectedType = null;
      } else {
        this.router.navigate(['questionarios']);
      }
    }
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

  isLastQuestao(): boolean {
    if (this.questionario.questaoQuestionarios) {
      return (
        this.questaoActual.pergunta ===
        this.questionario?.questaoQuestionarios[this.questionario?.questaoQuestionarios.length - 1]?.pergunta
      );
    }
    return false;
  }

  submeterQuestonario(): void {
    let percentage = 0;
    this.respostasEscolhidas.push(this.selectedType);
    let certas = 0;
    for (const o of this.respostasEscolhidas) {
      if (o.correcta) {
        certas++;
      }
    }
    if (this.questionario.questaoQuestionarios) {
      percentage = (certas / this.questionario.questaoQuestionarios.length) * 100;
    }
    this.saveResult(percentage);
    this.respostasCertasTotal = certas;
    this.resultPercentage = parseFloat(parseFloat(String(percentage)).toFixed(2));
    this.showResult = true;
  }

  selectOption(opcao: IOpcaoQuestaoQuestionario): void {
    this.selectedType = opcao;
  }

  sair(): void {
    this.router.navigate(['questionarios']);
  }

  saveResult(pontuacao: number): void {
    const respostaUserQuestionario = new RespostaUserQuestionario();
    respostaUserQuestionario.pontuacao = pontuacao;
    respostaUserQuestionario.user = this.user;
    respostaUserQuestionario.questionario = this.questionario;
    this.respostaUserQuestionarioService.create(respostaUserQuestionario).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
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
  getCracha(): string {
    let basePath = BASEIMAGEURL + 'images/badges/';
    let src = basePath;
    let nomefoto = '';
    let cor = '';
    // @ts-ignore
    if (this.resultPercentage < 50) {
      src += 'no_badge.jpg';
      return src;
    }
    nomefoto = this.getNomeFoto(this.questionario.id);
    cor = this.getBadgeColor(this.resultPercentage);
    src += nomefoto + '_' + cor + '.jpg';
    return src;
  }

  getBadgeColor(pontuacao: number): string {
    if (pontuacao >= 90) {
      return 'gold';
    }
    if (pontuacao >= 70) {
      return 'silver';
    }
    if (pontuacao >= 50) {
      return 'bronze';
    }
    return 'nobadge';
  }

  getNomeFoto(id?: number): string {
    if (id === 1) {
      return 'acustica';
    }
    if (id === 2) {
      return 'calor';
    }
    if (id === 3) {
      return 'capilaridade';
    }
    if (id === 4) {
      return 'eletro';
    }
    if (id === 5) {
      return 'hidro';
    }
    if (id === 6) {
      return 'mecanica';
    }
    if (id === 7) {
      return 'optica';
    }
    return '';
  }

  protected onSaveSuccess(): void {
    this.saved = true;
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }
}
