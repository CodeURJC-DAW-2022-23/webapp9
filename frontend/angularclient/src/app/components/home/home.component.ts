import { Component, OnInit } from '@angular/core';
import { ChartConfiguration, ChartData, ChartType } from 'chart.js';

import { Destination } from 'src/app/models/destination.model';
import { Itinerary } from 'src/app/models/itinerary.model';
import { Place } from 'src/app/models/place.model';
import { DestinationService } from 'src/app/services/destination.service';
import { ItinerariesService } from 'src/app/services/itineraries.service';
import { LogInService } from 'src/app/services/log-in.service';
import { PlacesService } from 'src/app/services/places.service';


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  destinations!: Destination[]
  places!: Place[]
  itineraries!: Itinerary[]
  chart!: Destination[]
  chartData: any


  constructor(public serviceDest: DestinationService, public servicePlaces: PlacesService, public serviceItineraries: ItinerariesService, private logService: LogInService) { }


  ngOnInit(): void {
    this.serviceDest.getDestinations().subscribe((data) => {
      this.destinations = data.content;
    });

    this.servicePlaces.getPlaces().subscribe((data) => {
      this.places = data.content
    })

    this.serviceItineraries.getItineraries().subscribe((data) => {
      this.itineraries = data.content;
      console.log(this.itineraries)
    });

    this.serviceDest.getChart().subscribe((data) => {
      this.chart = data.destinations;
      this.barChartData = {
        labels: this.chart.map((d: any) => d.name),
        datasets: [{
          data: this.chart.map((d: any) => d.views),
          label: "Views"
        }]
      }
    });
  }

  public barChartOptions: ChartConfiguration['options'] = {
    responsive: true,
    scales: {
      x: {},
      y: {
        beginAtZero: true
      }
    },
    plugins: {
      legend: {
        display: true,
      }
    }
  };
  public barChartType: ChartType = 'bar';

  public barChartData!: ChartData<'bar'>;


}
