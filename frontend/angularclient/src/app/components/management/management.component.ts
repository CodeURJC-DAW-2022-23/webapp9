import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Information } from 'src/app/models/information.model';
import { Page } from 'src/app/models/rest/page.model';
import { User } from 'src/app/models/user.model';
import { DestinationMngService } from 'src/app/services/destinationMng.service';
import { InformationMngService } from 'src/app/services/informationMng.service';
import { ItineraryMngService } from 'src/app/services/itineraryMng.service';
import { PlaceMngService } from 'src/app/services/placeMng.service';
import { UserMngService } from 'src/app/services/userMng.service';

@Component({
  selector: 'app-management',
  templateUrl: './management.component.html',
  styleUrls: ['./management.component.css'],
})
export class ManagementComponent {
  type!: String;
  items!: Page<Information>;
  users!: Page<User>;
  service!: InformationMngService;
  userService!: UserMngService;
  page: number = 0;

  constructor(
    private activatedRouter: ActivatedRoute,
    private itineraryService: ItineraryMngService,
    private placeService: PlaceMngService,
    private destinationService: DestinationMngService,
    private usrService: UserMngService
  ) {
    activatedRouter.url.subscribe((data) => {
      this.type = data[1].path;
      if (data[1].path === 'itinerary') {
        this.service = this.itineraryService;
      } else if (data[1].path === 'place') {
        this.service = this.placeService;
      } else if (data[1].path === 'destination') {
        this.service = this.destinationService;
      } else if (data[1].path === 'user') {
        this.userService = this.usrService;
      }
    });
  }

  ngOnInit(): void {
    if (this.type != 'user') {
      this.service.getList(this.page).subscribe((response) => {
        this.items = response;
      });
    }

    if (this.type == 'user') {
      this.userService.getList(this.page).subscribe((response) => {
        this.users = response;
      });
    }
  }

  changeFunc(value: string) {
    window.location.replace("/management/" + value);
  }

  deleteItem(id: number){
    this.service.deleteItem(id).subscribe(() => {
      window.location.href = `/management/${this.type}`
    });
  }

  deleteUser(id: number){
    this.service.deleteItem(id).subscribe(() => {
      window.location.href = `/management/${this.type}`
    });
  }
}
