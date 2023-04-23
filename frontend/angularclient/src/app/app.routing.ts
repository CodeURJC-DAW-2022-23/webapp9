import { Routes, RouterModule } from '@angular/router';

import { HomeComponent } from './components/home/home.component';
import { LogInComponent } from './components/log-in/log-in.component';
import { SearchComponent } from './components/search/search.component';
import { ManagementComponent } from './components/management/management.component';
import { AddEditMngComponent } from './components/add-edit-mng/add-edit-mng.component';
import { ErrorComponent } from './components/error/error.component';
import { SignUpComponent } from './components/sign-up/sign-up.component';

const appRoutes = [
  { path: "", component: HomeComponent },
  { path: "logIn", component: LogInComponent },
  { path: "search", component: SearchComponent },
  { path:"signUp", component:SignUpComponent},
  { path: 'management/:type/add', component:AddEditMngComponent },
  { path: 'management/:type/edit/:id', component:AddEditMngComponent },
  { path: 'management/:type', component:ManagementComponent },
  { path: 'error/:id', component: ErrorComponent }
]

  export const routing = RouterModule.forRoot(appRoutes);
