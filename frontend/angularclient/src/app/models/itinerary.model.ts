import { Information } from "./information.model"
import { User } from "./user.model"

export interface Itinerary extends Information {
    user: User,
    public: boolean
}
