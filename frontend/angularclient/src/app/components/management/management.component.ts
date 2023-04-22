import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Information } from 'src/app/models/information.model';
import { Page } from 'src/app/models/rest/page.model';
import { UserDetailsDTO } from 'src/app/models/rest/user-details-dto.model';
import { User } from 'src/app/models/user.model';
import { DestinationMngService } from 'src/app/services/destinationMng.service';
import { InformationMngService } from 'src/app/services/informationMng.service';
import { ItineraryMngService } from 'src/app/services/itineraryMng.service';
import { PlaceMngService } from 'src/app/services/placeMng.service';
import { UserService } from 'src/app/services/user.service';
import { UserMngService } from 'src/app/services/userMng.service';

@Component({
  selector: 'app-management',
  templateUrl: './management.component.html',
  styleUrls: ['./management.component.css'],
})
export class ManagementComponent {
  type!: String;
  items: Information[] = [];
  users: User[] = [];
  service!: InformationMngService;
  userService!: UserMngService;
  uService!: UserService;
  page: number = 0;
  loader: boolean = false;
  currentUser!: UserDetailsDTO;
  admin: boolean = false;

  constructor(
    private activatedRouter: ActivatedRoute,
    private itineraryService: ItineraryMngService,
    private placeService: PlaceMngService,
    private destinationService: DestinationMngService,
    private usrService: UserMngService,
    private usService: UserService
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
    this.uService = this.usService;
  }

  ngOnInit(): void {
    this.checkUser();

    if (this.type != 'user') {
      this.service.getList(this.page).subscribe((response) => {
        response.content.forEach(item => {
          this.items.push(item);
        });
      });
    }

    if (this.type == 'user') {
      this.userService.getList(this.page).subscribe((response) => {
        response.content.forEach(user => {
          this.users.push(user);
        });
      });
    }
  }

  checkUser(){
    this.uService.getMe().subscribe({
      next: (data) =>  {
        this.currentUser = data;
        this.admin = this.currentUser.user.roles.indexOf('ADMIN') !== -1;
        if (this.admin == false){
          window.location.href = "/error/403"
        }
      },
      error: () => window.location.href = "/logIn"
    })
  }

  changeFunc(value: string) {
    window.location.replace("/management/" + value);
  }

  deleteItem(id: number) {
    this.service.deleteItem(id).subscribe(() => {
      window.location.href = `/management/${this.type}`
    });
  }

  deleteUser(id: number) {
    this.service.deleteItem(id).subscribe(() => {
      window.location.href = `/management/${this.type}`
    });
  }

  loadMore() {
    this.loader = true;
    this.page += 1;
    if (this.type != 'user') {
      this.service.getList(this.page).subscribe(
        response => {
          response.content.forEach(item => {
            this.items.push(item);
          });
          this.loader = false;
        },
        error => {
          console.log(error);
          this.loader = false;
        }
      )
    } else {
      this.userService.getList(this.page).subscribe(
        response => {
          response.content.forEach(user => {
            this.users.push(user);
          });
          this.loader = false;
        },
        error => {
          console.log(error);
          this.loader = false;
        }
      )
    }

  }
}
