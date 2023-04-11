import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { Information } from 'src/app/models/information.model';

import { ItineraryService } from 'src/app/services/itinerary.service';
import { PlaceService } from 'src/app/services/place.service';
import { DestinationService } from 'src/app/services/destination.service';

import { LogInService } from 'src/app/services/log-in.service';
import { InformationService } from 'src/app/services/information.service';
import { UserDetailsDTO } from 'src/app/models/rest/user-details-dto.model';
import { Destination } from 'src/app/models/destination.model';
import { Itinerary } from 'src/app/models/itinerary.model';
import { Place } from 'src/app/models/place.model';
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
    this.service.getItem(id).subscribe((data) => {
      if ("destination" in data) {
        this.information = data["destination"];
        this.information.places = data["places"];
      }
      else if ("place" in data) {
        this.information = data["destination"];
        this.information.itineraries = data["itineraries"];
      }
      else if ("itinerary" in data) {
        this.information = data["destination"];
        this.information.places = data["places"];
        this.information.reviews = data["reviews"];
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
