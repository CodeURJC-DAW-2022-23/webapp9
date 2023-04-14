import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-management',
  templateUrl: './management.component.html',
  styleUrls: ['./management.component.css']
})
export class ManagementComponent {

  type!: String;

  constructor(private activatedRouter: ActivatedRoute){
    activatedRouter.url.subscribe((data) => {
      if (data[1].path === "itinerary") this.type = "itinerary";
      else if (data[1].path === "place") this.type = "place";
      else if (data[1].path === "destination") this.type = "destination";
      else if (data[1].path === "user") this.type = "user";
    });

  }

}
