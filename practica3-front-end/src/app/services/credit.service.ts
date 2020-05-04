import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { APIResponse } from '../interfaces/interfaces';

const endPoint = `${environment.apiUrl}/credits`;

@Injectable({
  providedIn: 'root'
})
export class CreditService {

  constructor(private http: HttpClient) { }

  getAllCredits(): Observable<APIResponse> {
    return this.http.get<APIResponse>(endPoint, {
      headers: this.getHeaders()
    });
  }

  changeCreditStatus(id: number, status: string): Observable<APIResponse> {
    return this.http.put<APIResponse>(`${endPoint}/${id}/${status}`, null, {
       headers: this.getHeaders()
    });
  }

  getHeaders(){
    return { Authorization: `Bearer ${localStorage.getItem('token')}`};
  }

}
