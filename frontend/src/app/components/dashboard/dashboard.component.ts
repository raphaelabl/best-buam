import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  waiterRequests: {tableNumber: number}[] = [];

  // Daten für das Donut-Diagramm
  salesData: number[] = [30, 50, 20];
  salesLabels: string[] = ['Vorspeisen', 'Hauptgerichte', 'Desserts'];

  // Gesamteinnahmen
  totalRevenue: number = 0;

  constructor() { }

  ngOnInit(): void {
    // Simulierte Daten
    this.waiterRequests = [
      { tableNumber: 5 },
      { tableNumber: 12 }
    ];

    // Simulierte Einnahmenberechnung
    this.calculateTotalRevenue();
  }

  calculateTotalRevenue(): void {
    // Hier würdest du normalerweise die Einnahmen vom Backend abrufen
    this.totalRevenue = 1250.50; // Beispielwert
  }
}
