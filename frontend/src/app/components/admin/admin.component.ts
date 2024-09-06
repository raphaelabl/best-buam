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
    partyAdmin: {}
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

        var tmp = this.partyList.find(element => element.id === data.id);

        if(tmp === null || tmp === undefined) {
          this.partyList.push(data);
        }else{
          tmp = data;
        }

        this.newParty = {partyAdmin: {}};
      },
      error: err => {
        console.log(err)
      }
    });

  }

  resetParty() {
    this.newParty = {partyAdmin: {}};
  }

  editFest(party: any) {
    this.newParty = party
  }


}
