import { Injectable } from '@angular/core';
import {BehaviorSubject, Subject} from "rxjs";
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class WebSocketService {

  socket!: WebSocket;
  messages: Subject<string> = new Subject<string>();

  private connectionStatus = new BehaviorSubject<boolean>(false);

  private heartbeatInterval: any;
  private heartbeatTimeout: any;

  constructor() {}

  connect(name: string): void {
    this.socket = new WebSocket(environment.WS_URL + "order/"+name);

    this.socket.onopen = () => {
      console.log('WebSocket connection established');
      this.connectionStatus.next(true);
      this.startHeartbeat();
    }

    this.socket.onmessage = (event) => {
      if(event.data === "pong"){
        console.log("pong");
        clearTimeout(this.heartbeatTimeout);
        return;
      }

      this.messages.next(event.data);
    };

    this.socket.onclose = () => {
      console.log('WebSocket connection closed');
      this.connectionStatus.next(false);
      this.stopHeartbeat();
    }

    this.socket.onerror = (err) => {
      this.connectionStatus.next(false);
      console.error("WebSocket error", err);
    };

  }

  getConnectionStatus() {
    return this.connectionStatus.asObservable();
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
    this.stopHeartbeat();
    this.connectionStatus.next(false);

    if (this.socket) {
      this.socket.close();
    }
  }

  // Heartbeat
  private startHeartbeat() {
    this.heartbeatInterval = setInterval(() => {
        if(this.socket.readyState !== WebSocket.OPEN) return;
        console.log("ping");
        this.socket.send("ping");
        this.heartbeatTimeout = setTimeout(() => {
          console.log("timeout");
          this.connectionStatus.next(false);
          this.socket.close();
        }, 10000);
    }, 20000)
  }

  private stopHeartbeat() {
    clearInterval(this.heartbeatInterval);
    clearTimeout(this.heartbeatTimeout);
  }
}
