import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AdminComponent } from './components/admin/admin.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import {PartyAdminComponent} from "./components/party-admin/party-admin.component";
import {WaiterComponent} from "./components/waiter/waiter.component";
import {BuffetComponent} from "./components/buffet/buffet.component";

export const routes: Routes = [
  { path: 'admin', component: AdminComponent},
  { path: 'home', component: DashboardComponent},
  { path: 'waiter', component: WaiterComponent},
  { path: 'fest-admin', component: PartyAdminComponent},
  { path: 'buffet', component: BuffetComponent},
  { path: '', redirectTo: '/home', pathMatch: 'full' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {

}
