import {inject, Injectable} from '@angular/core';
import {CanActivateFn, Router} from "@angular/router";
import {RoleService} from "./role.service";
import { log } from 'console';
import { KeycloakService } from 'keycloak-angular';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private roleService: RoleService, private router: Router, private keycloakService: KeycloakService) { }

  async canActivate(requiredRoles: number[]){

    if(!await this.roleService.checkPermission(requiredRoles)){
      this.router.navigate(['/home']);
    }
    return true
  }

}

export const authGuard: CanActivateFn = (route, state) => {
  return inject(AuthService).canActivate(route.data['requiredRoles']);
};
