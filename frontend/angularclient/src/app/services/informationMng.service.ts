import { Observable } from "rxjs";

import { Page } from "../models/rest/page.model";
import { Information } from "../models/information.model";

export interface InformationMngService {

  getList(page: number): Observable<Page<Information>>;

  deleteItem(id: number): any;
}
