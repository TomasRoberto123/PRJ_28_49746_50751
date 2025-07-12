import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';

import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { DataUtils } from 'app/core/util/data-util.service';
import { ENTRY, MODULOGAMIFICACAO } from 'app/app.constants';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit, OnDestroy {
  account: Account | null = null;
  authSubscription?: Subscription;
  isLoading: boolean | undefined;
  video = false;
  entry = ENTRY.entry;
  modulogamificacao = MODULOGAMIFICACAO;

  constructor(private accountService: AccountService, private router: Router, protected dataUtils: DataUtils) {}

  ngOnInit(): void {
    // this.authSubscription = this.accountService.getAuthenticationState().subscribe(account => (this.account = account));
  }

  isAuthenticated(): boolean {
    return this.accountService.isAuthenticated();
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  ngOnDestroy(): void {
    if (this.authSubscription) {
      this.authSubscription.unsubscribe();
    }
    ENTRY.entry = false;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  goToVisitaExploratoria(): void {
    this.router.navigate(['/visita-exploratoria']);
  }

  goToVisitaLivre(): void {
    this.router.navigate(['/visita-livre']);
  }

  setEntry() {
    ENTRY.entry = false;
    this.entry = ENTRY.entry;
  }
}
