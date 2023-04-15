import { Information } from "../information.model";
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
    related: Information[],
    reviews: Review[]
}
