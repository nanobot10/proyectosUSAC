import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { APIResponse } from '../interfaces/interfaces';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  endPoint = 'auth/signin';

  constructor(private http: HttpClient) { }

  doLogin( loginData: any ) {
    return this.http.post<APIResponse>(`${environment.apiUrl}/${this.endPoint}`, loginData)
      .pipe( map((resp) => {
        return resp.data.accessToken;
      }) );
  }

}
