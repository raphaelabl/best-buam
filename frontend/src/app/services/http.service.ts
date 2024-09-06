import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Party} from "../models/party";
import {environment} from "../../environments/environment";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class HttpService {

  constructor(private http: HttpClient) { }

  // Party Resources
  postParty(party: Party): Observable<Party> {
    return this.http.post(environment.API_URL + "party", party)
  }

  getAllParties(): Observable<Party[]>{
    return this.http.get<Party[]>(environment.API_URL + "party");
  }

}
