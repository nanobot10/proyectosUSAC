import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { APIResponse } from '../interfaces/interfaces';
import { environment } from '../../environments/environment';
import { BehaviorSubject } from 'rxjs';
import { Router } from '@angular/router';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  private loggedIn = new BehaviorSubject<boolean>(false);

  get isLoggedIn() {
    return this.loggedIn.asObservable();
  }

  endPoint = 'auth/signin';

  constructor(private http: HttpClient, private router: Router, private auth: AuthService) { }

  doLogin( loginData: any ) {
    return this.http.post<APIResponse>(`${environment.apiUrl}/${this.endPoint}`, loginData)
      .pipe( map((resp) => {
        if (resp.success) {
          this.loggedIn.next(true);
        }
        return resp;
      }) );
  }

  doLogout() {
    sessionStorage.removeItem('token');
    this.loggedIn.next(false);
    this.router.navigate(['login']);
  }

  isAuthenticated(){
    if (this.auth.isAuthenticated()) {
      this.loggedIn.next(true);
    }
  }

}
