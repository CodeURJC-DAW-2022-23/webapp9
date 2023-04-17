import { Routes, RouterModule } from '@angular/router';

import { HomeComponent } from './components/home/home.component';
import { LogInComponent } from './components/log-in/log-in.component';
import { SearchComponent } from './components/search/search.component';
import { ManagementComponent } from './components/management/management.component';

const appRoutes = [
    {path:"", component:HomeComponent},
    {path:"logIn", component:LogInComponent},
    {path:"search", component:SearchComponent},
    {path:'management/:type', component:ManagementComponent}
  ]

  export const routing = RouterModule.forRoot(appRoutes);
