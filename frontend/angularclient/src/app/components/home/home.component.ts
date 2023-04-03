import { Component } from '@angular/core';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent {
  constructor() {
    this.nameResult,
    this.topDestinationName,
    this.topPlaceName,
    this.topItineraryName,
    this.descriptionResult

  }

  nameResult = "Name";
  topDestinationName = "Top Destination Name";
  topPlaceName = "Top Place Name";
  topItineraryName = "Top Itinerary Name";
  descriptionResult = "Description"


}
