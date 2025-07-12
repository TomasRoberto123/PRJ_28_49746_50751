import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from 'app/shared/shared.module';
import { HOME_ROUTE } from './home.route';
import { HomeComponent } from './home.component';
import { CategoriasListComponent } from '../pages/categoria/categorias-list.component';
import { VisitaExploratoriaComponent } from '../pages/visita-exploratoria/visita-exploratoria.component';
import { HistoriasListComponent } from '../pages/historia/historias-list.component';
import { SubtemasListComponent } from '../pages/sub-tema/subtemas-list.component';
import { VisitaLivreComponent } from '../pages/visita-livre/visita-livre.component';
import { AparelhoDetalheComponent } from '../pages/instrumento/aparelho-detalhe.component';
import { GlossarioComponent } from '../pages/glossario/glossario.component';
import { HistoriaDetailsComponent } from '../pages/historia/historia-details.component';
import { QuestionariosListComponent } from '../pages/questionario/questionarios-list.component';
import { QuestionarioPaginaComponent } from '../pages/questionario/questionario-pagina.component';
import { SlideshowModule } from 'ng-simple-slideshow';
import { MdbModule } from 'mdb-angular-ui-kit';
import { BibliografiaTotalComponent } from 'app/pages/bibliografia/bibliografia-total.component';
import { AgradecimentosComponent } from 'app/pages/agradecimentos/agradecimentos.component';

@NgModule({
  imports: [SharedModule, RouterModule.forChild(HOME_ROUTE), SlideshowModule, MdbModule],
  declarations: [
    HomeComponent,
    CategoriasListComponent,
    VisitaExploratoriaComponent,
    HistoriasListComponent,
    SubtemasListComponent,
    VisitaLivreComponent,
    AparelhoDetalheComponent,
    GlossarioComponent,
    HistoriaDetailsComponent,
    QuestionariosListComponent,
    QuestionarioPaginaComponent,
    BibliografiaTotalComponent,
    AgradecimentosComponent,
  ],
})
export class HomeModule {}
