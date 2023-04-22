import { RouterModule, Routes } from '@angular/router';

import { HomeComponent } from './components/home/home.component';
import { LogInComponent } from './components/log-in/log-in.component';
import { SearchComponent } from './components/search/search.component';
import { SignUpComponent } from './components/sign-up/sign-up.component';
import { ProfileComponent } from './components/profile/profile.component';
import { AuthGuardService } from './services/auth-guard.service';
import { MyitinerariesComponent } from './components/myitineraries/myitineraries.component';

const routes: Routes = [
  { path: "profile", component: ProfileComponent, canLoad: [AuthGuardService] },
  { path:"", component:HomeComponent },
  { path:"logIn", component:LogInComponent },
  { path:"search", component:SearchComponent },
  { path:"signUp", component:SignUpComponent },
  { path:"myItineraries", component:MyitinerariesComponent }
]

export const routing = RouterModule.forRoot(routes);
