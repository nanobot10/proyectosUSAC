import { Component, OnInit } from '@angular/core';
import { UserService } from '../../services/user.service';
import { SwalService } from '../../services/swal.service';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { FormsService } from '../../services/forms.service';
import { CreditService } from '../../services/credit.service';

@Component({
  selector: 'app-credit',
  templateUrl: './credit.component.html',
  styleUrls: ['./credit.component.css']
})
export class CreditComponent implements OnInit {

  user: any;
  form: FormGroup;
  credits: any[] = [];

  constructor(private userService: UserService,
              private swalService: SwalService,
              private fb: FormBuilder,
              private fv: FormsService,
              private creditService: CreditService) { }

  ngOnInit() {
    this.getUserSummary();
    this.form = this.fb.group({
      amount: ['', Validators.required],
      description: ['', Validators.required]
    });
  }

  changeCreditStatus(id: number, status: string) {
    this.swalService.showLoading('Cambiando estado del credito');
    this.creditService.changeCreditStatus(id, status)
      .subscribe( resp => {
        if (resp.success) {
          this.getAllCredits();
          this.swalService.close();
        } else {
          this.swalService.showError(resp.message);
        }
      }, err => this.swalService.showUnknownError());
  }

  getUserSummary() {
    this.userService.getUserSummary()
    .subscribe( resp => {
      if (resp.success) {
        this.user = resp.data;
        if (this.userAdmin) {
          this.getAllCredits();
        }
      }
    });
  }

  getAllCredits() {
    this.creditService.getAllCredits()
      .subscribe( resp => {
        if (resp.success) {
          this.credits = resp.data;
        }
      }, err => console.log(err));
  }


  sendCredit() {
    if (this.form.invalid) {
      return this.fv.markFormGroupTouched(this.form);
    }

    if (this.form.value.amount <= 0) {
      return this.swalService.showError('El monto debe ser mayor a 0');
    }

    this.swalService.showLoading('Enviando Solicitud');

    this.userService.sendCredit(this.form.value)
      .subscribe( resp => {
        if (resp.success) {
          this.getUserSummary();
          this.form.reset();
          this.swalService.showSuccess('Éxito', 'Solicitud enviada, espere aprobación del administrador');
        } else {
          this.swalService.showError(resp.message);
        }
      }, err => this.swalService.showUnknownError() );
  }

  get creditsIsEmpty() {
    return !this.user.credits || this.user.credits.length <= 0;
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
