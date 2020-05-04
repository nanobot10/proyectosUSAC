import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators  } from '@angular/forms';
import { FormsService } from '../../services/forms.service';
import { UserService } from '../../services/user.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-transaction',
  templateUrl: './transaction.component.html',
  styleUrls: ['./transaction.component.css']
})
export class TransactionComponent implements OnInit {

  form: FormGroup;
  user: any;

  constructor(private fb: FormBuilder, private fv: FormsService, private userService: UserService) { }

  ngOnInit() {
    this.getUserBalance();
    this.form = this.fb.group({
      accountNumber: ['', Validators.required],
      amount: ['', [Validators.required]]
    });
  }

  getUserBalance(){
    this.userService.getUserBalance()
    .subscribe( resp => {
      if (resp.success) {
        this.user = resp.data;
      }
    }, err => console.log(err));
  }

  doTransfer() {

    if (this.form.invalid) {
      return this.fv.markFormGroupTouched(this.form);
    }
    if (this.form.value.amount > this.user.balance) {
      this.showError('No tiene fondos suficientes');
      return;
    }

    Swal.fire({
      title: 'Espere',
      text: 'Realizando Transferencia',
      icon: 'info',
      allowOutsideClick: false
    });
    Swal.showLoading();
    this.userService.doTransfer(this.form.value)
      .subscribe( resp => {
        if (resp.success) {
          this.getUserBalance();
          Swal.fire({
            title: 'Éxito',
            text: 'Transferencia Realizada',
            icon: 'success'
          });
          this.form.reset();
        } else {
          this.showError(resp.message);
        }
      }, err => {

      } );
  }


  showError(message: string) {
    Swal.fire({
      title: 'Error',
      text: message ,
      icon: 'error'
    });
  }


  get accountNumberValid() {
    return this.form.get('accountNumber').invalid && this.form.get('accountNumber').touched;
  }

  get amountValid() {
    return this.form.get('amount').invalid && this.form.get('amount').touched;
  }


}
