import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA} from "@angular/material/dialog";
import {Buffet} from "../../../models/buffet";
import {Item} from "../../../models/item";
import {HttpService} from "../../../services/http.service";
import {Printer} from "../../../models/printer";

@Component({
  selector: 'app-buffet-dialog',
  templateUrl: './buffet-dialog.component.html',
  styleUrls: ['./buffet-dialog.component.scss']
})
export class BuffetDialogComponent implements OnInit {
  newBuffet?: Buffet = {items: [], printers: []};

  newItem?: Item = {};

  newPrinter?: Printer = {}

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: Buffet,
    private http: HttpService
  ){
    this.newBuffet = data;
    console.log(this.newBuffet)
  }

  ngOnInit(): void {

  }

//region Item Management
  manageItem(){
    let index = this.newBuffet!.items!.findIndex(element => element.id === this.newItem!.id);

    if(index === null || index === undefined || index === -1) {
      this.newBuffet!.items! = [...this.newBuffet!.items!,this.newItem!];
    }else{
      this.newBuffet!.items![index] = this.newItem!;
    }

    this.http.postBuffet(this.newBuffet!).subscribe({
      next: data => {
        this.newBuffet = data;
        this.newItem = { };
      }
    })
  }

  editItem(itemId: number){
    this.newItem = {...this.newBuffet!.items.find(element => element.id === itemId)!};
  }

  removeItem(itemId: number) {
    this.newBuffet!.items = this.newBuffet!.items!.filter(element => itemId !== element.id)

    this.http.postBuffet(this.newBuffet!).subscribe({
      next: data => {
        this.newBuffet = data;
        this.newItem = { };
      }
    })
  }
  //endregion


//region Printer Management
  managePrinter(){
    let index = this.newBuffet!.printers!.findIndex(element => element.id === this.newPrinter!.id);

    if(index === null || index === undefined || index === -1) {
      this.newBuffet!.printers! = [...this.newBuffet!.printers!,this.newPrinter!];
    }else{
      this.newBuffet!.printers![index] = this.newPrinter!;
    }

    this.http.postBuffet(this.newBuffet!).subscribe({
      next: data => {
        this.newBuffet = data;
        this.newPrinter = { };
      }
    })
  }

  editPrinter(printerId: number){
    this.newPrinter = {...this.newBuffet!.printers.find(element => element.id === printerId)!};
  }

  removePrinter(printerId: number) {
    this.newBuffet!.printers = this.newBuffet!.printers!.filter(element => printerId !== element.id)

    this.http.postBuffet(this.newBuffet!).subscribe({
      next: data => {
        this.newBuffet = data;
        this.newPrinter = { };
      }
    })
  }


  testPrinter(id: number) {
    console.log("Test Test")
  }
  //endregion


}
