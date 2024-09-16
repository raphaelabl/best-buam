import { Order } from "../order";

export interface BuffetOrderDTO {
    id: string;
    order: Order;
    done: boolean;
}
