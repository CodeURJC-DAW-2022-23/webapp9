import { Component } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';
import { Information } from 'src/app/models/information.model';
import { DestinationService } from 'src/app/services/destination.service';
import { InformationService } from 'src/app/services/information.service';
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
  service!: InformationService;
  loader: boolean = false;

  constructor(private activatedRoute: ActivatedRoute,
    private destinationService: DestinationService,
    private itineraryService: ItineraryService,
    private placeService: PlaceService) {
  }

  ngOnInit() {
    this.activatedRoute.queryParams.subscribe((params: Params) => {
      this.name = params['name'] ? params['name'] : "";
      this.type = params['type'] ? params['type'] : "itinerary";
      this.sort = params['sort'] ? params['sort'] : "id";
      this.order = params['order'] ? params['order'] : "DESC";
      this.page = params['page'] ? params['page'] : "0";
      if (this.type == 'destination') {
        this.service = this.destinationService
      } else if (this.type == 'itinerary') {
        this.service = this.itineraryService
      } else if (this.type == 'place') {
        this.service = this.placeService
      }
    });

  }

  getImage(i: Information): string {
    return this.service.getImage(i)
  }

  loadMore() {
    this.loader = true;
    this.page += 1;
    this.service.search(this.name, this.type, this.sort, this.order, this.page).subscribe(
      response => {
        response.content.forEach(item => {
          this.items.push(item);
        });
        this.loader = false;
      },
      error => {
        console.log(error);
        this.loader = false;
      }
    )
  }
}


