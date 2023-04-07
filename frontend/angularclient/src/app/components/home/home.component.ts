import { Component, OnInit } from '@angular/core';

import { Destination } from 'src/app/models/destination.model';
import { DestinationService } from 'src/app/services/destination.service';


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit{
  destinations!: Destination[]


  constructor(private service: DestinationService) { }

  ngOnInit(): void{
    this.service.getDestinations().subscribe((data) => {
      this.destinations = data.content;
    });
  }

}
