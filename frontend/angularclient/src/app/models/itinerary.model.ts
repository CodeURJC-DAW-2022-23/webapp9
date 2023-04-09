import { User } from "./user.model"

export interface Itinerary {
        id: number,
        name: string,
        description: string,
        views: number,
        image: boolean,
        user: User,
        public: boolean,
        type: string,
        typeLowercase: string,
        flag: string 
}
