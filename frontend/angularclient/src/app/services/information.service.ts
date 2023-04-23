import { Observable } from "rxjs";

import { Page } from "../models/rest/page.model";
import { Information } from "../models/information.model";

export interface InformationService {

    getItem(id: number): Observable<any>;

    getList(): Observable<Page<any>>;

    getImage(information: Information): string;

    loadMoreInformation(id: number, page: number): Observable<any>;

}