import {Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {HttpService} from "../../services/http.service";
import {Party} from "../../models/party";
import {Buffet} from "../../models/buffet";
import {OrderPosition} from "../../models/order-position";
import {Order} from "../../models/order";
import {MatDialog} from "@angular/material/dialog";
import {RoleService} from "../../services/role.service";

@Component({
  selector: 'app-waiter',
  templateUrl: './waiter.component.html',
  styleUrls: ['./waiter.component.scss']
})
export class WaiterComponent implements OnInit {

  currentParty: Party = {waiters: [], buffets: []};
  selectedBuffet: Buffet = {items: [], printers: []};

  newOrder: Order = {positions: [], waiter: {}};
  specialOrderPosition: OrderPosition = { item: {}, amount: 0}

  // Seperate Payment
  cloneArticles: OrderPosition[] = [];
  separateBill: OrderPosition[] = []
  payTogether: boolean = true;
  selectedSeparateBill: boolean = false;

  changeSteps: number[] = [0.50,1,2,5,10,20,50,100];
  paidAmount = 0;

  @ViewChild('editOrderTemplate') editOrderTemplate!: TemplateRef<any>;
  @ViewChild('payPopUp') payPopUp!: TemplateRef<any>;

  constructor(public dialog: MatDialog, private http: HttpService, public roleService: RoleService) { }

  ngOnInit(): void {
    this.loadAllData();
  }

  loadAllData() {
    this.http.getPartyByWaiter(this.roleService.getUserName()!).subscribe({
      next: data => {
        this.currentParty = data;
        if(this.currentParty.buffets.length > 0){
          this.selectedBuffet = this.currentParty.buffets[0];
        }
      }
    })
  }

  //region OrderManagement
  addToOrder(orderPosition: OrderPosition) {

    if(orderPosition.editId === null || orderPosition.editId === undefined){
     orderPosition.editId = orderPosition.item!.id!.toString();
    }
    const index = this.newOrder!.positions.findIndex(element => element.editId === orderPosition.editId);

    if(index === null || index === undefined || index === -1){
      this.newOrder!.positions.push({item: orderPosition.item, amount: 1, editId: orderPosition.item!.id!.toString()});
    }else{
      this.newOrder!.positions[index].amount! += 1;
    }

  }

  specialOrder(order: OrderPosition) {

    if(order.editId === null || order.editId === undefined){
    this.specialOrderPosition.item = order.item;
    this.specialOrderPosition.amount = 1;
    }else{
      this.specialOrderPosition = {...order};
    }

    const dialogRef = this.dialog.open(this.editOrderTemplate);

    dialogRef.afterClosed().subscribe(() => {

      if(this.specialOrderPosition.editId === null || this.specialOrderPosition.editId === undefined || this.specialOrderPosition.editId === ""){
        this.specialOrderPosition.editId = this.getNextSpezialId(this.specialOrderPosition.item!.id!.toString());
        this.specialOrderPosition.isSpezial = true;
        this.newOrder!.positions.push({...this.specialOrderPosition});
        this.specialOrderPosition = {};
      }else{
        let index = this.newOrder!.positions.findIndex(element => element.editId === this.specialOrderPosition.editId);

        if(index !== null && index !== undefined && index !== -1){
          this.newOrder!.positions[index] = {...this.specialOrderPosition};
          this.specialOrderPosition = {}
        }
      }
    });
  }

  getNextSpezialId(itemId: string): string {
    const currentId = "S_"+itemId;

    if(this.newOrder!.positions.findIndex(element => currentId === element.editId) === -1){
      return currentId;
    }

    return this.getNextSpezialId(currentId);
  }

  removeOrderPostion(editId: string){
    let op = this.newOrder!.positions.find(element => element.editId! === editId);

    if(op!.amount! > 1){
      op!.amount! -= 1
      return;
    }

    this.newOrder!.positions = this.newOrder!.positions.filter(element => element.editId !== editId);
  }

  closeDialogs() {
    this.dialog.closeAll();
  }

  //endregion


  //region PayManagement  payPopUp
  openDialog(){
    let dialogref = this.dialog.open(this.payPopUp);

    dialogref.afterClosed().subscribe(() => {
      if(!this.payTogether){
        this.payTogether = true;
      }
      this.separateBill = [];
    });
  }

  getTotal(order: OrderPosition[]) {
    let test = order.map(element => element.item!.price! * element.amount!).reduce((accumulator, currentValue) => accumulator + currentValue, 0);

    test.toFixed(2)
    return test;
  }

  getChangeArray(order : OrderPosition[]) {
    let change: number[] = [0,0,0,0,0,0,0,0];
    let total: number = this.paidAmount - this.getTotal(order);

    while(total >= 0.5){
      for (let i = this.changeSteps.length - 1; i >= 0; i--){
        if(total - this.changeSteps[i] >= 0){
          total -= this.changeSteps[i];
          change[i]++;
          break;
        }
      }
    }

    return change;
  }

  switchPaymentOption(){
    this.payTogether = !this.payTogether;
    if(!this.payTogether){
      this.cloneArticles = structuredClone(this.newOrder.positions)
    }else if(this.payTogether){
      this.cloneArticles = []
      this.separateBill = [];
    }

    console.log(this.cloneArticles);
  }


  //Seperate Payment

  addToBill(orderPosition: OrderPosition){

    console.log(orderPosition)

    const oldIndex = this.cloneArticles.findIndex(element => element.editId === orderPosition.editId);
    if(oldIndex === null || oldIndex === undefined || oldIndex === -1){
      return;
    }else{
      if(this.cloneArticles[oldIndex].amount! > 0){
        const seperateIndex = this.separateBill.findIndex(element => element.editId === orderPosition.editId);

        if(seperateIndex === null || seperateIndex === undefined || seperateIndex === -1){
          this.separateBill.push({item: orderPosition.item, amount: 1, editId: orderPosition.editId, isSpezial: orderPosition.isSpezial, spezialText: orderPosition.spezialText});
        }else{
          this.separateBill[seperateIndex].amount! += 1;
        }

        this.cloneArticles[oldIndex].amount! -= 1;
      }
    }

    console.log(this.separateBill)
  }

  removeFromBill(orderPosition: OrderPosition){
    let op = this.separateBill!.find(element => element.editId! === orderPosition.editId);

    if(op!.amount! > 1){
      op!.amount! -= 1
    }else{
      this.separateBill = this.separateBill.filter(element => element.editId !== orderPosition.editId);
    }


    const oldIndex = this.cloneArticles.findIndex(element => element.editId === orderPosition.editId);
    if(oldIndex === null || oldIndex === undefined || oldIndex === -1){
      return;
    }
    this.cloneArticles[oldIndex].amount! += 1;

  }

  nextSeparate(){
    if(this.getTotal(this.cloneArticles) == 0){
      this.completePayment();
    }
    this.separateBill = [];
    this.selectedSeparateBill = false;
  }


  completePayment() {

    this.newOrder.statusPayed = true;

    this.http.postOrder(this.newOrder).subscribe({
        next: data => {
          this.closeDialogs()

          this.newOrder = {positions: [], waiter: {}};
          this.paidAmount = 0;
          this.specialOrderPosition = {}

        },
        error: err => {
          console.log(err);
        }
      });
  }

  //endregion






}
