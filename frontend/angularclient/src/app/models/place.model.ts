import { Destination } from "./destination.model"

export interface Place {
    id: number,
    name: string,
    description: string,
    views: number,
    image: boolean,
    destination: Destination,
    type: string,
    typeLowercase: string,
    flag: string 
}
