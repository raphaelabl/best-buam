import {Component, OnInit} from '@angular/core';
import {Order} from "../../models/order";
import {WebSocketService} from "../../services/web-socket.service";
import {Subscription} from "rxjs";
import { log } from 'console';
import { BuffetOrderDTO } from 'src/app/models/dto/buffet-order-dto';

@Component({
  selector: 'app-buffet',
  templateUrl: './buffet.component.html',
  styleUrls: ['./buffet.component.scss']
})
export class BuffetComponent implements OnInit{

  buffetName: string = "Kueche";

  orders: BuffetOrderDTO[] = [];

  orderSubscription!: Subscription;

  constructor(private webSocketService: WebSocketService) {
  }

  ngOnInit(): void {

  }

  buffetNameInputed(){
    this.loadData();
  }

  loadData() {
    this.webSocketService.connect(this.buffetName);
    this.orderSubscription = this.webSocketService.getMessages().subscribe({
      next: data => {

        let orderO: BuffetOrderDTO = JSON.parse(data)

        orderO.order.preparationStatus = 0
        this.orders.push(orderO);

      },
      error: err => console.log(err)
    })
  }


  setInProgress(orderId: string) {
    this.orders.find(order => order.id === orderId)!.order.preparationStatus = 1;
  }

  completeOrder(orderId: string) {
    this.orders = this.orders.filter(order => order.id !== orderId);
    this.webSocketService.sendMessage("dispach/"+orderId);
    this.printReceipt(0); // TODO Druck machen
  }

  printReceipt(orderId: number) {
    // Hier wird die Funktion für den Rechnungsdruck implementiert.
  }

}
