import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { LoginService } from '../../services/login.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  form: FormGroup;

  constructor(private fb: FormBuilder, private loginService: LoginService, public router: Router) { }

  ngOnInit() {
    this.form = this.fb.group({
      userCode: ['1000', Validators.required],
      username: ['admin.admin', Validators.required],
      password: ['password', Validators.required]
    });
  }


  login(){
    if (this.form.invalid) {
      return this.markFormGroupTouched(this.form);
    }
    this.loginService.doLogin(this.form.value)
      .subscribe( (resp: any) => {
        localStorage.setItem('token', resp);
        this.router.navigate(['home']);
      }, error => console.log('Bad Credentials'));
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

  private markFormGroupTouched(formGroup: FormGroup) {
    Object.values(formGroup.controls).forEach(control => {
      control.markAsTouched();
      if (control instanceof FormGroup) {
        if (control.controls) {
          this.markFormGroupTouched(control);
        }
      }
    });
  }

}
