import {PartyAdmin} from "./party-admin";
import {Waiter} from "./waiter";
import {Buffet} from "./buffet";

export interface Party {
  id?: number;

  // Generall Event information
  name?: string,
  organization?: string,
  partyAdmin?: PartyAdmin,

  waiters: Waiter[];
  buffets: Buffet[];

  // Date from - to the waiters can access
  startDate?: Date,
  endDate?: Date,

  // Party Admin insight and organization Time
  leadTime?: number,
  followUpTime?: number
}
