import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { LoginService } from '../../services/login.service';
import { Router } from '@angular/router';
import { FormsService } from '../../services/forms.service';
import Swal from 'sweetalert2';

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
              private fv: FormsService) { }

  ngOnInit() {
    this.form = this.fb.group({
      userCode: ['1000', Validators.required],
      username: ['admin.admin', Validators.required],
      password: ['password', Validators.required]
    });
  }


  login(){
    if (this.form.invalid) {
      return this.fv.markFormGroupTouched(this.form);
    }
    
    Swal.fire({
      title: 'Espere',
      text: 'Iniciando Sesión',
      icon: 'info',
      allowOutsideClick: false
    });
    Swal.showLoading();

    this.loginService.doLogin(this.form.value)
      .subscribe( (resp: any) => {
        localStorage.setItem('token', resp);
        this.router.navigate(['home']);
      }, err => {
        if (err.status === 401) {
          Swal.fire({
            title: 'Error',
            text: 'Datos de inicio de sesión incorrectos',
            icon: 'error'
          });
        } else {
          Swal.fire({
            title: 'Error',
            text: 'Ha ocurrido un error desconocido, intente más tarde' ,
            icon: 'error'
          });
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
