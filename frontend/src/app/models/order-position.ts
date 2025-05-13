import {Item} from "./item";

export interface OrderPosition {
  id?: number;

  editId?: string;

  item?: Item;
  amount?: number;

  spezialText?: string;
  isSpezial?: boolean;


}
