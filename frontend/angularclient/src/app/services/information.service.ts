import { Observable } from "rxjs";

import { Page } from "../models/rest/page.model";

export interface InformationService {

    getItem(id: number): Observable<any>;

    getList(): Observable<Page<any>>;

}