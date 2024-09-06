import { Routes } from '@angular/router';
import {CreateItemComponent} from "./components/create-item/create-item.component";
import {DashboardComponent} from "./components/dashboard/dashboard.component";

export const routes: Routes = [
  { path: 'create-product', component: CreateItemComponent },
  {path: 'home', component: DashboardComponent},
  { path: '', redirectTo: '/home', pathMatch: 'full' }
];
