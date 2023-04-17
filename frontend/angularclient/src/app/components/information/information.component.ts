import { Component, Input } from '@angular/core';

import { Information } from 'src/app/models/information.model';
import { UserDetailsDTO } from 'src/app/models/rest/user-details-dto.model';
import { DestinationService } from 'src/app/services/destination.service';
import { InformationService } from 'src/app/services/information.service';
import { ItineraryService } from 'src/app/services/itinerary.service';
import { LogInService } from 'src/app/services/log-in.service';
import { PlaceService } from 'src/app/services/place.service';

@Component({
  selector: 'app-information',
  templateUrl: './information.component.html',
  styleUrls: ['./information.component.css']
})
export class InformationComponent {

  @Input() information!: Information;
  @Input() owned!: boolean;

  service!: InformationService;

  registered: boolean = false;
  user: UserDetailsDTO | undefined = undefined;

  constructor(private itineraryService: ItineraryService, 
              private placeService: PlaceService, 
              private destinationService: DestinationService, 
              private userService: UserService) { }

  ngOnInit() {
    if (this.information.typeLowercase === "itinerary") this.service = this.itineraryService;
    else if (this.information.typeLowercase === "place") this.service = this.placeService;
    else if (this.information.typeLowercase === "destination") this.service = this.destinationService;

    this.loadUser();
  }

  loadImage(information: Information): string {
    return information.flag;
  }

  loadUser() {
    this.userService.getMe().subscribe((data) => {
      if (this.fromItinerary === -1) return;

      for (let i = 0; i < data.itineraries.totalPages; i++) {
        this.userService.moreItineraries(i).subscribe((request) => {
          request.itineraries.content.forEach((itinerary) => {
            if (itinerary.id === this.fromItinerary) {
              this.owned = true; 
              return;
            }
          })
        });
      }
    });
  }

}
