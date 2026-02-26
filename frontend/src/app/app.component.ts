import {Component, OnInit} from '@angular/core';
import {HttpService} from "./services/http.service";
import {RoleService} from "./services/role.service";
import {KeycloakService} from "keycloak-angular";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit{
  constructor(public roleService: RoleService, private keycloakService: KeycloakService) {
  }

  navLinks: NavData[] = [
    {route: "/admin", name: "Admin-Page", roles: 1},
    {route: "/fest-admin", name: "Fest-Admin-Page", roles: 2},
    {route: "/waiter", name: "Kellner", roles: 3},
    {route: "/buffet", name: "Schanke", roles: 4},
  ]

  public async ngOnInit() {
    this.roleService.loadUserProfile();
  }

  logout() {
    this.keycloakService.logout();
  }
}

interface NavData{route: string, name: string, roles: number}
