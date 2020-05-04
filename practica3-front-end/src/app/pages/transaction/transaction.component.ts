import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators  } from '@angular/forms';
import { FormsService } from '../../services/forms.service';
import { UserService } from '../../services/user.service';
import Swal from 'sweetalert2';
import { SwalService } from '../../services/swal.service';

@Component({
  selector: 'app-transaction',
  templateUrl: './transaction.component.html',
  styleUrls: ['./transaction.component.css']
})
export class TransactionComponent implements OnInit {

  form: FormGroup;
  user: any;

  constructor(private fb: FormBuilder,
              private fv: FormsService,
              private userService: UserService,
              private swalService: SwalService) { }

  ngOnInit() {
    this.getUserBalance();
    this.form = this.fb.group({
      accountNumber: ['', Validators.required],
      amount: ['', [Validators.required]]
    });
  }

  getUserBalance(){
    this.userService.getUserSummary()
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

    if (this.form.value.accountNumber === this.user.accountNumber) {
      return this.swalService.showError('No puedes trasferirte a tu propia cuenta');
    }

    if(this.form.value.amount <= 0){
      return this.swalService.showError('El monto debe ser mayor a 0');
    }

    if (this.form.value.amount > this.user.balance) {
      return this.swalService.showError('No tienes fondos suficientes');
    }

    this.swalService.showLoading('Realizando Transferencia');
    this.userService.doTransfer(this.form.value)
      .subscribe( resp => {
        if (resp.success) {
          this.getUserBalance();
          this.swalService.showSuccess('Éxito', 'Transferencia Realizada');
          this.form.reset();
        } else {
          this.swalService.showError(resp.message);
        }
      }, err => {
          this.swalService.showUnknownError();
      } );
  }


  get accountNumberValid() {
    return this.form.get('accountNumber').invalid && this.form.get('accountNumber').touched;
  }

  get amountValid() {
    return this.form.get('amount').invalid && this.form.get('amount').touched;
  }

  get userAdmin() {
    return this.user.roles[0] === 'ROLE_ADMIN';
  }


}
