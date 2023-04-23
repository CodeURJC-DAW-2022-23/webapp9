import { Itinerary } from "./itinerary.model";
import { User } from "./user.model";

export interface Review {
    id: number,
    title: string,
    description: string,
    views: number,
    score: number,
    date: string,
    itinerary: Itinerary,
    user: User,
    dateToString: string
}