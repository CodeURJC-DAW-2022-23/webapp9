import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { Information } from 'src/app/models/information.model';

import { ItineraryService } from 'src/app/services/itinerary.service';
import { PlaceService } from 'src/app/services/place.service';
import { DestinationService } from 'src/app/services/destination.service';

import { InformationService } from 'src/app/services/information.service';
import { InformationDetailsDTO } from 'src/app/models/rest/information-details-dto';
import { UserService } from 'src/app/services/user.service';
import { UserDetailsDTO } from 'src/app/models/rest/user-details-dto.model';
import { Page } from 'src/app/models/rest/page.model';
import { Review } from 'src/app/models/review.model';
import { NgForm } from '@angular/forms';
import { Observable } from 'rxjs';
import { Place } from 'src/app/models/place.model';

@Component({
  selector: 'app-detail',
  templateUrl: './detail.component.html',
  styleUrls: ['./detail.component.css']
})
export class DetailComponent {

  information!: InformationDetailsDTO;
  details!: any;
  service!: InformationService;

  admin: boolean = false;
  registered: boolean = false;
  user!: UserDetailsDTO;
  ownedItinerary: boolean = false;
  toItinerary!: number;

  infoPage: number = -1;
  reviewsPage: number = -1;
  infoLoader: boolean = false;
  reviewsLoader: boolean = false;

  constructor(private activatedRouter: ActivatedRoute,
    private router: Router,
    private itineraryService: ItineraryService,
    private placeService: PlaceService,
    private destinationService: DestinationService,
    private userService: UserService) { }

  ngOnInit(): void {
    this.activatedRouter.url.subscribe((data) => {
      if (data[1].path === "itinerary") {
        this.service = this.itineraryService;
        this.ownedItinerary = true;
      }
      else if (data[1].path === "place") this.service = this.placeService;
      else if (data[1].path === "destination") this.service = this.destinationService;
    });

    const id = this.activatedRouter.snapshot.params['id'];
    this.update(id);

    this.loadUser();
  }

  update(id: number) {
    this.service.getItem(id).subscribe({
      next: (data) => {
        if ("destination" in data) {
          this.information = data["destination"];
          this.information.related = [];
          this.loadMoreInformation();
        }
        else if ("place" in data) {
          this.information = data["place"];
          this.information.related = [];
          this.loadMoreInformation();
        }
        else if ("itinerary" in data) {
          this.information = data["itinerary"];
          this.information.related = [];
          this.loadMoreInformation();
          this.information.reviews = [];
          this.loadMoreReviews();
        }
      },
      error: (error) => {
        this.router.navigate(['/error/', error.status]);
      }
    });
  }

  loadImage(information: Information): string {
    return this.service.getImage(information);
  }

  loadUser() {
    this.userService.getMe().subscribe((data) => {
      this.user = data;
      this.registered = true;
      this.admin = this.user.user.roles.indexOf('ADMIN') !== -1;

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

  copyItinerary(id: number) {
    if (this.service instanceof ItineraryService) this.service.copy(id).subscribe({
      next: (response) => this.router.navigate(['/details/itinerary/', response.id]),
      error: (error) => this.router.navigate(['/error/', error.status])
    });
  }

  loadMoreInformation() {
    this.infoLoader = true;
    this.infoPage += 1;
    this.service.loadMoreInformation(this.information.id, this.infoPage).subscribe({
      next: (response) => {
        if ("places" in response) response.places.content.forEach((information: Information) => {
          this.information.related.push(information);
        });
        else if ("itineraries" in response) response.itineraries.content.forEach((information: Information) => {
          this.information.related.push(information);
        });
        else if (this.information.typeLowercase == "destination") response.content.forEach((information: Information) => {
          this.information.related.push(information);
        });
        this.infoLoader = false;
      },
      error: (error) => {
        this.infoLoader = false;
      }
    })
  }

  loadMoreReviews() {
    this.reviewsLoader = true;
    this.reviewsPage += 1;
    this.itineraryService.loadMoreReviews(this.information.id, this.reviewsPage).subscribe({
      next: (response) => {

        response.content.forEach((review: Review) => {
          this.information.reviews.push(review);
        });
        this.reviewsLoader = false;
      },
      error: (error) => {
        this.reviewsLoader = false;
      }
    })
  }

  getPdfUrl(id: number) {
    if (this.service instanceof ItineraryService)
      window.open(this.service.getPdfUrl(id), "_blank")
  }

  addPlace(itinerary: number) {
    this.itineraryService.addPlace(itinerary, this.information.id).subscribe({
      next: () => this.router.navigate(['/details/itinerary/', itinerary]),
      error: (error) => this.router.navigate(['/error/', error.status])
    });
  }

  addReview(f: NgForm, id: number) {
    if (parseInt(f.value.score) < 0 || parseInt(f.value.score) > 5) return;

    this.itineraryService.addReview(id, {
      title: f.value.title,
      description: f.value.description,
      score: f.value.score,
      user: this.user.user.username
    }).subscribe({
      next: () => this.reloadReviews(),
      error: (error) => this.router.navigate(['/error/', error.status])
    });
  }

  reloadReviews() {
    this.information.reviews = [];

    for (let i = 0; i <= this.reviewsPage; i++) {
      this.itineraryService.loadMoreReviews(this.information.id, i).subscribe({
        next: (response) => {

          response.content.forEach((review: Review) => {
            this.information.reviews.push(review);
          });
        }
      })
    }
  }

  reloadInformation() {
    this.information.related = [];

    for (let i = 0; i <= this.infoPage; i++) {
      this.service.loadMoreInformation(this.information.id, i).subscribe({
        next: (response) => {
          console.log(response)
          if ("places" in response) response.places.content.forEach((information: Information) => {
            this.information.related.push(information);
          });
          else if ("itineraries" in response) response.itineraries.content.forEach((information: Information) => {
            this.information.related.push(information);
          });
          else if (this.information.typeLowercase == "destination") response.content.forEach((information: Information) => {
            this.information.related.push(information);
          });
          this.infoLoader = false;
        },
        error: (error) => {
          console.log(error);
          this.infoLoader = false;
        }
      })
    }
  }

  onDeletion(deleted: boolean) {
    if (deleted) {
      this.reloadInformation();
    }
  }

}
