import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { Information } from 'src/app/models/information.model';

import { ItineraryService } from 'src/app/services/itinerary.service';
import { PlaceService } from 'src/app/services/place.service';
import { DestinationService } from 'src/app/services/destination.service';

import { LogInService } from 'src/app/services/log-in.service';
import { InformationService } from 'src/app/services/information.service';
import { UserDetailsDTO } from 'src/app/models/rest/user-details-dto.model';
import { InformationDeatilsDTO } from 'src/app/models/rest/information-details-dto';

@Component({
  selector: 'app-detail',
  templateUrl: './detail.component.html',
  styleUrls: ['./detail.component.css']
})
export class DetailComponent {

  information!: InformationDeatilsDTO;
  details!: any;
  service!: InformationService;

  admin: boolean = false;
  registered: boolean = false;
  user: UserDetailsDTO | undefined = undefined;

  constructor(private activatedRouter: ActivatedRoute,
              private router: Router, 
              private itineraryService: ItineraryService, 
              private placeService: PlaceService, 
              private destinationService: DestinationService, 
              private logInService: LogInService) {
    activatedRouter.url.subscribe((data) => {
      if (data[1].path === "itinerary") this.service = this.itineraryService;
      else if (data[1].path === "place") this.service = this.placeService;
      else if (data[1].path === "destination") this.service = this.destinationService;
    });

    // Set user attributes if log in.
  }

  ngOnInit(): void {
    const id = this.activatedRouter.snapshot.params['id'];
    this.update(id);
  }

  update(id: number) {
    this.service.getItem(id).subscribe({
      next: (data) => {
        if ("destination" in data) {
          this.information = data["destination"];
          this.information.related = data["places"].content;
        }
        else if ("place" in data) {
          this.information = data["place"];
          this.information.related = data["itineraries"].content;
        }
        else if ("itinerary" in data) {
          this.information = data["itinerary"];
          this.information.related = data["places"].content;
          this.information.reviews = data["reviews"].content;
        }
      }, 
      error: (error) => {
        window.location.href = `/error/${error.status}`;
      }
    });
  }

  loadImage(information: Information): string {
    if (information.typeLowercase === "destination") return this.service.getImage(information);
    if (information.typeLowercase === "place") return this.service.getImage(information);
    if (information.typeLowercase === "itinerary") return this.service.getImage(information);
    return "";
  }

}
