import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Information } from 'src/app/models/information.model';
import { User } from 'src/app/models/user.model';
import { DestinationMngService } from 'src/app/services/destinationMng.service';
import { InformationMngService } from 'src/app/services/informationMng.service';
import { ItineraryMngService } from 'src/app/services/itineraryMng.service';
import { LogInService } from 'src/app/services/log-in.service';
import { PlaceMngService } from 'src/app/services/placeMng.service';
import { UserMngService } from 'src/app/services/userMng.service';

@Component({
  selector: 'app-management',
  templateUrl: './management.component.html',
  styleUrls: ['./management.component.css']
})
export class ManagementComponent {

  type!: String;
  items!: Information[];
  users!: User[];
  service!: InformationMngService;

  constructor(private activatedRouter: ActivatedRoute,
              private itineraryService: ItineraryMngService,
              private placeService: PlaceMngService,
              private destinationService: DestinationMngService,
              private userService: UserMngService,
              private logInService: LogInService){
    activatedRouter.url.subscribe((data) => {
      if (data[1].path === "itinerary"){
        this.type = "itinerary";
        this.service = this.itineraryService;
      }
      else if (data[1].path === "place"){
        this.type = "place";
        this.service = this.placeService;
      }
      else if (data[1].path === "destination"){
        this.type = "destination";
        this.service = this.destinationService;
      }
      else if (data[1].path === "user"){
        this.type = "user";
        this.service = this.userService;
      }
    });

  }

}
