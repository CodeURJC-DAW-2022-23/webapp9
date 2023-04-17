import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { Information } from 'src/app/models/information.model';

import { ItineraryService } from 'src/app/services/itinerary.service';
import { PlaceService } from 'src/app/services/place.service';
import { DestinationService } from 'src/app/services/destination.service';

import { InformationService } from 'src/app/services/information.service';
import { InformationDeatilsDTO } from 'src/app/models/rest/information-details-dto';
import { UserService } from 'src/app/services/user.service';
import { UserDetailsDTO } from 'src/app/models/rest/user-details-dto.model';

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
  user!: UserDetailsDTO;
  ownedItinerary: boolean = false;

  infoPage: number = 0;
  reviewsPage: number = 0;

  constructor(private activatedRouter: ActivatedRoute,
              private router: Router, 
              private itineraryService: ItineraryService, 
              private placeService: PlaceService, 
              private destinationService: DestinationService, 
              private userService: UserService) {
    activatedRouter.url.subscribe((data) => {
      if (data[1].path === "itinerary") {
        this.service = this.itineraryService;
        this.ownedItinerary = true;
      }
      else if (data[1].path === "place") this.service = this.placeService;
      else if (data[1].path === "destination") this.service = this.destinationService;
    });
  }

  ngOnInit(): void {
    const id = this.activatedRouter.snapshot.params['id'];
    this.update(id);

    this.loadUser();
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
    return this.service.getImage(information);
  }

  loadUser() {
    this.userService.getMe().subscribe((data) => {
      this.user = data;

      if (!this.ownedItinerary) {
        return;
      }
      this.ownedItinerary = false; 

      this.user.itineraries.totalPages;

      for (let i = 0; i < this.user.itineraries.totalPages; i++) {
        this.userService.moreItineraries(i).subscribe((request) => {
          request.itineraries.content.forEach((itinerary) => {
            if (i > 0) this.user.itineraries.content.push(itinerary);
            
            if (itinerary.id === this.information.id) {
              this.ownedItinerary = true; 
              return;
            }
          })
        });
      }
    });
  }

}
