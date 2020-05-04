import { Component, OnInit } from '@angular/core';
import { UserService } from '../../services/user.service';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { SwalService } from '../../services/swal.service';
import { FormsService } from '../../services/forms.service';
import { DebitService } from '../../services/debit.service';

@Component({
  selector: 'app-debit',
  templateUrl: './debit.component.html',
  styleUrls: ['./debit.component.css']
})
export class DebitComponent implements OnInit {

  user: any;
  form: FormGroup;

  constructor(private userService: UserService,
              private fb: FormBuilder,
              private swalService: SwalService,
              private fv: FormsService,
              private debitService: DebitService) { }

  ngOnInit() {
    this.getUserSummary();
    this.form = this.fb.group({
      accountNumber: ['', Validators.required],
      amount: ['', Validators.required],
      description: ['', Validators.required]
    });
  }

  getUserSummary() {
    this.userService.getUserSummary()
      .subscribe( resp => {
        if (resp.success) {
          this.user = resp.data;
        } else {
          this.swalService.showError(resp.message);
        }
      }, err => this.swalService.showUnknownError() );
  }

  sendDebit() {

    if (this.form.invalid) {
      return this.fv.markFormGroupTouched(this.form);
    }

    if (this.form.value.amount <= 0) {
      return this.swalService.showError('El monto debe ser mayor a 0');
    }

    this.swalService.showLoading('Haciendo Débito');

    this.debitService.sendDebit(this.form.value)
      .subscribe( resp => {
        if (resp.success) {
          this.form.reset();
          this.swalService.showSuccess('Éxito', 'Se ha realizado el débito');
        } else {
          this.swalService.showError(resp.message);
        }
      }, err => this.swalService.showUnknownError());

  }

  get accountNumberValid() {
    return this.form.get('accountNumber').invalid && this.form.get('accountNumber').touched;
  }

  get amountValid() {
    return this.form.get('amount').invalid && this.form.get('amount').touched;
  }

  get descriptionValid() {
    return this.form.get('description').invalid && this.form.get('description').touched;
  }

  get userAdmin() {
    return this.user.roles[0] === 'ROLE_ADMIN';
  }

}
