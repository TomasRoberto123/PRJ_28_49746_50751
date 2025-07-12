import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { mergeMap } from 'rxjs/operators';
import { ActivateService } from './activate.service';
import { MODULOGAMIFICACAO } from 'app/app.constants';

@Component({
  selector: 'jhi-activate',
  templateUrl: './activate.component.html',
})
export class ActivateComponent implements OnInit {
  error = false;
  success = false;
  modulogamificacao = MODULOGAMIFICACAO;

  constructor(private activateService: ActivateService, private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.route.queryParams.pipe(mergeMap(params => this.activateService.get(params.key))).subscribe(
      () => (this.success = true),
      () => (this.error = true)
    );
  }
}
