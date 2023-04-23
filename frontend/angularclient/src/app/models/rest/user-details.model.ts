import { Itinerary } from "../itinerary.model";
import { Review } from "../review.model";
import { User } from "../user.model";

export interface UserDetails {
    user: User,
    itineraries: Itinerary[],
    reviews: Review[]
}
