import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
@Injectable({
  providedIn: 'root',
})
export class FileUploadService {
  baseApiUrl = 'http://localhost:8080/api/upload';

  constructor(private http: HttpClient) {}

  upload(file: File): Observable<any> {
    const formData = new FormData();
    const headers = new HttpHeaders({ Accept: 'text/csv' });

    formData.append('file', file, file.name);

    return this.http.post(this.baseApiUrl, formData, {
      headers: headers,
      responseType: 'text',
    });
  }
}
