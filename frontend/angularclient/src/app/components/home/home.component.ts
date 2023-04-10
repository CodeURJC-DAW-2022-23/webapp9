import { Component, OnInit } from '@angular/core';

import { Destination } from 'src/app/models/destination.model';
import { Itinerary } from 'src/app/models/itinerary.model';
import { Place } from 'src/app/models/place.model';
import { DestinationService } from 'src/app/services/destination.service';
import { ItinerariesService } from 'src/app/services/itineraries.service';
import { PlacesService } from 'src/app/services/places.service';


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit{
  destinations!: Destination[]
  places!: Place[]
  itineraries!:Itinerary[]


  constructor(private serviceDest: DestinationService, private servicePlaces: PlacesService, private serviceItineraries: ItinerariesService ) { }

  ngOnInit(): void{
    this.serviceDest.getDestinations().subscribe((data) => {
      this.destinations = data.content;
    });

    this.servicePlaces.getPlaces().subscribe((data) => {
      this.places = data.content
    })

    this.serviceItineraries.getItineraries().subscribe((data) => {
      this.itineraries = data.content;
    });
  }

}
