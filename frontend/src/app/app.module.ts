import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {DashboardComponent} from "./components/dashboard/dashboard.component";
import {AdminComponent} from "./components/admin/admin.component";
import {FormsModule} from "@angular/forms";
import {HttpClientModule} from "@angular/common/http";
import {PartyAdminComponent} from './components/party-admin/party-admin.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MatLegacyDialogModule as MatDialogModule} from "@angular/material/legacy-dialog";
import {BuffetDialogComponent} from './components/_dialog/buffet-dialog/buffet-dialog.component';
import {WaiterComponent} from './components/waiter/waiter.component';
import {BuffetComponent} from './components/buffet/buffet.component';

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
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    BrowserAnimationsModule,
    MatDialogModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
