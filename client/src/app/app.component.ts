import {AfterViewInit, Component, ViewChild} from '@angular/core';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import { CustomerDataSource } from './services/customer.datasource';
import { CustomerService } from './services/customers.service';
import { tap} from 'rxjs/operators';
import { CountryService } from './services/country.service';
import { Country } from './models/country.model';
import { FormControl } from '@angular/forms';
import { Title } from '@angular/platform-browser';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements AfterViewInit {
  title = "App";
  displayedColumns: string[] = ['name', 'phone', 'country', 'valid'];
  dataSource!: CustomerDataSource;
  selectOptions! : Country [];
  countryFormControl = new FormControl(undefined);
  statusFormControl = new FormControl(undefined);



  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort, { static: false }) sort!: MatSort;

  constructor(private customerService: CustomerService, private countryService: CountryService, private titleService: Title) {}

  ngOnInit() {
    this.dataSource = new CustomerDataSource(this.customerService);
    this.titleService.setTitle("Jumia demo app");
  }

  ngAfterViewInit() {

    // First load
    this.loadCustomers();

    this.countryService.getCustomers().subscribe(result => {
      this.selectOptions = result;
    });

    this.dataSource.counter$
    .pipe(
      tap((count) => {
        this.paginator.length = count;
      })
    )
    .subscribe();

    // Registering reload event on sort change
    this.paginator.page
    .pipe(
      tap(() => this.loadCustomers())
    )
    .subscribe();

    // Registering reload event on sort change
    this.sort.sortChange
    .pipe(
      tap(() => this.loadCustomers())
    )
    .subscribe();
  }

  onSelectionChange(){
    this.paginator.firstPage();
    this.loadCustomers();
  }

  loadCustomers() {
    let q = undefined;
    if(this.countryFormControl.value != undefined){
      let selectedCountry: Country = this.countryFormControl.value;
      q = selectedCountry.code;
    }
    let valid = this.statusFormControl.value;
    let sortDirection = this.findSortDirection(this.sort.direction);
    this.dataSource.loadCustomers(this.paginator.pageIndex, this.paginator.pageSize, this.sort.active, sortDirection, q, valid);
  }

  findSortDirection(sortDirection: string): any{
    switch (sortDirection) {
      case "asc":
        return false;
      case "desc":
        return true;
      default:
        return undefined;
    }
  }
}