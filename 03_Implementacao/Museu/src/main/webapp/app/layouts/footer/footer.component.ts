import { Component } from '@angular/core';
import { MODULOGAMIFICACAO } from 'app/app.constants';

@Component({
  selector: 'jhi-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.scss'],
})
export class FooterComponent {
  modulogamificacao = MODULOGAMIFICACAO;
}
