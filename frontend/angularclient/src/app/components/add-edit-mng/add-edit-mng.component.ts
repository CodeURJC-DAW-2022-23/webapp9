import { Component, ElementRef, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { DestinationMngService } from '../../services/destinationMng.service';
import { ItineraryMngService } from '../../services/itineraryMng.service';
import { PlaceMngService } from '../../services/placeMng.service';
import { UserMngService } from '../../services/userMng.service';
import { UserService } from 'src/app/services/user.service';
import { UserDetailsDTO } from 'src/app/models/rest/user-details-dto.model';

@Component({
  selector: 'app-add-edit-mng',
  templateUrl: './add-edit-mng.component.html',
  styleUrls: ['./add-edit-mng.component.css']
})
export class AddEditMngComponent {

  type!: string;
  mode!: string;
  id!: number;
  name: string = "";
  description: string = "";
  flagCode: string = "";
  itemUser: string = "";
  itemDestination: string = "";
  username: string = "";
  firstName: string = "";
  lastName: string = "";
  email: string = "";
  nationality: string = "";
  currentUser!: UserDetailsDTO;
  admin: boolean = false;
  @ViewChild('passwordInput') passwordInput!: ElementRef;
  @ViewChild('itemNameInput') itemNameInput!: ElementRef;
  @ViewChild('itemDescriptionInput') itemDescriptionInput!: ElementRef;
  @ViewChild('itemFlagCodeInput') itemFlagCodeInput!: ElementRef;
  @ViewChild('itemUserInput') itemUserInput!: ElementRef;
  @ViewChild('itemDestinationInput') itemDestinationInput!: ElementRef;
  @ViewChild('usernameInput') usernameInput!: ElementRef;
  @ViewChild('firstNameInput') firstNameInput!: ElementRef;
  @ViewChild('lastNameInput') lastNameInput!: ElementRef;
  @ViewChild('userEmailInput') userEmailInput!: ElementRef;
  @ViewChild('nationalityInput') nationalityInput!: ElementRef;
  @ViewChild('itemNameInputIt') itemNameInputIt!: ElementRef;
  @ViewChild('itemDescriptionInputIt') itemDescriptionInputIt!: ElementRef;
  @ViewChild('itemNameInputPl') itemNameInputPl!: ElementRef;
  @ViewChild('itemDescriptionInputPl') itemDescriptionInputPl!: ElementRef;
  @ViewChild('destinationFile') destinationFile: any;
  @ViewChild('itineraryFile') itineraryFile: any;
  @ViewChild('placeFile') placeFile: any;
  @ViewChild('userFile') userFile: any;

  constructor(private activatedRouter: ActivatedRoute, private router: Router, private itineraryService: ItineraryMngService,
    private placeService: PlaceMngService,
    private destinationService: DestinationMngService,
    private userService: UserMngService,
    private uService: UserService) {
    activatedRouter.url.subscribe((data) => {
      this.type = data[1].path;
      this.mode = data[2].path;
      this.id = parseInt(data[3].path);
    });
  }

  ngOnInit(): void {
    this.checkUser();

    if (this.mode == "edit") {
      switch (this.type) {
        case "destination":
          this.editDestinationInit();
          break;
        case "itinerary":
          this.editItineraryInit();
          break;
        case "place":
          this.editPlaceInit();
          break;
        case "user":
          this.editUserInit();
          break;
        default:
          break;
      }
    }
  }

  checkUser() {
    this.uService.getMe().subscribe({
      next: (data) => {
        this.currentUser = data;
        this.admin = this.currentUser.user.roles.indexOf('ADMIN') !== -1;
        if (this.admin == false) {
          this.router.navigate(['/error/403']);
        }
      },
      error: () => this.router.navigate(['/logIn'])
    })
  }

  editDestinationInit() {
    this.destinationService.getItem(this.id).subscribe((dest) => {
      this.name = dest.destination.name;
      this.description = dest.destination.description;
      this.flagCode = dest.destination.flagCode;
    })
  }

  editItineraryInit() {
    this.itineraryService.getItem(this.id).subscribe((itin) => {
      this.name = itin.itinerary.name;
      this.description = itin.itinerary.description;
      this.itemUser = itin.itinerary.user.username;
    })
  }
  editPlaceInit() {
    this.placeService.getItem(this.id).subscribe((place) => {
      this.name = place.place.name;
      this.description = place.place.description;
      this.itemDestination = place.place.destination.name;
    })
  }
  editUserInit() {
    this.userService.getUser(this.id).subscribe((user) => {
      this.username = user.username;
      this.firstName = user.firstName;
      this.lastName = user.lastName;
      this.email = user.email;
      this.nationality = user.nationality;
    })
  }

  submitDestination() {

    if (this.mode == "add") {
      this.destinationService.createItem(this.itemNameInput.nativeElement.value, this.itemDescriptionInput.nativeElement.value, this.itemFlagCodeInput.nativeElement.value).subscribe({
        next: (data) => {
          const image = this.destinationFile.nativeElement.files[0];
          if (image) {
            let formData = new FormData();
            formData.append("imageFile", image);
            this.destinationService.editImage(data.id, formData).subscribe({
              next: () => this.redirect(),
              error: (error) => {
                if (error.status == 200) {
                  this.redirect()
                } else {
                  this.router.navigate(['/error/', error.status])
                }
              },
            })
          }
        },
        error: (error) => {
          this.router.navigate(['/error/', error.status])
        }
      })

    } else if (this.mode == "edit") {
      this.destinationService.editItem(this.id, this.itemNameInput.nativeElement.value, this.itemDescriptionInput.nativeElement.value, this.itemFlagCodeInput.nativeElement.value).subscribe({
        next: (data) => {
          this.redirect();
            const image = this.destinationFile.nativeElement.files[0];
            if (image) {
              let formData = new FormData();
              formData.append("imageFile", image);
              this.destinationService.editImage(this.id, formData).subscribe({
                next: () => this.redirect(),
                error: (error) => {
                  if (error.status == 200) {
                    this.redirect()
                  } else {
                    this.router.navigate(['/error/', error.status])
                  }
                },
              })
          }
        },
        error: (error) => {
          this.router.navigate(['/error/', error.status])
        }
      })
    }

  }
  submitItinerary() {
    if (this.mode == "add") {
      this.itineraryService.createItem(this.itemNameInputIt.nativeElement.value, this.itemDescriptionInputIt.nativeElement.value, this.itemUserInput.nativeElement.value).subscribe({
        next: (data) => {
          const image = this.itineraryFile.nativeElement.files[0];
          if (image) {
            let formData = new FormData();
            formData.append("imageFile", image);
            this.itineraryService.editImage(data.id, formData).subscribe({
              next: () => this.redirect(),
              error: (error) => {
                if (error.status == 200) {
                  this.redirect()
                } else {
                  this.router.navigate(['/error/', error.status])
                }
              },
            })
          }
        },
        error: (error) => {
          this.router.navigate(['/error/', error.status])
        }
      })
    } else if (this.mode == "edit") {
      this.itineraryService.editItem(this.id, this.itemNameInputIt.nativeElement.value, this.itemDescriptionInputIt.nativeElement.value, this.itemUserInput.nativeElement.value).subscribe({
        next: (data) => {
          this.redirect();
          const image = this.itineraryFile.nativeElement.files[0];
          if (image) {
            let formData = new FormData();
            formData.append("imageFile", image);
            this.itineraryService.editImage(this.id, formData).subscribe({
              next: () => this.redirect(),
              error: (error) => {
                if (error.status == 200) {
                  this.redirect()
                } else {
                  this.router.navigate(['/error/', error.status])
                }
              },
            })
          }
        },
        error: (error) => {
          this.router.navigate(['/error/', error.status])
        }
      })
    }
  }
  submitPlace() {
    if (this.mode == "add") {
      this.placeService.createItem(this.itemNameInputPl.nativeElement.value, this.itemDescriptionInputPl.nativeElement.value, this.itemDestinationInput.nativeElement.value).subscribe({
        next: (data) => {
          const image = this.placeFile.nativeElement.files[0];
          if (image) {
            let formData = new FormData();
            formData.append("imageFile", image);
            this.placeService.editImage(data.id, formData).subscribe({
              next: () => this.redirect(),
              error: (error) => {
                if (error.status == 200) {
                  this.redirect()
                } else {
                  this.router.navigate(['/error/', error.status])
                }
              },
            })
          }
        },
        error: (error) => {
          this.router.navigate(['/error/', error.status])
        }
      })
    } else if (this.mode == "edit") {
      this.redirect();
      this.placeService.editItem(this.id, this.itemNameInputPl.nativeElement.value, this.itemDescriptionInputPl.nativeElement.value, this.itemDestinationInput.nativeElement.value).subscribe({
        next: (data) => {
          const image = this.placeFile.nativeElement.files[0];
          if (image) {
            let formData = new FormData();
            formData.append("imageFile", image);
            this.placeService.editImage(this.id, formData).subscribe({
              next: () => this.redirect(),
              error: (error) => {
                if (error.status == 200) {
                  this.redirect()
                } else {
                  this.router.navigate(['/error/', error.status])
                }
              },
            })
          }
        },
        error: (error) => {
          this.router.navigate(['/error/', error.status])
        }
      })
    }
  }
  submitUser() {
    if (this.mode == "add") {
      this.userService.createUser(this.usernameInput.nativeElement.value, this.firstNameInput.nativeElement.value, this.lastNameInput.nativeElement.value, this.userEmailInput.nativeElement.value, this.passwordInput.nativeElement.value, this.nationalityInput.nativeElement.value).subscribe({
        next: (data) => {
          const image = this.userFile.nativeElement.files[0];
          if (image) {
            let formData = new FormData();
            formData.append("imageFile", image);
            this.userService.editImage(data.id, formData).subscribe({
              next: () => this.redirect(),
              error: (error) => {
                if (error.status == 200) {
                  this.redirect()
                } else {
                  this.router.navigate(['/error/', error.status])
                }
              },
            })
          }
        },
        error: (error) => {
          this.router.navigate(['/error/', error.status])
        }
      })
    } else if (this.mode == "edit") {
      this.redirect();
      this.userService.editUser(this.id, this.usernameInput.nativeElement.value, this.firstNameInput.nativeElement.value, this.lastNameInput.nativeElement.value, this.userEmailInput.nativeElement.value, this.nationalityInput.nativeElement.value).subscribe({
        next: (data) => {
          const image = this.userFile.nativeElement.files[0];
          if (image) {
            let formData = new FormData();
            formData.append("imageFile", image);
            this.userService.editImage(this.id, formData).subscribe({
              next: () => this.redirect(),
              error: (error) => {
                if (error.status == 200) {
                  this.redirect()
                } else {
                  this.router.navigate(['/error/', error.status])
                }
              },
            })
          }
        },
        error: (error) => {
          this.router.navigate(['/error/', error.status])
        }
      })
    }

  }

  redirect() {
    this.router.navigate(['/management/', this.type]);
  }

}
