import {Component, OnInit} from '@angular/core';
import {Order} from "../../models/order";
import {WebSocketService} from "../../services/web-socket.service";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-buffet',
  templateUrl: './buffet.component.html',
  styleUrls: ['./buffet.component.scss']
})
export class BuffetComponent implements OnInit{

  orders: Order[] = [];

  orderSubscription!: Subscription;

  constructor(private webSocketService: WebSocketService) {
  }

  ngOnInit(): void {
    this.loadData();
  }

  loadData() {
    this.webSocketService.connect();
    this.orderSubscription = this.webSocketService.getMessages().subscribe({
      next: data => {
        this.orders.push(JSON.parse(data))
      },
      error: err => console.log(err)
    })
  }


  setInProgress(orderId: number) {
    this.orders.find(order => order.id === orderId)!.preparationStatus = 1;
  }

  completeOrder(orderId: number) {
    this.orders.find(order => order.id === orderId)!.preparationStatus = 2;
    this.printReceipt(0); // TODO Druck machen
  }

  printReceipt(orderId: number) {
    // Hier wird die Funktion für den Rechnungsdruck implementiert.
  }

}
