import {Component, OnInit} from '@angular/core';
import {Order} from "../../models/order";
import {WebSocketService} from "../../services/web-socket.service";
import {Subscription} from "rxjs";
import { log } from 'console';
import { BuffetOrderDTO } from 'src/app/models/dto/buffet-order-dto';
import {RoleService} from "../../services/role.service";
import {HttpService} from "../../services/http.service";

@Component({
  selector: 'app-buffet',
  templateUrl: './buffet.component.html',
  styleUrls: ['./buffet.component.scss']
})
export class BuffetComponent implements OnInit{

  buffetName: string = "";

  orders: BuffetOrderDTO[] = [];

  orderSubscription!: Subscription;

  connectionStatus = true;

  constructor(private webSocketService: WebSocketService, public roleService: RoleService, private http: HttpService) {
  }

  ngOnInit(): void {
    this.loadData();
    this.connectionObserver();
  }

  public connectionObserver() {
    this.webSocketService.getConnectionStatus().subscribe(status => {
      this.connectionStatus = status;
    });
  }

  loadData() {
    this.buffetName = this.roleService.getUserName()!;

    console.log(this.roleService.getUserName())

    this.webSocketService.connect(this.buffetName);
    this.orderSubscription = this.webSocketService.getMessages().subscribe({
      next: data => {

        console.log(data)
        let orderO: BuffetOrderDTO = JSON.parse(data)

        orderO.order.preparationStatus = 0
        this.orders.push(orderO);

      },
      error: err => console.log(err)
    })
  }


  completeOrder(orderId: string) {
    var o = this.orders.find(order => order.id === orderId)!.order;
    if(o.preparationStatus == 0){
      o.preparationStatus = 1;
    }else{
      // IF ORDER is Depatched right -> this.orders = this.orders.filter(order => order.id !== orderId);
      this.http.dispatchOrder(orderId).subscribe({
        next: data => {
          this.orders = this.orders.filter(order => order.id !== orderId);
        },
        error: err => console.log(err)
      });
      //this.webSocketService.sendMessage("dispach/"+orderId);
    }
  }


}
