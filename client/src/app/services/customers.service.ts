import { Injectable } from '@angular/core';
import { Page } from '../models/page.model';
import { Observable, of } from 'rxjs';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class CustomerService {

    private baseUrl = 'http://localhost:8080/';
    private searchUrl = `${this.baseUrl}search/`;

    constructor(private httpClient: HttpClient) { }

    // A method to perform load of all customers
    getCustomers(page: number, size: number, sortBy: string, desc: boolean): Observable<Page> {
        let requestUrl = `${this.baseUrl}?`;
        requestUrl += this.addCommonParameters(page, size, sortBy, desc);
        return this.httpClient.get<Page>(requestUrl);
    }

    // A method to perform search of customers, based on country and/or phone number validity
    searchCustomers(page: number, size: number, sortBy: string, desc: boolean, country: string, valid: boolean): Observable<Page> {
        let requestUrl = `${this.searchUrl}?`;
        requestUrl += this.addCommonParameters(page, size, sortBy, desc);
        requestUrl += this.addParameter("q", country);
        requestUrl += this.addParameter("valid", valid);
        return this.httpClient.get<Page>(requestUrl);
    }

    // adding parameters common to both previous service methods
    addCommonParameters(page: number, size: number, sortBy: string, desc: boolean): string {
        let requestUrl = '';
        requestUrl += this.addParameter("page", page);
        requestUrl += this.addParameter("size", size);
        requestUrl += this.addParameter("sortBy", sortBy);
        requestUrl += this.addParameter("desc", desc);
        return requestUrl;
    }

    // Generically adding a parameter to the request url
    addParameter(name: string, variable: any): string {
        if(variable == undefined){
            return '';
        }
        return `&${name}=${variable}`;
    }
}