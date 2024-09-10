import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Party} from "../models/party";
import {environment} from "../../environments/environment";
import {Observable} from "rxjs";
import {Buffet} from "../models/buffet";
import {Item} from "../models/item";
import {Order} from "../models/order";

@Injectable({
  providedIn: 'root'
})
export class HttpService {

  constructor(private http: HttpClient) { }

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

}
