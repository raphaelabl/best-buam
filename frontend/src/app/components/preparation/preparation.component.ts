import { Component } from '@angular/core';
import { timer } from 'rxjs';
import {KeycloakService} from "keycloak-angular";
import {HttpService} from "../../services/http.service";
import {PreparationDto} from "../../models/dto/preparation-dto";
import {NgClass} from "@angular/common";

@Component({
  selector: 'app-preparation',
  standalone: true,
  imports: [
    NgClass
  ],
  templateUrl: './preparation.component.html',
  styleUrl: './preparation.component.scss'
})
export class PreparationComponent {

  timerSource = timer(0, 7500);
  subsribe = this.timerSource.subscribe(val => this.timerSubscription());

  buffetName = ""

  preparations: PreparationDto[] = [];

  constructor(private keycloak: KeycloakService, private http: HttpService) {
    this.buffetName = this.keycloak.getUsername();
  }

  timerSubscription() {
    if(this.buffetName != ""){
      this.http.getPreparationAmounts(this.buffetName).subscribe({
        next: (data) => {
          this.preparations = data;
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

}
