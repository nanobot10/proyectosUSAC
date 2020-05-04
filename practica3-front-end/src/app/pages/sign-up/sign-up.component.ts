import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { FormsService } from '../../services/forms.service';
import Swal from 'sweetalert2';
import { RouteReuseStrategy, Router } from '@angular/router';
import { SignUpService } from '../../services/sign-up.service';
import { SwalService } from '../../services/swal.service';

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styleUrls: ['./sign-up.component.css']
})
export class SignUpComponent implements OnInit {

  form: FormGroup;

  constructor(private fb: FormBuilder,
              private fv: FormsService,
              private router: Router,
              private signUpService: SignUpService,
              private swalService: SwalService) { }

  ngOnInit() {
    this.form = this.fb.group({
      name: ['', Validators.required],
      username: ['', Validators.required],
      email: ['', [
          Validators.required, 
          Validators.pattern('[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,3}$')
        ]
      ],
      password: ['', [
          Validators.required,
          Validators.pattern('(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[$@$!%*?&])[A-Za-z\d$@$!%*?&].{8,}')
        ]
      ]
    });
  }

  signUp() {
    if ( this.form.invalid ) {
      return this.fv.markFormGroupTouched(this.form);
    }

    this.swalService.showLoading('Guardando Información');
    this.signUpService.signUp(this.form.value)
      .subscribe( resp => {

        if (resp.success) {

          this.swalService.showConfirmButton('Usuario Creado', `Tu código de usuario es ${resp.data.userCode}`)
            .then(res => this.router.navigate(['login']));

        } else {
          Swal.fire({
            title: 'Error',
            text: resp.message ,
            icon: 'error'
          });
        }

      }, err => {
        Swal.fire({
          title: 'Error',
          text: 'Ha ocurrido un error desconocido' ,
          icon: 'error'
        });
      });
  }



  get nameValid() {
    return this.form.get('name').invalid && this.form.get('name').touched;
  }
  get usernameValid() {
    return this.form.get('username').invalid && this.form.get('username').touched;
  }
  get emailValid() {
    return this.form.get('email').invalid && this.form.get('email').touched;
  }
  get passwordValid() {
    return this.form.get('password').invalid && this.form.get('password').touched;
  }

}
