import { Injectable } from '@angular/core';
import { Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import { Country } from '../models/country.model';

@Injectable({
  providedIn: 'root',
})
export class CountryService {

    private countryUrl = 'http://localhost:8080/countries/';

    constructor(private httpClient: HttpClient) { }

    getCustomers(): Observable<Country[]> {
        return this.httpClient.get<Country[]>(this.countryUrl);
    }
}