import { DataSource } from '@angular/cdk/table';
import { CollectionViewer } from '@angular/cdk/collections';
import { Observable, BehaviorSubject, of } from "rxjs";
import { catchError, finalize } from "rxjs/operators";
import { Page } from '../models/page.model';
import { CustomerService } from './customers.service';
import { CustomerInfo } from '../models/customer.model';
 
export class CustomerDataSource implements DataSource<CustomerInfo>{
 
    private resultList = new BehaviorSubject<CustomerInfo[]>([]);
    private loadingSubject = new BehaviorSubject<boolean>(false);
    private countSubject = new BehaviorSubject<number>(0);
    public counter$ = this.countSubject.asObservable();
 
    constructor(private customerService: CustomerService) { }
 
    connect(collectionViewer: CollectionViewer): Observable<CustomerInfo[]> {
        return this.resultList.asObservable();
    }
 
    disconnect(collectionViewer: CollectionViewer): void {
        this.resultList.complete();
        this.loadingSubject.complete();
        this.countSubject.complete();
    }
 
    loadCustomers(pageNumber: number, pageSize: number, sortBy: string, desc: boolean, q: any, valid: any) {
        this.loadingSubject.next(true);
        if(q == undefined && valid == undefined){ // Load if no search query is defined
            this.customerService.getCustomers(pageNumber, pageSize, sortBy, desc)
            .subscribe(result => this.loadSourceData(result));
        } else { // Or else perform search
            this.customerService.searchCustomers(pageNumber, pageSize, sortBy, desc, q, valid)
            .subscribe(result => this.loadSourceData(result));
        }
    }

    loadSourceData(result: Page){
        this.resultList.next(result.content);
        this.countSubject.next(result.totalElements);
        this.loadingSubject.next(false);
    }
}