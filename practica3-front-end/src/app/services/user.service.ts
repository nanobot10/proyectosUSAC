import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { APIResponse } from '../interfaces/interfaces';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  endPoint = 'users';

  constructor(private http: HttpClient) { }

  getUserProfile(): Observable<APIResponse> {
    return this.http.get<APIResponse>(`${environment.apiUrl}/${this.endPoint}/user-profile`,{
      headers: this.getHeaders()
    });
  }

  getUserBalance(): Observable<APIResponse> {
    return this.http.get<APIResponse>(`${environment.apiUrl}/${this.endPoint}/balance`,{
      headers: this.getHeaders()
    });
  }

  getHeaders(){
    return { Authorization: `Bearer ${localStorage.getItem('token')}`};
  }

}
