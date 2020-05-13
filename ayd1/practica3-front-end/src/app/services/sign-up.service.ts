import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { APIResponse } from '../interfaces/interfaces';

@Injectable({
  providedIn: 'root'
})
export class SignUpService {

  endPoint = 'auth/signup';

  constructor(private http: HttpClient) { }

  signUp(user: any) {
    return this.http.post<APIResponse>(`${environment.apiUrl}/${this.endPoint}`, user, {
      headers: {
        Authorization: `Bearer ${sessionStorage.getItem('token')}`
      }
    });
  }

}
