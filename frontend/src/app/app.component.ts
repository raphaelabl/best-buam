import {Component, OnInit} from '@angular/core';
import {HttpService} from "./services/http.service";
import {RoleService} from "./services/role.service";
import {KeycloakService} from "keycloak-angular";
import {Router} from "@angular/router";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit{
  constructor(public roleService: RoleService, private keycloakService: KeycloakService, public router: Router) {
  }

  navLinks: NavData[] = [
    {route: "/admin", name: "Admin-Page", roles: 1},
    {route: "/fest-admin", name: "Fest-Admin-Page", roles: 2},
    {route: "/waiter", name: "Kellner", roles: 3},
    {route: "/buffet", name: "Schanke", roles: 4},
    {route: "/preparation", name: "Vorbereitung", roles: 4},
  ]

  public async ngOnInit() {
    this.roleService.loadUserProfile()
  }

  logout() {
    this.keycloakService.logout();
  }
}

interface NavData{route: string, name: string, roles: number}
