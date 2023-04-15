import { Component, Input } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Information } from 'src/app/models/information.model';
import { DestinationService } from 'src/app/services/destination.service';
import { InformationService } from 'src/app/services/information.service';
import { ItineraryService } from 'src/app/services/itinerary.service';
import { PlaceService } from 'src/app/services/place.service';

@Component({
  selector: 'app-information',
  templateUrl: './information.component.html',
  styleUrls: ['./information.component.css']
})
export class InformationComponent {

  @Input() information!: Information;
  service!: InformationService;

  constructor(private activatedRouter: ActivatedRoute,
              private itineraryService: ItineraryService, 
              private placeService: PlaceService, 
              private destinationService: DestinationService) {
    activatedRouter.url.subscribe((data) => {
      if (this.information.typeLowercase === "itinerary") this.service = this.itineraryService;
      else if (this.information.typeLowercase === "place") this.service = this.placeService;
      else if (this.information.typeLowercase === "destination") this.service = this.destinationService;
    });
  }

  loadImage(information: Information): string {
    return this.service.getImage(information);
  }

}
