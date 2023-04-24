import { Component } from '@angular/core';
import { NgForm } from '@angular/forms';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { Information } from 'src/app/models/information.model';
import { Page } from 'src/app/models/rest/page.model';
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
    private placeService: PlaceService,
    private router: Router) {
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

        this.items = [];
        this.service.search(this.name, this.type, this.sort, this.order, this.page).subscribe({
          next: (response: Page<Information>) => {
            response.content.forEach(i => this.items.push(i));
          }
        })
      });
    }

  ngOnInit() {

  }

  getImage(i: Information): string {
    return this.service.getImage(i)
  }

  search(f: NgForm) {
    this.name = f.value.name ? f.value.name : "";
    this.type = f.value.type ? f.value.type : "itinerary";
    this.sort = f.value.sort ? f.value.sort : "id";
    this.order = f.value.order ? f.value.order : "DESC";
    this.page = f.value.page ? f.value.page : "0";

    this.router.navigate(["/search"], { queryParams: { name: f.value.name, type: this.type, sort: this.sort, order: this.order, page: this.page } });
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
        this.loader = false;
      }
    )
  }

}


