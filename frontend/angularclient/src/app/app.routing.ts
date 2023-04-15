import { Routes, RouterModule } from '@angular/router';

import { HomeComponent } from './components/home/home.component';
import { LogInComponent } from './components/log-in/log-in.component';
import { SearchComponent } from './components/search/search.component';
import { SignUpComponent } from './components/sign-up/sign-up.component';

const appRoutes = [
    {path:"", component:HomeComponent},
    {path:"logIn", component:LogInComponent},
    {path:"search", component:SearchComponent},
    {path:"signUp", component:SignUpComponent}
  ]

  export const routing = RouterModule.forRoot(appRoutes);