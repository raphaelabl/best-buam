import { Component } from '@angular/core';
import {FormsModule} from "@angular/forms";

@Component({
  selector: 'app-create-item',
  standalone: true,
  imports: [
    FormsModule
  ],
  templateUrl: './create-item.component.html',
  styleUrl: './create-item.component.scss'
})
export class CreateItemComponent {
  product = {
    name: '',
    price: 0,
    category: '',
  };

  onSubmit() {
    console.log('Produkt erstellt:', this.product);

    this.product = { name: '', price: 0, category: '' };
  }
}
