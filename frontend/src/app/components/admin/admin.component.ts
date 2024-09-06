import {Component, OnInit} from '@angular/core';
import {FormsModule} from "@angular/forms";
import {CommonModule} from "@angular/common";
import {Party} from "../../models/party";
import {HttpClientModule} from "@angular/common/http";
import {HttpService} from "../../services/http.service";

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [
    FormsModule,
    CommonModule
  ],
  templateUrl: './admin.component.html',
  styleUrl: './admin.component.scss'
})
export class AdminComponent implements OnInit{

  constructor(public http: HttpService) {}

  partyList: Party[] = [];

  newParty: Party = {
    partyAdmin: {}
  };

  // NgOnInit Prepare All Data
  ngOnInit(): void {
  }

  // Posting a new Party
  createParty() {

    this.http.postParty(this.newParty).subscribe({
      next: data => {
        this.partyList.push(data);
        this.newParty = {partyAdmin: {}};
      },
      error: err => {
        console.log(err)
      }
    });

  }

  editFest(party: any) {
    this.newParty = party
  }
}
