import {Component, OnInit} from '@angular/core';
import {Party} from "../../models/party";
import {HttpService} from "../../services/http.service";

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.scss']
})
export class AdminComponent implements OnInit{

  constructor(public http: HttpService) {}

  partyList: Party[] = [];

  newParty: Party = {
    partyAdmin: {},
    buffets: [],
    waiters: []
  };

  // NgOnInit Prepare All Data
  ngOnInit(): void {
    this.loadData();
  }

  loadData() {
    this.http.getAllParties().subscribe({
      next: data => {
        this.partyList = data;
      },
      error: err => {
        console.log(err)
      }
    })
  }

  // Posting a new Party
  createParty() {

    this.http.postParty(this.newParty).subscribe({
      next: data => {

        let index = this.partyList.findIndex(element => element.id === data.id);

        if(index === null || index === undefined || index === -1) {
          this.partyList = [...this.partyList,data];
        }else{
          this.partyList[index] = data;
        }

        this.newParty = {
          partyAdmin: {},
          buffets: [],
          waiters: []};
      },
      error: err => {
        console.log(err)
      }
    });

  }

  resetParty() {
    this.newParty = {
      partyAdmin: {},
      buffets: [],
      waiters: []
    };
  }

  editFest(party: any) {
    console.log(party)
    this.newParty = {...party, partyAdmin: {...party.partyAdmin}}
  }


}
