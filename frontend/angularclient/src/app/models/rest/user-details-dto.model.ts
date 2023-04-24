import { Itinerary } from "../itinerary.model";
import { Review } from "../review.model";
import { User } from "../user.model";
import { Page } from "./page.model";

export interface UserDetailsDTO {
    user: User,
    itineraries: Page<Itinerary>,
    reviews: Page<Review>
}
