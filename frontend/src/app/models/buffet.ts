import {Item} from "./item";
import {Printer} from "./printer";

export interface Buffet {
  id?: number;

  name?: string;

  items: Item[];
  printers: Printer[];

}
