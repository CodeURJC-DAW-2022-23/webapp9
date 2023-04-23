import { Component, Input } from '@angular/core';
import { User } from '../../models/user.model';
import { Review } from '../../models/review.model';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-review',
  templateUrl: './review.component.html',
  styleUrls: ['./review.component.css']
})
export class ReviewComponent {

  @Input() user!: User;
  @Input() review!: Review;

  constructor(private userService: UserService) { }

  loadUserImage(id: number): string {
    return this.userService.getImage(id);
  }

}
