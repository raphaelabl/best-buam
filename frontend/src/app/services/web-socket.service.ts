import { Injectable } from '@angular/core';
import {Subject} from "rxjs";
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class WebSocketService {

  socket!: WebSocket;
  messages: Subject<string> = new Subject<string>();

  constructor() {}

  connect(): void {
    this.socket = new WebSocket(environment.WS_URL + "/order");

    this.socket.onmessage = (event) => {
      this.messages.next(event.data);
    };
  }

  sendMessage(message: string): void {
    if (this.socket && this.socket.readyState === WebSocket.OPEN) {
      this.socket.send(message);
    }
  }

  getMessages() {
    return this.messages.asObservable();
  }

  disconnect(): void {
    if (this.socket) {
      this.socket.close();
    }
  }
}
