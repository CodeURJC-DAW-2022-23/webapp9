import { Component, ElementRef, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { DestinationMngService } from '../services/destinationMng.service';
import { InformationMngService } from '../services/informationMng.service';
import { ItineraryMngService } from '../services/itineraryMng.service';
import { PlaceMngService } from '../services/placeMng.service';
import { UserMngService } from '../services/userMng.service';

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

  constructor(private activatedRouter: ActivatedRoute, private itineraryService: ItineraryMngService,
    private placeService: PlaceMngService,
    private destinationService: DestinationMngService,
    private userService: UserMngService) {
    activatedRouter.url.subscribe((data) => {
      this.type = data[1].path;
      this.mode = data[2].path;
      this.id = parseInt(data[3].path);
    });
  }

  ngOnInit(): void {
    if (this.mode=="edit"){
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

  editDestinationInit(){

  }
  editItineraryInit(){

  }
  editPlaceInit(){

  }
  editUserInit(){

  }

  submitDestination(){

    if (this.mode=="add"){
      this.destinationService.createItem(this.itemNameInput.nativeElement.value, this.itemDescriptionInput.nativeElement.value, this.itemFlagCodeInput.nativeElement.value).subscribe(()=> {
        window.location.href="/management/destination"
      })
    }else if (this.mode=="edit"){

    }

  }
  submitItinerary(){
    if (this.mode=="add"){
      this.itineraryService.createItem(this.itemNameInputIt.nativeElement.value, this.itemDescriptionInputIt.nativeElement.value, this.itemUserInput.nativeElement.value).subscribe(()=> {
      window.location.href="/management/itinerary"
    })
    }else if (this.mode=="edit"){

    }
  }
  submitPlace(){
    if (this.mode=="add"){
      this.placeService.createItem(this.itemNameInputPl.nativeElement.value, this.itemDescriptionInputPl.nativeElement.value, this.itemDestinationInput.nativeElement.value).subscribe(()=> {
      window.location.href="/management/place"
    })
    }else if (this.mode=="edit"){

    }
  }
  submitUser(){
    if (this.mode=="add"){
      this.userService.createUser(this.usernameInput.nativeElement.value, this.firstNameInput.nativeElement.value, this.lastNameInput.nativeElement.value, this.userEmailInput.nativeElement.value, this.passwordInput.nativeElement.value, this.nationalityInput.nativeElement.value).subscribe(()=> {
      window.location.href="/management/user"
    })
    }else if (this.mode=="edit"){

    }

  }

}
