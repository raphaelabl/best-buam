import { Component } from '@angular/core';
import { timer } from 'rxjs';
import {KeycloakService} from "keycloak-angular";
import {HttpService} from "../../services/http.service";
import {PreparationDto} from "../../models/dto/preparation-dto";
import {NgClass, NgStyle} from "@angular/common";

@Component({
  selector: 'app-preparation',
  standalone: true,
  imports: [
    NgClass,
    NgStyle
  ],
  templateUrl: './preparation.component.html',
  styleUrl: './preparation.component.scss'
})
export class PreparationComponent {

  timerSource = timer(0, 7500);
  subsribe = this.timerSource.subscribe(val => this.timerSubscription());

  //3 diffrent Colors for the grid
  // Light yellow for beer
  cachelColors: string[] = ['#ffffff','#faf0a1', '#d0ff6c', '#faa8a8', '#9fe4ff'];
  buffetName = ""

  preparations: PreparationDto[] = [];

  constructor(private keycloak: KeycloakService, private http: HttpService) {
    this.buffetName = this.keycloak.getUsername();
  }

  timerSubscription() {
    if(this.buffetName != ""){
      this.http.getPreparationAmounts(this.buffetName).subscribe({
        next: (data) => {
          data.forEach(preparation => {
            let idx = this.preparations.findIndex((p: PreparationDto) => preparation.itemName.toUpperCase() === p.itemName.toUpperCase());

            if(idx !== -1){
              this.preparations[idx].dispatchedAmount = preparation.dispatchedAmount;
              this.preparations[idx].preparationAmount = preparation.preparationAmount;
            } else {
              preparation.color = 0;
              this.preparations.push(preparation);
            }
          })
          console.log(data);
        },
        error: (error) => {
          console.log(error)
        }
      });
    }
  }

  get gridColumns(): string {
    const count = this.preparations.length;

    if (count <= 15) {
      return 'repeat(2, 1fr)';
    }

    if (count <= 20) {
      return 'repeat(3, 1fr)';
    }

    return 'repeat(4, 1fr)';
  }


  cachelClick(itemName: string) {
    let idx = this.preparations.findIndex((p: PreparationDto) => itemName.toUpperCase() === p.itemName.toUpperCase());

    if(idx !== -1){
      if(this.preparations[idx].color < this.cachelColors.length){
        this.preparations[idx].color++;
        console.log(itemName.toUpperCase() + " " + this.preparations[idx].color);

      }else{
        this.preparations[idx].color = 0;
      }
    }
  }
}
