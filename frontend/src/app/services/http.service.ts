import {Injectable} from '@angular/core';
import { HttpClient } from "@angular/common/http";
import {Party} from "../models/party";
import {environment} from "../../environments/environment";
import {Observable} from "rxjs";
import {Buffet} from "../models/buffet";
import {Order} from "../models/order";
import {Waiter} from "../models/waiter";
import {PartyAdmin} from "../models/party-admin";
import {RoleService} from "./role.service";

@Injectable({
  providedIn: 'root'
})
export class HttpService {

  constructor(private http: HttpClient, private roleService: RoleService) {
  }

  // Party Resources
  postParty(party: Party): Observable<Party> {
    return this.http.post<Party>(environment.API_URL + "party", party)
  }

  getAllParties(): Observable<Party[]>{
    return this.http.get<Party[]>(environment.API_URL + "party");
  }

  getPartyById(partyId: number): Observable<Party> {
    return this.http.get<Party>(environment.API_URL + "party/id" , {params: {id: partyId}})
  }


  getPartyByWaiter(userName: string) {
    return this.http.get<Party>(environment.API_URL + "party/user", {params: {userName: userName}});
  }

  getPartyPerAdmin(adminEmail: string): Observable<Party[]>{
    return this.http.get<Party[]>(environment.API_URL + "party/adminEmail", {params: {adminEmail: adminEmail}});
  }

  // Buffet Resources
  postBuffet(newBuffet: Buffet): Observable<Buffet> {
    return this.http.post<Buffet>(environment.API_URL + "buffet", newBuffet)
  }


  // Order Resource
  postOrder(newOrder: Order): Observable<Order>{
    return this.http.post<Order>(environment.API_URL + "order", newOrder)
  }

  getOrders(): Observable<Order[]>{
    return this.http.get<Order[]>(environment.API_URL + "order");
  }

  dispatchOrder(orderId: string): Observable<string> {
    return this.http.get<string>(environment.API_URL + "order/dispatch", {params: {orderId: orderId, buffetName: this.roleService.getUserName()||""}});
  }

  // Keycloak
  postKeycloakPartyAdmin(partyAdmin: PartyAdmin): Observable<string> {
    return this.http.post<string>(environment.API_URL + "keycloak/party-admin", partyAdmin);
  }

  postKeycloakWaiter(waiter: Waiter): Observable<string> {
    return this.http.post<string>(environment.API_URL + "keycloak/waiter", waiter);
  }

  postKeycloakBuffet(buffet: Buffet): Observable<string> {
    return this.http.post<string>(environment.API_URL + "keycloak/buffet", buffet);
  }

  deleteKeycloakPartyAdmin(partyAdminUsername: string): Observable<string> {
    return this.http.delete<string>(environment.API_URL + "keycloak/party-admin/"+partyAdminUsername);
  }

  deleteKeycloakWaiter(waiterUsername: string): Observable<string> {
    return this.http.delete<string>(environment.API_URL + "keycloak/waiter/"+ waiterUsername);
  }

  deleteKeycloakBuffet(buffetLogin: string): Observable<string> {
    return this.http.delete<string>(environment.API_URL + "keycloak/buffet"+ buffetLogin);
  }

}
