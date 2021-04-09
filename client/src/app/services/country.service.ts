import { Injectable } from '@angular/core';
import { Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import { Country } from '../models/country.model';
import { BaseUrL } from '../models/url.const';

@Injectable({
  providedIn: 'root',
})
export class CountryService {

    private countryUrl = BaseUrL.Url + 'countries/';

    constructor(private httpClient: HttpClient) { }

    getCustomers(): Observable<Country[]> {
        return this.httpClient.get<Country[]>(this.countryUrl);
    }
}