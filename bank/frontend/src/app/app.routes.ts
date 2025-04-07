import { Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { MessagesComponent } from './components/messages/messages.component';
import { PartnersComponent } from './components/partners/partners.component';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'messages', component: MessagesComponent },
  { path: 'partners', component: PartnersComponent },
  { path: '**', redirectTo: '' }
];