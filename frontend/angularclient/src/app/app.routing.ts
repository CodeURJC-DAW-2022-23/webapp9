import { Routes, RouterModule } from '@angular/router';

import { HomeComponent } from './components/home/home.component';
import { LogInComponent } from './components/log-in/log-in.component';
import { SignUpComponent } from './components/sign-up/sign-up.component';
import { SearchComponent } from './components/search/search.component';
import { DetailComponent } from './components/detail/detail.component';
import { ErrorComponent } from './components/error/error.component';
import { AddEditMngComponent } from './components/add-edit-mng/add-edit-mng.component';
import { ManagementComponent } from './components/management/management.component';
import { MyitinerariesComponent } from './components/myitineraries/myitineraries.component';
import { ProfileComponent } from './components/profile/profile.component';

const appRoutes = [
  { path: "", component: HomeComponent },
  { path: "logIn", component: LogInComponent },
  { path: "signUp", component: SignUpComponent },
  { path: "search", component: SearchComponent },
  { path: 'details/itinerary/:id', component: DetailComponent },
  { path: 'details/place/:id', component: DetailComponent },
  { path: 'details/destination/:id', component: DetailComponent },
  { path: 'management/:type', component:ManagementComponent },
  { path: 'management/:type/edit/:id', component:AddEditMngComponent },
  { path: 'management/:type/add', component:AddEditMngComponent },
  { path: 'error/:id', component: ErrorComponent },
  { path: 'myItineraries', component: MyitinerariesComponent },
  { path: 'profile', component: ProfileComponent }
]

  export const routing = RouterModule.forRoot(appRoutes);
