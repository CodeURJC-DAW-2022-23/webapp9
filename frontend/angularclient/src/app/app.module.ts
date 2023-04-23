import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { FormsModule } from '@angular/forms';

import { ProfileComponent } from './components/profile/profile.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { HomeComponent } from './components/home/home.component';
import { LogInComponent } from './components/log-in/log-in.component';
import { SearchComponent } from './components/search/search.component';
import { DetailComponent } from './components/detail/detail.component';

import { routing } from './app.routing';
import { ManagementComponent } from './components/management/management.component';
import { AddEditMngComponent } from './components/add-edit-mng/add-edit-mng.component';
import { ErrorComponent } from './components/error/error.component';
import { InformationComponent } from './components/information/information.component';
import { SignUpComponent } from './components/sign-up/sign-up.component';
import { MyitinerariesComponent } from './components/myitineraries/myitineraries.component';
import { ReviewComponent } from './components/review/review.component';
import { NgChartsModule } from 'ng2-charts';

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    HomeComponent,
    LogInComponent,
    SignUpComponent,
    SearchComponent,
    SignUpComponent,
    ProfileComponent,
    MyitinerariesComponent,
    DetailComponent,
    InformationComponent,
    ErrorComponent,
    ManagementComponent,
    AddEditMngComponent,
    SignUpComponent,
    ReviewComponent
  ],
  imports: [
    AppRoutingModule,
    BrowserModule,
    HttpClientModule,
    FormsModule,
    NgChartsModule,
    routing
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
