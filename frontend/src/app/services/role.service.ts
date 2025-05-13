import { Injectable } from '@angular/core';
import { log } from 'console';
import {KeycloakService} from "keycloak-angular";
import {KeycloakProfile} from "keycloak-js";

@Injectable({
  providedIn: 'root'
})
export class RoleService {

  roleMap: Map<string, number> = new Map;
  roles: string[] = [];
  public userProfile: KeycloakProfile | null = null;

  constructor(private keycloak: KeycloakService) {

    // Zuteilung der Rollen auf levels(numbers) für einfachere Zurodnung
    //ADMIN also ich hhahahhahah:1
    //Fest-Admin:2
    //Kellner:3
    //Schanken:4

    this.roleMap.set("admin", 1)
    this.roleMap.set("party-admin", 2)
    this.roleMap.set("waiter", 3)
    this.roleMap.set("buffet", 4)

  }

  async initialize(): Promise<void> {
    try {
      console.log("THIS IS INITIALIZED")
      await this.loadUserProfile();
      await this.loadUserRoles();
    } catch (error) {
      console.error('Error initializing RoleService:', error);
    }
  }

  private async loadUserProfile(): Promise<void> {
    try {
      this.userProfile = await this.keycloak.loadUserProfile();
      console.log(this.userProfile)

    } catch (error) {
      console.error('Error loading user profile:', error);
    }
  }

  private async loadUserRoles(): Promise<void> {
    try {
      this.roles = await this.keycloak.getUserRoles();

      console.log("MY USER ROLES")
      console.log(this.roles)

    } catch (error) {
      console.error('Error loading user roles:', error);
    }
  }

  checkPermission(requiredRoles: number[]) {


    return true;

  }

  containsRole(requiredRole: number){
    return this.roles
    .map(role => this.roleMap.get(role))
    .includes(requiredRole);
  }

  getAllUser(){

  }

  getPermissions() {
    return this.roles;
  }

  getEmail(){
    //return "none";
    return this.userProfile!.email;
  }

  getUserName() {
    //return "none"
    return this.userProfile!.username;
  }

  getFirstName(){
    return this.userProfile!.firstName;
  }

  setRoles(roles: string[]) {

    this.roles = roles;
  }

  setProfile(profile: KeycloakProfile) {
    this.userProfile = profile;
  }

}
