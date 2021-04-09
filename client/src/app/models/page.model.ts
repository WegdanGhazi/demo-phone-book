import { CustomerInfo } from "./customer.model";

export class Page {
    totalElements!: number;
    totalPages!: number;
    numberOfElements!: number;
    size!: number;
    number!: number;
    first!: boolean;
    last!: boolean;
    empty!: boolean;
    content!: CustomerInfo[];
}