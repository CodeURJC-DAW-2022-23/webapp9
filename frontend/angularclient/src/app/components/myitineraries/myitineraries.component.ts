import { Component, OnInit, ViewChild } from '@angular/core';
import { ItinerariesService } from 'src/app/services/itineraries.service';
import { LogInService } from 'src/app/services/log-in.service';
import { UserService } from 'src/app/services/user.service';
import { Itinerary } from 'src/app/models/itinerary.model';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-myitineraries',
  templateUrl: './myitineraries.component.html',
  styleUrls: ['./myitineraries.component.css']
})
export class MyitinerariesComponent implements OnInit {
  items: Itinerary[] = [];
  id: number = -1;
  name: String = "";
  description: String = "";
  isPublic: boolean | undefined = undefined;
  isEditing: boolean = false;

  @ViewChild('userFile') userFile: any;

  constructor(private router: Router, private logInService: LogInService, private itineraryService: ItinerariesService, private userService: UserService) {   }

  ngOnInit() {
    this.logInService.reqIsLogged();
    if (!this.logInService.isLogged()) window.location.href = "/error/403";
    else this.loadMyItineraries();
  }

  loadMyItineraries() {
    this.itineraryService.getUserItineraries().subscribe((response) => {
      response.content.forEach(item => {
        this.items.push(item);
      })
    });
  }

  onSubmit() {
    const name = (<HTMLInputElement>document.getElementById('nameField')).value;
    const description = (<HTMLInputElement>document.getElementById('descriptionField')).value;
    const isPrivate = (<HTMLInputElement>document.getElementById('privacyField')).checked;
  
    const newItinerary: any = {};
    newItinerary.name = name.trim();
    newItinerary.description = description.trim();
    newItinerary.isPrivate = isPrivate.valueOf();
  
    if (this.logInService.isLogged()) {
      this.userService.addUserItinerary(JSON.stringify(newItinerary)).subscribe({
        next: (response: any) => {
          this.id = response.id;
          const img = this.userFile.nativeElement.file[0];
          if (img) {
            let formData = new FormData();
            formData.append("imageFile", img);
            this.itineraryService.setItineraryImage(this.id, formData).subscribe({
              next: () => {
                window.location.href = "/myItineraries";
              }, error: (err) => {
                if (err.status == 400) window.location.href = "/error/400";
              }
            });
          } else {
            window.location.href = "/myItineraries";
          }
        }
      });
    }
    window.location.href = "/myItineraries";
  }

  editItinerary(id: number, name: String, description: String, isPublic: boolean) {
    this.isEditing = true;
    this.id = id;
    this.name = name;
    this.description = description;
    this.isPublic = isPublic;
  }

  deleteItinerary(id: number) {
    this.itineraryService.deleteItineraryById(id).subscribe({
      next: (response: any) => {
        console.log("response was:");
        console.log(response);
      },
      error: (err) => {
        if (err.status != 404) {
          console.error('Error when asking if logged: ' + JSON.stringify(err));
          window.location.href = "/error/" + err.status;
        }
      }
    });
    window.location.href = "/myItineraries";
  }

  onEdit() {
    const name = (<HTMLInputElement>document.getElementById('editNameField')).value;
    const description = (<HTMLInputElement>document.getElementById('editDescriptionField')).value;
    const isPrivate = (<HTMLInputElement>document.getElementById('editPrivacyField')).checked;

    const newItinerary: any = {};
    newItinerary.name = name.trim();
    newItinerary.description = description.trim();
    newItinerary.isPrivate = isPrivate.valueOf();

    if (this.logInService.isLogged()) this.userService.editUserItinerary(this.id, JSON.stringify(newItinerary));
    const img = this.userFile.nativeElement.file[0];
    if (img) {
      let formData = new FormData();
      formData.append("imageFile", img);
      this.itineraryService.setItineraryImage(this.id, formData).subscribe();
    }

    this.isEditing = false;
    window.location.href = "/myItineraries";
  }
}
