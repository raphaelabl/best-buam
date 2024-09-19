import {Component, OnInit} from '@angular/core';
import {HttpService} from "../../services/http.service";
import {Party} from "../../models/party";
import moment from 'moment';
import {Waiter} from "../../models/waiter";
import {Buffet} from "../../models/buffet";
import {MatDialog} from "@angular/material/dialog";
import {BuffetDialogComponent} from "../_dialog/buffet-dialog/buffet-dialog.component";

@Component({
  selector: 'app-party-admin',
  templateUrl: './party-admin.component.html',
  styleUrls: ['./party-admin.component.scss']
})
export class PartyAdminComponent implements OnInit {

  // Party variables
  planedParties: Party[] = [];
  selectedParty: Party = {partyAdmin: {}, waiters: [], buffets: [{items: [], printers: []}]};

  // Waiter variables
  newWaiter: Waiter = {};
  showUserPw: boolean = false;

  // Buffet variables
  newBuffet: Buffet = {items: [], printers: []};

  constructor(private http: HttpService, public dialog: MatDialog) { }

  ngOnInit(): void {
    this.loadData()
  }

  loadData(){
    // TODO Login Email Address
    this.http.getPartyPerAdmin("jakob.jedinger@icloud.com").subscribe({
      next: data => {
        this.planedParties = data;
        if(data.length === 1){
          this.selectedParty = (data[0].id !== null || data[0] !== undefined || data[0] !== 0)?
            this.planedParties.find(element => element.id === data[0].id)! :
            {
              partyAdmin: {},
              buffets: [],
              waiters: []
            }
        }
      }
    })
  }

  //region Waiter management
  manageWaiters() {
    let index = this.selectedParty.waiters!.findIndex(element => element.id === this.newWaiter.id);

    if(index === null || index === undefined || index === -1) {
      this.selectedParty.waiters = [...this.selectedParty.waiters!,this.newWaiter];
    }else{
      this.selectedParty.waiters![index] = this.newWaiter;
    }

    this.http.postParty(this.selectedParty).subscribe({
      next: data => {
        this.selectedParty = data;
        this.newWaiter = { };
      }
    })
  }

  editWaiter(waiter: number){
   this.newWaiter = {...this.selectedParty.waiters!.find(element => element.id === waiter)!};
  }

  removeWaiter(waiterId: number) {
    this.selectedParty.waiters = this.selectedParty.waiters!.filter(element => waiterId !== element.id)

    this.http.postParty(this.selectedParty).subscribe({
      next: data => {
        this.selectedParty = data;
        this.newWaiter = { };
      }
    })
  }
//endregion

  //region Buffet Management
  manageBuffet(){
    let index = this.selectedParty.buffets!.findIndex(element => element.id === this.newBuffet.id);

    if(index === null || index === undefined || index === -1) {
      this.selectedParty.buffets = [...this.selectedParty.buffets!,this.newBuffet];
    }else{
      this.selectedParty.buffets![index] = this.newBuffet;
    }

    this.http.postParty(this.selectedParty).subscribe({
      next: data => {
        this.selectedParty = data;
        this.newBuffet = { items: [], printers: [] };
      }
    })
  }

  editBuffetInDialog(buffetId: number){

    this.newBuffet = {...this.selectedParty.buffets!.find(element => element.id === buffetId)!};

    const dialogRef = this.dialog.open(
      BuffetDialogComponent,
      {
        height: '90%',
        width: '90%',
        data: this.newBuffet,
      }
    )
  }
  removeBuffet(buffetId: number){
    this.selectedParty.buffets = this.selectedParty.buffets!.filter(element => buffetId !== element.id)

    this.http.postParty(this.selectedParty).subscribe({
      next: data => {
        this.selectedParty = data;
        this.newWaiter = { };
      }
    })
  }
  //endregion



  //region GPT
  salesData = [
    { name: 'Getränk A', count: 150 },
    { name: 'Essen B', count: 80 }
  ];

  printers = [
    { name: 'Drucker 1' },
    { name: 'Drucker 2' }
  ];


  newPrinter = {
    name: ''
  };



  addPrinter() {
    this.printers.push({ ...this.newPrinter });
    // Hier kannst du einen Service aufrufen, um den Drucker zu speichern.
    this.newPrinter = { name: '' };
  }

  removePrinter(printer: any) {
    this.printers = this.printers.filter(p => p !== printer);
    // Hier kannst du einen Service aufrufen, um den Drucker zu löschen.
  }

  testPrint(printer: any) {
    //TODO Testdruck noch machen
  }


  formatDate(toFormatDate: Date | undefined) {
    if(toFormatDate !== null && toFormatDate !== undefined){
      return moment(toFormatDate).format('DD.MMM.YYYY');
    }
    return "";
  }
}

//endregion
