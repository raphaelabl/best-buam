import {OrderPosition} from "./order-position";
import {Waiter} from "./waiter";

export interface Order {

  id?: number;

  tableNr?: number;
  waiter: Waiter;

  statusPayed?: boolean;
  statusDelivered?: boolean;

  positions: OrderPosition[];

  // Frontend Variables
  preparationStatus?: number;

}
