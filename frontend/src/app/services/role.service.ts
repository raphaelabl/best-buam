import {inject, Injectable} from '@angular/core';
import {KeycloakProfile} from "keycloak-js";
import {KeycloakService} from "keycloak-angular";

@Injectable({
  providedIn: 'root'
})
export class RoleService {

  roleMap: Map<string, number> = new Map;
  roles: number[] = [];
  keycloakProfile?: KeycloakProfile = undefined;
  keycloakService: KeycloakService = inject(KeycloakService);

  constructor() {

    this.roleMap.set("Not Found", -1)
    this.roleMap.set("admin", 1)
    this.roleMap.set("party-admin", 2)
    this.roleMap.set("waiter", 3)
    this.roleMap.set("buffet", 4)

  }

  logout(){
    this.keycloakService.logout();
  }

  isLoggedIn(){
    return !!this.keycloakProfile;
  }

  async loadUserProfile() {
    if(this.keycloakService.isLoggedIn()){
      this.keycloakProfile = await this.keycloakService.loadUserProfile();
    }
  }

  checkPermission(requiredRoles: number[]) {
    console.log(requiredRoles);
    console.log(this.roles);
    if(this.roles && this.roles.length === 0){
      return false;
    }

   return requiredRoles.some(role => this.roles.includes(role));
}

  containsRole(requiredRole: number){
    const token = this.keycloakService.getKeycloakInstance()?.tokenParsed;

    if(!token) return false;

    const clientRoles: string[] = token.resource_access?.['bestbuam-frontend']?.roles ?? [];
    this.roles = clientRoles.map(role => this.roleMap.get(role) || -1);

    return this.roles.includes(requiredRole);
  }

  getPermissions() {
    return this.roles;
  }

  getEmail(){
    return this.keycloakProfile!.email;
  }

  getUserName() {
    return this.keycloakProfile!.username;
  }

  getFirstName(){
    return this.keycloakProfile!.firstName;
  }


}
