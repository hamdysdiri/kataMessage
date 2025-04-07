import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment.development';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Message } from '../models/messages';

@Injectable({
  providedIn: 'root'
})
export class MessageService {

  constructor(private http: HttpClient) { }

  getAllMessages(): Observable<Message[]> {
    return this.http.get<Message[]>(`${environment.apiBaseUrl}/messages/getAll`);
  }

  getLastMessage(): Observable<Message> {
    return this.http.get<Message>(`${environment.apiBaseUrl}/messages/last-received`);
  }

  sendMessage(message: string): Observable<any> {
    return this.http.post(`${environment.apiBaseUrl}/messages/create`, message, {
      headers: new HttpHeaders({ 'Content-Type': 'text/plain' })
    });
  }

}
