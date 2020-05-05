import { Component, OnInit } from '@angular/core';
import { UserService } from '../../services/user.service';
import { SwalService } from '../../services/swal.service';

@Component({
  selector: 'app-balance',
  templateUrl: './balance.component.html',
  styleUrls: ['./balance.component.css']
})
export class BalanceComponent implements OnInit {

  user: any;
  users: any[] = [];
  accountNumber: string;

  constructor(private userService: UserService,
              private swalService: SwalService) { }

  ngOnInit() {
    this.userService.getUserSummary()
      .subscribe( resp => {
        if (resp.success) {
          this.user = resp.data;
          if (this.userAdmin) {
           this.getAllUsers();
          }
        }
      }, err => console.log(err));
  }

  getAllUsers() {
    this.userService.getAllUsers()
      .subscribe( resp => {
        if (resp.success) {
          this.users = resp.data;
        } else {
          this.swalService.showError(resp.message);
        }
      }, err => this.swalService.showUnknownError());
  }

  get userAdmin() {
    return this.user.roles[0] === 'ROLE_ADMIN';
  }

}
