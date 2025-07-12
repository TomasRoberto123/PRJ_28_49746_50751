import { Component, HostListener, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';

import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { RespostaUserQuestionarioService } from 'app/pages/resposta-user-questionario/service/resposta-user-questionario.service';
import { HttpResponse } from '@angular/common/http';
import { IRespostaUserQuestionario } from 'app/pages/resposta-user-questionario/resposta-user-questionario.model';
import { MODULOGAMIFICACAO } from 'app/app.constants';
import { ViewportScroller } from '@angular/common';

@Component({
  selector: 'jhi-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.scss'],
})
export class SettingsComponent implements OnInit {
  account!: Account;
  success = false;
  respostasQuestionarios: IRespostaUserQuestionario[] = [];
  modulogamificacao = MODULOGAMIFICACAO;
  edit = false;
  pageYoffset = 0;
  settingsForm = this.fb.group({
    firstName: [undefined, [Validators.required, Validators.minLength(1), Validators.maxLength(50)]],
    lastName: [undefined, [Validators.required, Validators.minLength(1), Validators.maxLength(50)]],
    email: [undefined, [Validators.required, Validators.minLength(5), Validators.maxLength(254), Validators.email]],
  });
  basePath: any;

  constructor(
    private accountService: AccountService,
    private fb: FormBuilder,
    private respostaUserQuestionarioService: RespostaUserQuestionarioService,
    private scroll: ViewportScroller
  ) {}

  @HostListener('window:scroll', ['$event']) onScroll() {
    this.pageYoffset = window.pageYOffset;
  }
  scrollToTop() {
    this.scroll.scrollToPosition([0, 0]);
  }

  ngOnInit(): void {
    this.accountService.identity().subscribe(account => {
      if (account) {
        this.settingsForm.patchValue({
          firstName: account.firstName,
          lastName: account.lastName,
          email: account.email,
        });
        this.account = account;
        this.respostaUserQuestionarioService.findAllByUserId().subscribe((response: HttpResponse<IRespostaUserQuestionario[]>) => {
          this.respostasQuestionarios = response.body ?? [];
        });
      }
    });
  }

  save(): void {
    this.success = false;

    this.account.firstName = this.settingsForm.get('firstName')!.value;
    this.account.lastName = this.settingsForm.get('lastName')!.value;
    this.account.email = this.settingsForm.get('email')!.value;

    this.accountService.save(this.account).subscribe(() => {
      this.success = true;
      this.edit = false;
      this.accountService.authenticate(this.account);
    });
  }

  getCracha(resposta: IRespostaUserQuestionario): string {
    let basePath = '../../content/images/badges/';
    let src = basePath;
    let nomefoto = '';
    let cor = '';
    // @ts-ignore
    if (resposta.pontuacao < 50) {
      src += 'no_badge.jpg';
      return src;
    }
    if (resposta) {
      if (resposta.questionario) {
        if (resposta.questionario.id) {
          nomefoto = this.getNomeFoto(resposta.questionario.id);
          if (resposta.pontuacao) {
            cor = this.getBadgeColor(resposta.pontuacao);
          }
          src += nomefoto + '_' + cor + '.jpg';
        }
      }
    }
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

  getNomeFoto(id: number): string {
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
}
