import { Routes, RouterModule } from '@angular/router';

import { HomeComponent } from './components/home/home.component';
import { LogInComponent } from './components/log-in/log-in.component';
import { SearchComponent } from './components/search/search.component';
import { DetailComponent } from './components/detail/detail.component';
import { ErrorComponent } from './components/error/error.component';

const appRoutes = [
  { path: "", component: HomeComponent },
  { path: "logIn", component: LogInComponent },
  { path: "search", component: SearchComponent },
  { path: 'details/itinerary/:id', component: DetailComponent },
  { path: 'details/place/:id', component: DetailComponent },
  { path: 'details/destination/:id', component: DetailComponent },
  { path: 'error/:id', component: ErrorComponent }
]

export const routing = RouterModule.forRoot(appRoutes);