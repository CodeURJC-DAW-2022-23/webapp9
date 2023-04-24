import { Component, OnInit, ViewChild } from '@angular/core';
import { ItinerariesService } from 'src/app/services/itineraries.service';
import { LogInService } from 'src/app/services/log-in.service';
import { UserService } from 'src/app/services/user.service';
import { Itinerary } from 'src/app/models/itinerary.model';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { User } from 'src/app/models/user.model';

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
  page: number = 0;
  isPublic: boolean | undefined = undefined;
  isEditing: boolean = false;
  user: User | undefined;

  @ViewChild('userFile') userFile: any;
  imgError: boolean = false;
  loader: boolean = false;

  constructor(private router: Router, private logInService: LogInService, private itineraryService: ItinerariesService, private userService: UserService) {   }

  ngOnInit() {
    this.userService.getMe().subscribe({
      next: (response: any) => {
        this.loadMyItineraries();
      },
      error: (err) => {
        window.location.href = "/error/" + err.status;
      }
    });
  }

  afterViewInit() {
    this.userService.getMe().subscribe({
      next: (response: any) => {
        this.loadMyItineraries();
      },
      error: (err) => {
        window.location.href = "/error/" + err.status;
      }
    });
  }

  loadMyItineraries() {
    this.itineraryService.getUserItineraries(this.page).subscribe((response) => {
      response.content.forEach(item => {
        this.items.push(item);
        console.log(item);
      })
    });
    this.loader = false;
  }

  onSubmit() {
    const name = (<HTMLInputElement>document.getElementById('nameField')).value;
    const description = (<HTMLInputElement>document.getElementById('descriptionField')).value;
    const publicValue  = (<HTMLInputElement>document.getElementById('publicValue')).checked;
  
    const newItinerary: any = {};
    newItinerary.name = name.trim();
    newItinerary.description = description.trim();
    newItinerary.publicValue = publicValue.valueOf();
  
    if (this.logInService.isLogged()) {
      this.userService.addUserItinerary(JSON.stringify(newItinerary)).subscribe({
        next: (response: any) => {
          window.location.reload();
          this.id = response.id;
          const img = this.userFile.nativeElement.file[0];
          if (img) {
            let formData = new FormData();
            formData.append("imageFile", img);
            this.itineraryService.setItineraryImage(this.id, formData).subscribe({
              next: () => {
                window.location.reload();
              }, error: (err) => {
                if (err.status != 200) {
                  window.location.href = "/error/" + err.status;
                  window.location.reload();
                }
              }
            });
          }
        }
      });
    }
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
        window.location.reload();
      },
      error: (err) => {
        if (err.status != 404) {
          console.error('Error: ' + JSON.stringify(err));
          window.location.href = "/error/" + err.status;
        }
      }
    });
  }

  onEdit() {
    const name = (<HTMLInputElement>document.getElementById('editNameField')).value;
    const description = (<HTMLInputElement>document.getElementById('editDescriptionField')).value;
    const isPrivate = (<HTMLInputElement>document.getElementById('editPrivacyField')).checked;

    const newItinerary: any = {};
    newItinerary.name = name.trim();
    newItinerary.description = description.trim();
    newItinerary.isPrivate = isPrivate.valueOf();

    if (this.logInService.isLogged()) this.userService.editUserItinerary(this.id, JSON.stringify(newItinerary)).subscribe({
      next: (response: any) => {
        window.location.reload();
        console.log("response was:");
        console.log(response);
        const img = this.userFile.nativeElement.file[0];
        this.isEditing = false;
        if (img) {
          let formData = new FormData();
          formData.append("imageFile", img);
          this.itineraryService.setItineraryImage(this.id, formData).subscribe({
            next: (response: any) => {
              this.isEditing = false;
              window.location.reload();
            },
            error: (err) => {
              if (err.status != 200) this.router.navigate(['/error/' + err.status]);
            }
          });
        }
      },
      error: (err) => {
        console.error("Error when making new itinerary; " + JSON.stringify(err));
      }
    });
  }

  loadMore() {
    this.loader = true;
    this.page += 1;
    this.loadMyItineraries();
  }
}
