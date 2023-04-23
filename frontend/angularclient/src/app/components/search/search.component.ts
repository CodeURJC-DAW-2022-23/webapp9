import { Component } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';
import { Information } from 'src/app/models/information.model';
import { DestinationService } from 'src/app/services/destination.service';
import { ItineraryService } from 'src/app/services/itinerary.service';
import { PlaceService } from 'src/app/services/place.service';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent {

  name!: string;
  type!: string;
  sort!: string;
  order!: string;
  page!: number;
  items: Information[] = [];

  constructor(private activatedRoute: ActivatedRoute,
    destinationService: DestinationService,
    itineraryService: ItineraryService,
    placeService: PlaceService) { }

  ngOnInit() {
    this.activatedRoute.queryParams.subscribe((params: Params) => {
      this.name = params['name'] ? params['name'] : "";
      this.type = params['type'] ? params['type'] : "itinerary";
      this.sort = params['sort'] ? params['sort'] : "id";
      this.order = params['order'] ? params['order'] : "DESC";
      this.page = params['page'] ? params['page'] : "0";
    });
  }

}
