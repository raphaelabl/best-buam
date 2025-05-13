import {inject, Injectable} from '@angular/core';
import {CanActivateFn, Router} from "@angular/router";
import {RoleService} from "./role.service";

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private initialized = false;

  constructor(private roleService: RoleService, private router: Router) {
    this.initialize();
  }

  private async initialize() {
    await this.roleService.initialize();
    this.initialized = true;
  }

  async canActivate(requiredRoles: number[]): Promise<boolean> {
    if (!this.initialized) {
      await this.initialize();
    }

    return this.roleService.checkPermission(requiredRoles);
  }
}

export const authGuard: CanActivateFn = (route, state) => {
  return inject(AuthService).canActivate(route.data['requiredRoles']);
};
