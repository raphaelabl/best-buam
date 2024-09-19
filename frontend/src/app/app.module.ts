import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {DashboardComponent} from "./components/dashboard/dashboard.component";
import {AdminComponent} from "./components/admin/admin.component";
import {FormsModule} from "@angular/forms";
import { provideHttpClient, withInterceptorsFromDi } from "@angular/common/http";
import {PartyAdminComponent} from './components/party-admin/party-admin.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {BuffetDialogComponent} from './components/_dialog/buffet-dialog/buffet-dialog.component';
import {WaiterComponent} from './components/waiter/waiter.component';
import {BuffetComponent} from './components/buffet/buffet.component';
import {MatDialogModule} from "@angular/material/dialog";

@NgModule({
  declarations: [
      AppComponent,
      DashboardComponent,
      AdminComponent,
      PartyAdminComponent,
      BuffetDialogComponent,
      WaiterComponent,
      BuffetComponent,
  ],
  bootstrap: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    BrowserAnimationsModule,
    MatDialogModule
  ],
  providers: [
    provideHttpClient(withInterceptorsFromDi())
  ]
})
export class AppModule { }
