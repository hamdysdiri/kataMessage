import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/internal/Observable';
import { environment } from '../../environments/environment.development';
import { PartnerRequestDTO } from '../models/PartnerRequestDTO';

@Injectable({
  providedIn: 'root'
})
export class PartnerService {

  constructor(private http: HttpClient) { }

  getAllPartners(): Observable<any> {
    return this.http.get(`${environment.apiBaseUrl}/partners/getAll`);
  }

  createPartner(partner: PartnerRequestDTO): Observable<any> {
    return this.http.post(`${environment.apiBaseUrl}/partners/create`, partner);
  }

  deletePartner(id: string): Observable<any> {
    return this.http.delete(`${environment.apiBaseUrl}/partners/delete/${id}`);
  }

}