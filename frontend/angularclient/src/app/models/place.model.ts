import { Destination } from "./destination.model"
import { Information } from "./information.model"

export interface Place extends Information {
    destination: Destination
}
