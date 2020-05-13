import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { LoginService } from '../../services/login.service';
import { Router } from '@angular/router';
import { FormsService } from '../../services/forms.service';
import Swal from 'sweetalert2';
import { SwalService } from '../../services/swal.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  form: FormGroup;

  constructor(private fb: FormBuilder,
              private loginService: LoginService,
              public router: Router,
              private fv: FormsService,
              private swalService: SwalService) { }

  ngOnInit() {
    this.form = this.fb.group({
      userCode: ['1000', Validators.required],
      username: ['admin1', Validators.required],
      password: ['holaHola10!', Validators.required]
    });
  }


  login(){
    if (this.form.invalid) {
      return this.fv.markFormGroupTouched(this.form);
    }

    this.swalService.showLoading('Iniciando Sesión');
    this.loginService.doLogin(this.form.value)
      .subscribe( (resp) => {
        if (resp.success) {
          sessionStorage.setItem('token', resp.data.accessToken);
          this.swalService.close();
          this.router.navigate(['home']);
        } else {
          this.swalService.showError('Datos de inicio de sesión incorrectos');
        }
      }, err => {
        if (err.status === 401) {
          this.swalService.showError('Datos de inicio de sesión incorrectos');
        } else {
          this.swalService.showUnknownError();
        }
      });
  }


  get userCodeValid() {
    return this.form.get('userCode').invalid && this.form.get('userCode').touched;
  }

  get usernameValid() {
    return this.form.get('username').invalid && this.form.get('username').touched;
  }

  get passwordValid() {
    return this.form.get('password').invalid && this.form.get('password').touched;
  }

}
