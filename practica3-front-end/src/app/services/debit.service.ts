import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { APIResponse } from '../interfaces/interfaces';

const endPoint = `${environment.apiUrl}/debits`;

@Injectable({
  providedIn: 'root'
})
export class DebitService {

  constructor(private http: HttpClient) { }
  
  sendDebit(request: any): Observable<APIResponse> {
    return this.http.put<APIResponse>(`${endPoint}`, request, {
      headers: this.getHeaders()
    });
  }

  getHeaders() {
    return { Authorization: `Bearer ${localStorage.getItem('token')}`};
  }

}
