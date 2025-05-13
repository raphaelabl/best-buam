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

  canActivate(requiredRoles: number[]): boolean{

    console.log("Required:",requiredRoles)

    if(this.roleService.checkPermission(requiredRoles)){

      console.log(this.roleService.getPermissions())
      return true
    
    } else {
      console.log("error")
      console.log(this.roleService.getPermissions());
      

      return false;
    }
  }

}

export const authGuard: CanActivateFn = (route, state) => {
  return inject(AuthService).canActivate(route.data['requiredRoles']);
};
