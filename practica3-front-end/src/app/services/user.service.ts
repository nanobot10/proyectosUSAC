import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { APIResponse } from '../interfaces/interfaces';
import { Observable } from 'rxjs';

const endPoint =  `${environment.apiUrl}/users`;

@Injectable({
  providedIn: 'root'
})
export class UserService {

  

  constructor(private http: HttpClient) { }

  getUserProfile(): Observable<APIResponse> {
    return this.http.get<APIResponse>(`${endPoint}/user-profile`, {
      headers: this.getHeaders()
    });
  }

  getUserBalance(): Observable<APIResponse> {
    return this.http.get<APIResponse>(`${endPoint}/balance`, {
      headers: this.getHeaders()
    });
  }

  doTransfer(request: any): Observable<APIResponse> {
    return this.http.post<APIResponse>(`${endPoint}/transfer`, request, {
      headers: this.getHeaders()
    });
  }

  getHeaders(){
    return { Authorization: `Bearer ${localStorage.getItem('token')}`};
  }

}
