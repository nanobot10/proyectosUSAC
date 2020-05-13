import { Component, OnInit } from '@angular/core';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  user: any;

  constructor(private userService: UserService) { }

  ngOnInit() {
    this.user = this.userService.getUserProfile()
      .subscribe( resp => {
        if (resp.success) {
          this.user = resp.data;
        }
      }, err => console.log(err) );
  }

}
