import { Routes } from '@angular/router';
import { HomeComponent } from 'app/home/home.component';
import { VisitaLivreComponent } from 'app/pages/visita-livre/visita-livre.component';
import { CategoriasListComponent } from 'app/pages/categoria/categorias-list.component';
import { SubtemasListComponent } from 'app/pages/sub-tema/subtemas-list.component';
import { AparelhoDetalheComponent } from 'app/pages/instrumento/aparelho-detalhe.component';
import { GlossarioComponent } from 'app/pages/glossario/glossario.component';
import { VisitaExploratoriaComponent } from 'app/pages/visita-exploratoria/visita-exploratoria.component';
import { HistoriasListComponent } from 'app/pages/historia/historias-list.component';
import { HistoriaDetailsComponent } from 'app/pages/historia/historia-details.component';
import { QuestionariosListComponent } from 'app/pages/questionario/questionarios-list.component';
import { QuestionarioPaginaComponent } from 'app/pages/questionario/questionario-pagina.component';
import { BibliografiaTotalComponent } from 'app/pages/bibliografia/bibliografia-total.component';
import { AgradecimentosComponent } from 'app/pages/agradecimentos/agradecimentos.component';

export const HOME_ROUTE: Routes = [
  {
    path: '',
    component: HomeComponent,
    data: { pageTitle: 'Museu' },
  },
  { path: 'visita-livre', component: VisitaLivreComponent, data: { pageTitle: 'Museu - Visitar' } },
  { path: 'categorias', component: CategoriasListComponent, data: { pageTitle: 'Museu - Categorias' } },
  { path: ':idCategoria/subs', component: SubtemasListComponent, data: { pageTitle: 'Museu - Subtemas' } },
  { path: 'apa/:idAparelho', component: AparelhoDetalheComponent, data: { pageTitle: 'Museu - Instrumento' } },
  { path: 'glossario', component: GlossarioComponent, data: { pageTitle: 'Museu - Glossário' } },
  { path: 'visita-exploratoria', component: VisitaExploratoriaComponent, data: { pageTitle: 'Museu - Explorar' } },
  { path: 'historias', component: HistoriasListComponent, data: { pageTitle: 'Museu - História' } },
  { path: 'historias/:idHistoria', component: HistoriaDetailsComponent, data: { pageTitle: 'Museu - História' } },
  { path: 'questionarios', component: QuestionariosListComponent, data: { pageTitle: 'Museu - Quiz' } },
  { path: 'questionarios/:idQuestionario', component: QuestionarioPaginaComponent, data: { pageTitle: 'Museu - Quiz' } },
  { path: 'bibliografias', component: BibliografiaTotalComponent, data: { pageTitle: 'Museu - Bibliografia' } },
  { path: 'agradecimentos', component: AgradecimentosComponent, data: { pageTitle: 'Museu - Ficha Técnica' } },
];
