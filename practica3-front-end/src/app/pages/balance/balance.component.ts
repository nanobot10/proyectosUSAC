import { Component, OnInit } from '@angular/core';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-balance',
  templateUrl: './balance.component.html',
  styleUrls: ['./balance.component.css']
})
export class BalanceComponent implements OnInit {

  user: any;

  constructor(private userService: UserService) { }

  ngOnInit() {
    this.userService.getUserBalance()
      .subscribe( resp => {
        if (resp.success) {
          this.user = resp.data;
        }
      }, err => console.log(err));
  }

}
