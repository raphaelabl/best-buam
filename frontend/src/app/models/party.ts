import {PartyAdmin} from "./party-admin";

export interface Party {
  id?: number;

  // Generall Event information
  name?: string,
  organization?: string,
  partyAdmin?: PartyAdmin,

  // Date from - to the waiters can access
  startDate?: Date,
  endDate?: Date,

  // Party Admin insight and organization Time
  leadTime?: number,
  followUpTime?: number
}
