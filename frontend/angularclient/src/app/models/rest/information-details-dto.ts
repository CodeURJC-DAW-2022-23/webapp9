import { Destination } from "../destination.model";
import { Itinerary } from "../itinerary.model";
import { Place } from "../place.model";
import { Review } from "../review.model";

export interface InformationDeatilsDTO {
    id: number,
    name: string,
    description: string,
    views: number,
    image: boolean,
    type: string,
    typeLowercase: string,
    flag: string,
    places: Place[],
    itineraries: Itinerary[],
    destinations: Destination[],
    reviews: Review[]
}
