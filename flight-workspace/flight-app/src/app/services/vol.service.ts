import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Vol {
  id: string;
  villeDepart: string;
  villeArrivee: string;
  dateDepart: string;
  dateArrivee: string;
  prix: number;
  tempsTrajet: number;
}

@Injectable({ providedIn: 'root' })
export class VolService {
  private apiUrl = 'http://localhost:8080/api/vols';

  constructor(private http: HttpClient) {}

  getVols(filters: any): Observable<Vol[]> {
    let params = new HttpParams();

    if (filters.villeDepart) params = params.set('villeDepart', filters.villeDepart);
    if (filters.villeArrivee) params = params.set('villeArrivee', filters.villeArrivee);
    if (filters.dateDepart) params = params.set('dateDepart', filters.dateDepart);
    if (filters.dateArrivee) params = params.set('dateArrivee', filters.dateArrivee);
    if (filters.tri) params = params.set('tri', filters.tri);
    if (filters.ordre) params = params.set('ordre', filters.ordre);

    return this.http.get<Vol[]>(this.apiUrl, { params });
  }
}
